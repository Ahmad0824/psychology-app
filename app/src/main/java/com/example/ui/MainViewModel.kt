package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.ThoughtRepository
import com.example.model.CbtThoughtRecord
import com.example.model.ChatMessage
import com.example.model.DbtExerciseLog
import com.example.network.GeminiClient
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class ScreenTab {
    ASSISTANT,
    CBT,
    DBT,
    LIBRARY
}

enum class BreathingPhase(val label: String, val seconds: Int) {
    INHALE("Inhale deeply through your nose", 4),
    HOLD("Hold gently, feeling grounded", 4),
    EXHALE("Exhale slowly through your mouth", 6)
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ThoughtRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ThoughtRepository(db.thoughtDao())
    }

    // Navigation
    private val _currentTab = MutableStateFlow(ScreenTab.ASSISTANT)
    val currentTab: StateFlow<ScreenTab> = _currentTab.asStateFlow()

    fun setTab(tab: ScreenTab) {
        _currentTab.value = tab
    }

    // Safety Banner Dialog / Emergency Modal
    private val _showCrisisModal = MutableStateFlow(false)
    val showCrisisModal: StateFlow<Boolean> = _showCrisisModal.asStateFlow()

    fun toggleCrisisModal(show: Boolean) {
        _showCrisisModal.value = show
    }

    // --- Chat State ---
    private val initialGreeting =
        "Hello! I'm here to help you explore fascinating psychology frameworks, understand how the mind works, and organize your own thoughts and reflections in a structured, supportive way.\n\n" +
                "Whether you want to learn about classic psychological experiments, break down cognitive patterns using CBT, or practice emotional regulation tools from DBT, we can take it one step at a time.\n\n" +
                "What is on your mind today? Are there any specific psychological concepts you'd like to learn about, or is there a personal thought or situation you'd like to organize and reflect on?"

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = initialGreeting,
                isFromUser = false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    private val _safetyShutdownTriggered = MutableStateFlow(false)
    val safetyShutdownTriggered: StateFlow<Boolean> = _safetyShutdownTriggered.asStateFlow()

    fun sendMessage(text: String) {
        val trimmed = text.trim()
        if (trimmed.isBlank()) return

        val userMsg = ChatMessage(text = trimmed, isFromUser = true)
        _messages.value = _messages.value + userMsg

        // Check for immediate crisis shutdown
        if (GeminiClient.isCrisisTrigger(trimmed)) {
            _safetyShutdownTriggered.value = true
            val shutdownMsg = ChatMessage(
                text = GeminiClient.SAFETY_SHUTDOWN_TEXT,
                isFromUser = false,
                isCrisisShutdown = true
            )
            _messages.value = _messages.value + shutdownMsg
            return
        }

        // Check for diagnosis question
        if (GeminiClient.isDiagnosisTrigger(trimmed)) {
            val diagMsg = ChatMessage(
                text = GeminiClient.NO_DIAGNOSIS_TEXT,
                isFromUser = false,
                isNoDiagnosisNotice = true
            )
            _messages.value = _messages.value + diagMsg
            return
        }

        viewModelScope.launch {
            _isChatLoading.value = true
            try {
                val history = _messages.value.map { it.text to it.isFromUser }
                val (replyText, isShutdown) = GeminiClient.sendMessage(history, trimmed)
                if (isShutdown) {
                    _safetyShutdownTriggered.value = true
                }
                val aiMsg = ChatMessage(
                    text = replyText,
                    isFromUser = false,
                    isCrisisShutdown = isShutdown
                )
                _messages.value = _messages.value + aiMsg
            } catch (e: Exception) {
                _messages.value = _messages.value + ChatMessage(
                    text = "I'm having a brief connection issue. We can still explore any CBT, DBT, or psychology concepts from the library!",
                    isFromUser = false
                )
            } finally {
                _isChatLoading.value = false
            }
        }
    }

    fun resetChat() {
        _safetyShutdownTriggered.value = false
        _messages.value = listOf(
            ChatMessage(text = initialGreeting, isFromUser = false)
        )
    }

    // --- CBT Thought Record State ---
    val cbtRecords: StateFlow<List<CbtThoughtRecord>> = repository.allCbtRecords.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun saveCbtRecord(
        situation: String,
        automaticThought: String,
        distortions: List<String>,
        emotion: String,
        intensityBefore: Int,
        evidenceFor: String,
        evidenceAgainst: String,
        balancedThought: String,
        intensityAfter: Int,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val record = CbtThoughtRecord(
                situation = situation.trim(),
                automaticThought = automaticThought.trim(),
                distortions = distortions,
                emotion = emotion.trim().ifBlank { "Anxiety / Stress" },
                intensityBefore = intensityBefore,
                evidenceFor = evidenceFor.trim(),
                evidenceAgainst = evidenceAgainst.trim(),
                balancedThought = balancedThought.trim(),
                intensityAfter = intensityAfter
            )
            repository.saveCbtRecord(record)
            onSuccess()
        }
    }

    fun deleteCbtRecord(record: CbtThoughtRecord) {
        viewModelScope.launch {
            repository.deleteCbtRecord(record)
        }
    }

    // --- DBT Paced Breathing State ---
    private val _isBreathingActive = MutableStateFlow(false)
    val isBreathingActive: StateFlow<Boolean> = _isBreathingActive.asStateFlow()

    private val _currentBreathingPhase = MutableStateFlow(BreathingPhase.INHALE)
    val currentBreathingPhase: StateFlow<BreathingPhase> = _currentBreathingPhase.asStateFlow()

    private val _breathingSecondsLeft = MutableStateFlow(4)
    val breathingSecondsLeft: StateFlow<Int> = _breathingSecondsLeft.asStateFlow()

    private val _cyclesCompleted = MutableStateFlow(0)
    val cyclesCompleted: StateFlow<Int> = _cyclesCompleted.asStateFlow()

    private var breathingJob: Job? = null

    fun toggleBreathingPacer() {
        if (_isBreathingActive.value) {
            stopBreathingPacer()
        } else {
            startBreathingPacer()
        }
    }

    private fun startBreathingPacer() {
        _isBreathingActive.value = true
        _cyclesCompleted.value = 0
        breathingJob?.cancel()
        breathingJob = viewModelScope.launch {
            while (_isBreathingActive.value) {
                // Inhale 4s
                _currentBreathingPhase.value = BreathingPhase.INHALE
                for (s in 4 downTo 1) {
                    _breathingSecondsLeft.value = s
                    delay(1000)
                }

                // Hold 4s
                _currentBreathingPhase.value = BreathingPhase.HOLD
                for (s in 4 downTo 1) {
                    _breathingSecondsLeft.value = s
                    delay(1000)
                }

                // Exhale 6s
                _currentBreathingPhase.value = BreathingPhase.EXHALE
                for (s in 6 downTo 1) {
                    _breathingSecondsLeft.value = s
                    delay(1000)
                }

                _cyclesCompleted.value += 1
            }
        }
    }

    fun stopBreathingPacer() {
        _isBreathingActive.value = false
        breathingJob?.cancel()
        breathingJob = null
        _breathingSecondsLeft.value = 4
        _currentBreathingPhase.value = BreathingPhase.INHALE
    }

    // --- DBT Exercise Logs ---
    val dbtLogs: StateFlow<List<DbtExerciseLog>> = repository.allDbtLogs.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun logDbtExercise(
        skillName: String,
        category: String,
        distressBefore: Int,
        distressAfter: Int,
        notes: String
    ) {
        viewModelScope.launch {
            val log = DbtExerciseLog(
                skillName = skillName,
                skillCategory = category,
                distressBefore = distressBefore,
                distressAfter = distressAfter,
                notes = notes.trim()
            )
            repository.saveDbtLog(log)
        }
    }

    fun deleteDbtLog(log: DbtExerciseLog) {
        viewModelScope.launch {
            repository.deleteDbtLog(log)
        }
    }

    // Preset prompts from other screens into AI Assistant
    fun askAssistantAbout(query: String) {
        setTab(ScreenTab.ASSISTANT)
        sendMessage(query)
    }

    override fun onCleared() {
        super.onCleared()
        stopBreathingPacer()
    }
}
