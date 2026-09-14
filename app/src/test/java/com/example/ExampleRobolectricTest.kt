package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.PsychoEduLibraryData
import com.example.network.GeminiClient
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("PsychoEdu", appName)
    }

    @Test
    fun `test safety shutdown critical trigger detection`() {
        assertTrue(GeminiClient.isCrisisTrigger("I am thinking about suicide"))
        assertTrue(GeminiClient.isCrisisTrigger("I want to end my life"))
        assertTrue(GeminiClient.isCrisisTrigger("I feel like hurting myself"))
        assertFalse(GeminiClient.isCrisisTrigger("Teach me about CBT distortions"))
    }

    @Test
    fun `test no diagnosis safety rule detection`() {
        assertTrue(GeminiClient.isDiagnosisTrigger("Do I have depression?"))
        assertTrue(GeminiClient.isDiagnosisTrigger("Can you diagnose me with bipolar?"))
        assertFalse(GeminiClient.isDiagnosisTrigger("What is the history of cognitive psychology?"))
    }

    @Test
    fun `test psychoedu library data is populated`() {
        assertTrue(PsychoEduLibraryData.cognitiveDistortions.isNotEmpty())
        assertTrue(PsychoEduLibraryData.dbtSkills.isNotEmpty())
        assertTrue(PsychoEduLibraryData.psychologyTopics.isNotEmpty())
        assertEquals(
            "If you are in distress, please reach out for immediate help. Text HOME to 741741 to connect with the Crisis Text Line, or contact a local suicide and crisis hotline for immediate assistance.",
            GeminiClient.SAFETY_SHUTDOWN_TEXT
        )
    }
}
