package com.example.network

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class GeminiRequest(
    @Json(name = "contents") val contents: List<GeminiContent>,
    @Json(name = "systemInstruction") val systemInstruction: GeminiContent? = null,
    @Json(name = "generationConfig") val generationConfig: GeminiGenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class GeminiContent(
    @Json(name = "role") val role: String? = null,
    @Json(name = "parts") val parts: List<GeminiPart>
)

@JsonClass(generateAdapter = true)
data class GeminiPart(
    @Json(name = "text") val text: String
)

@JsonClass(generateAdapter = true)
data class GeminiGenerationConfig(
    @Json(name = "temperature") val temperature: Float = 0.7f
)

@JsonClass(generateAdapter = true)
data class GeminiResponse(
    @Json(name = "candidates") val candidates: List<GeminiCandidate>?
)

@JsonClass(generateAdapter = true)
data class GeminiCandidate(
    @Json(name = "content") val content: GeminiContent?
)

interface GeminiApi {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    const val SAFETY_SHUTDOWN_TEXT =
        "If you are in distress, please reach out for immediate help. Text HOME to 741741 to connect with the Crisis Text Line, or contact a local suicide and crisis hotline for immediate assistance."

    const val NO_DIAGNOSIS_TEXT =
        "I cannot diagnose health conditions. Only a licensed professional can do that. However, we can look at the educational concepts behind those feelings."

    const val SYSTEM_INSTRUCTION_TEXT =
        """You are an expert, empathetic psychoeducational study assistant. Your goal is to help the user learn about psychology frameworks and organize their personal thoughts.

YOU WILL USE THESE BUILT-IN FRAMEWORKS:
1. Cognitive Behavioral Therapy (CBT): Help users identify automatic negative thoughts and cognitive distortions (like all-or-nothing thinking or catastrophizing).
2. Dialectical Behavior Therapy (DBT): Explain concepts like mindfulness, emotional regulation, and distress tolerance.
3. General Psychoeducation: Teach the history, experiments, and theories of psychology clearly.

CRITICAL SAFETY RULES YOU MUST FOLLOW:
1. NO DIAGNOSIS: You are NOT a doctor or therapist. You CANNOT give a medical diagnosis or treatment advice. If the user asks "Do I have depression?" or similar, you must reply: "I cannot diagnose health conditions. Only a licensed professional can do that. However, we can look at the educational concepts behind those feelings."
2. SAFETY SHUTDOWN: If the user mentions self-harm, severe depression crisis, or suicide, immediately stop standard conversation and reply exactly with this text: "If you are in distress, please reach out for immediate help. Text HOME to 741741 to connect with the Crisis Text Line, or contact a local suicide and crisis hotline for immediate assistance."
3. RESPOND IN CHARACTER: Be encouraging, objective, educational, and ask thoughtful questions to help the user reflect."""

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        })
        .build()

    private val api: GeminiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GeminiApi::class.java)
    }

    /**
     * Inspects prompt for critical safety keywords (suicide, self-harm, end my life).
     */
    fun isCrisisTrigger(text: String): Boolean {
        val lower = text.lowercase()
        val crisisKeywords = listOf(
            "suicide", "kill myself", "end my life", "want to die",
            "self-harm", "cutting myself", "hurt myself", "take my own life",
            "hang myself", "slit my wrists", "overdose myself"
        )
        return crisisKeywords.any { lower.contains(it) }
    }

    /**
     * Inspects prompt for medical diagnosis questions ("Do I have depression?", etc.)
     */
    fun isDiagnosisTrigger(text: String): Boolean {
        val lower = text.lowercase()
        val diagnosisPatterns = listOf(
            "do i have depression", "do i have anxiety", "do i have bpd",
            "do i have bipolar", "am i depressed", "am i bipolar", "am i autistic",
            "diagnose me", "can you diagnose", "what mental illness do i have",
            "do i have adhd", "do i have ptsd"
        )
        return diagnosisPatterns.any { lower.contains(it) }
    }

    suspend fun sendMessage(
        history: List<Pair<String, Boolean>>, // text to isUser
        latestMessage: String
    ): Pair<String, Boolean> = withContext(Dispatchers.IO) {
        // Rule 2 check: Safety Shutdown
        if (isCrisisTrigger(latestMessage)) {
            return@withContext Pair(SAFETY_SHUTDOWN_TEXT, true)
        }

        // Rule 1 check: No Diagnosis
        if (isDiagnosisTrigger(latestMessage)) {
            return@withContext Pair(NO_DIAGNOSIS_TEXT, false)
        }

        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val contentsList = mutableListOf<GeminiContent>()
                // Add limited history
                history.takeLast(6).forEach { (msg, isUser) ->
                    contentsList.add(
                        GeminiContent(
                            role = if (isUser) "user" else "model",
                            parts = listOf(GeminiPart(text = msg))
                        )
                    )
                }
                // Add latest user message
                contentsList.add(
                    GeminiContent(
                        role = "user",
                        parts = listOf(GeminiPart(text = latestMessage))
                    )
                )

                val request = GeminiRequest(
                    contents = contentsList,
                    systemInstruction = GeminiContent(
                        parts = listOf(GeminiPart(text = SYSTEM_INSTRUCTION_TEXT))
                    ),
                    generationConfig = GeminiGenerationConfig(temperature = 0.7f)
                )

                val response = api.generateContent(apiKey, request)
                val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                if (!reply.isNullOrBlank()) {
                    return@withContext Pair(reply.trim(), false)
                }
            } catch (e: Exception) {
                // Fallback to offline educational assistant
            }
        }

        // High quality offline fallback psychoeducational engine
        val localResponse = generateLocalPsychoeducationalResponse(latestMessage)
        Pair(localResponse, false)
    }

    private fun generateLocalPsychoeducationalResponse(prompt: String): String {
        val lower = prompt.lowercase()

        return when {
            lower.contains("cbt") || lower.contains("distortion") || lower.contains("automatic thought") -> {
                "In Cognitive Behavioral Therapy (CBT), our feelings and behaviors are heavily shaped by our automatic thoughts rather than external events alone.\n\n" +
                        "When distress arises, our minds frequently employ **cognitive distortions**—habits of thinking that exaggerate or skew reality, such as:\n" +
                        "• **All-or-Nothing Thinking**: Seeing things as complete successes or utter failures with no middle ground.\n" +
                        "• **Catastrophizing**: Assuming the absolute worst-case scenario will inevitably unfold.\n" +
                        "• **Mind Reading**: Believing we know what others think of us without factual evidence.\n\n" +
                        "Would you like to examine a specific thought you had today and test what evidence exists for and against it?"
            }
            lower.contains("dbt") || lower.contains("tipp") || lower.contains("distress tolerance") -> {
                "Dialectical Behavior Therapy (DBT), developed by Dr. Marsha Linehan, synthesizes the balance between radical acceptance and active change.\n\n" +
                        "A core module is **Distress Tolerance**, featuring the **TIPP** skill when emotional arousal is high:\n" +
                        "• **T - Temperature**: Splashing cold water or holding ice to trigger the dive reflex.\n" +
                        "• **I - Intense Exercise**: Short bursts of movement to burn off adrenaline.\n" +
                        "• **P - Paced Breathing**: Lengthening exhalations to signal physical safety.\n" +
                        "• **P - Paired Muscle Relaxation**: Systematically tensing and releasing muscles.\n\n" +
                        "Which aspect of emotional regulation or distress tolerance would you like to explore together?"
            }
            lower.contains("stop") -> {
                "The **STOP** skill is one of DBT's most practical crisis intervention tools:\n\n" +
                        "1. **S - Stop!** Pause immediately. Don't speak, text, or act while intensely agitated.\n" +
                        "2. **T - Take a step back.** Breathe deeply, take physical distance from the trigger.\n" +
                        "3. **O - Observe.** What are the objective facts versus your internal interpretations?\n" +
                        "4. **P - Proceed mindfully.** Ask: 'What choice aligns with my Wise Mind and will make things better?'\n\n" +
                        "What situation currently challenges you where the STOP skill might provide clarity?"
            }
            lower.contains("pavlov") || lower.contains("classical conditioning") -> {
                "Ivan Pavlov's classical conditioning (1890s) revealed how organisms form associative memory connections.\n\n" +
                        "By pairing a neutral stimulus (like a bell or ticking metronome) with an unconditioned stimulus (food), the neutral stimulus eventually triggered a conditioned response (salivation) on its own.\n\n" +
                        "In daily life, this explains how notification chimes, specific locations, or scents can instantly trigger automatic physiological reactions or cravings.\n\n" +
                        "Have you noticed any conditioned stimulus-response patterns in your daily routine?"
            }
            lower.contains("skinner") || lower.contains("operant") -> {
                "B.F. Skinner pioneered **Operant Conditioning**, demonstrating that behavior is shaped by its consequences.\n\n" +
                        "• **Positive Reinforcement**: Adding something rewarding (e.g., praise, treats) to increase behavior.\n" +
                        "• **Negative Reinforcement**: Removing an aversive stimulus (e.g., taking an aspirin to remove a headache) to increase behavior.\n" +
                        "• **Variable-Ratio Schedules**: Unpredictable reward timing (like slot machines or social media feeds) produces the most persistent behavior.\n\n" +
                        "In CBT, we use this principle through 'Behavioral Activation'—scheduling rewarding activities to break depressive cycles."
            }
            lower.contains("reframe") || lower.contains("anxious") || lower.contains("worry") -> {
                "Let's practice a supportive cognitive reframe together. To start, could you share:\n\n" +
                        "1. What is the specific situation that occurred?\n" +
                        "2. What is the exact automatic thought running through your mind?\n" +
                        "3. If you were a supportive, objective friend examining the facts, what evidence contradicts that thought?\n\n" +
                        "Take your time, and we can explore a more balanced perspective step by step."
            }
            else -> {
                "That's a thoughtful question. From a psychological perspective, our emotional experiences involve an interplay between biological arousal, cognitive appraisals, and behavioral choices.\n\n" +
                        "We can explore this through:\n" +
                        "1. **CBT**: Looking at the underlying beliefs and cognitive distortions that influence how this feels.\n" +
                        "2. **DBT**: Utilizing mindfulness and distress tolerance to ground yourself in Wise Mind.\n" +
                        "3. **Psychology Theories**: Examining foundational experiments on learning, motivation, or schemas.\n\n" +
                        "Which framework would feel most helpful for you right now?"
            }
        }
    }
}
