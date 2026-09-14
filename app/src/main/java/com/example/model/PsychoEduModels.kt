package com.example.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cbt_thought_records")
data class CbtThoughtRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val situation: String,
    val automaticThought: String,
    val distortions: List<String>,
    val emotion: String,
    val intensityBefore: Int, // 0 - 100%
    val evidenceFor: String,
    val evidenceAgainst: String,
    val balancedThought: String,
    val intensityAfter: Int // 0 - 100%
)

@Entity(tableName = "dbt_exercise_logs")
data class DbtExerciseLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val skillName: String,
    val skillCategory: String, // Distress Tolerance, Emotion Regulation, Mindfulness
    val notes: String,
    val distressBefore: Int, // 1 - 10
    val distressAfter: Int // 1 - 10
)

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
    val isCrisisShutdown: Boolean = false,
    val isNoDiagnosisNotice: Boolean = false
)

data class CognitiveDistortion(
    val id: String,
    val title: String,
    val shortDescription: String,
    val example: String,
    val socraticQuestion: String
)

data class DbtSkill(
    val id: String,
    val title: String,
    val category: String, // Distress Tolerance, Emotion Regulation, Mindfulness
    val subtitle: String,
    val explanation: String,
    val actionableSteps: List<String>,
    val tip: String
)

data class PsychologyTopic(
    val id: String,
    val title: String,
    val pioneer: String,
    val category: String, // Classical & Operant, Cognitive, Developmental, Humanistic, Neuroscience
    val coreConcept: String,
    val landmarkExperiment: String,
    val realWorldApplication: String,
    val studyReflectionQuestion: String
)
