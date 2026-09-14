package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.PsychoEduLibraryData
import com.example.ui.BreathingPhase
import com.example.ui.MainViewModel

@Composable
fun DbtScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var selectedSubTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Paced Breathing", "STOP Skill", "5-4-3-2-1 Grounding", "Opposite Action")

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("dbt_screen")
    ) {
        Surface(tonalElevation = 2.dp) {
            TabRow(selectedTabIndex = selectedSubTab) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedSubTab == index,
                        onClick = { selectedSubTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (selectedSubTab == index) FontWeight.Bold else FontWeight.Normal,
                                maxLines = 1
                            )
                        }
                    )
                }
            }
        }

        when (selectedSubTab) {
            0 -> BreathingPacerView(viewModel = viewModel)
            1 -> StopSkillView(viewModel = viewModel)
            2 -> Grounding54321View(viewModel = viewModel)
            3 -> OppositeActionView(viewModel = viewModel)
        }
    }
}

@Composable
fun BreathingPacerView(viewModel: MainViewModel) {
    val isBreathingActive by viewModel.isBreathingActive.collectAsStateWithLifecycle()
    val phase by viewModel.currentBreathingPhase.collectAsStateWithLifecycle()
    val secondsLeft by viewModel.breathingSecondsLeft.collectAsStateWithLifecycle()
    val cyclesCompleted by viewModel.cyclesCompleted.collectAsStateWithLifecycle()

    var showDistressLogDialog by remember { mutableStateOf(false) }

    val targetScale = when (phase) {
        BreathingPhase.INHALE -> 1.4f
        BreathingPhase.HOLD -> 1.4f
        BreathingPhase.EXHALE -> 0.85f
    }

    val animatedScale by animateFloatAsState(
        targetValue = if (isBreathingActive) targetScale else 1f,
        animationSpec = tween(
            durationMillis = if (phase == BreathingPhase.HOLD) 200 else 1000,
            easing = FastOutSlowInEasing
        ),
        label = "breathing_scale"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SelfImprovement,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                    Column {
                        Text(
                            text = "TIPP: Paced Diaphragmatic Breathing",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Exhaling longer than inhaling stimulates the vagus nerve and downregulates the autonomic nervous system.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))

            // Pulsing Breathing Visualizer Circle
            Box(
                modifier = Modifier
                    .size(220.dp)
                    .scale(animatedScale)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (isBreathingActive) phase.name else "READY",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    if (isBreathingActive) {
                        Text(
                            text = "$secondsLeft",
                            style = MaterialTheme.typography.displayMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(
                            text = "4-4-6 Cycle",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isBreathingActive) phase.label else "Tap Start to begin paced parasympathetic breathing",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            if (cyclesCompleted > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Cycles Completed: $cyclesCompleted",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = { viewModel.toggleBreathingPacer() },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("toggle_breathing_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = if (isBreathingActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isBreathingActive) "Pause Pacer" else "Start Paced Breathing")
                }

                if (cyclesCompleted > 0 && !isBreathingActive) {
                    OutlinedButton(
                        onClick = { showDistressLogDialog = true },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Log Distress Level")
                    }
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Why 4-4-6 Breathing Works (Psychoeducation)",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "During stress, the sympathetic nervous system triggers rapid shallow breathing. By consciously extending the exhalation to 6 seconds, baroreceptors signal the brainstem to slow cardiac output, breaking the adrenaline loop.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }

    if (showDistressLogDialog) {
        DbtDistressLogDialog(
            skillName = "TIPP: Paced Breathing ($cyclesCompleted cycles)",
            category = "Distress Tolerance",
            onDismiss = { showDistressLogDialog = false },
            onSave = { before, after, notes ->
                viewModel.logDbtExercise(
                    skillName = "TIPP: Paced Breathing",
                    category = "Distress Tolerance",
                    distressBefore = before,
                    distressAfter = after,
                    notes = notes
                )
                showDistressLogDialog = false
            }
        )
    }
}

@Composable
fun StopSkillView(viewModel: MainViewModel) {
    var step0Checked by remember { mutableStateOf(false) }
    var step1Checked by remember { mutableStateOf(false) }
    var step2Checked by remember { mutableStateOf(false) }
    var step3Checked by remember { mutableStateOf(false) }
    var userObservationNotes by remember { mutableStateOf("") }
    var showDistressLogDialog by remember { mutableStateOf(false) }

    val allDone = step0Checked && step1Checked && step2Checked && step3Checked

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "The STOP Skill Walkthrough",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = "A crisis pause tool designed to stop impulsive actions when emotional urges threaten to make matters worse.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Step S
        item {
            StopStepCard(
                letter = "S",
                title = "Stop! Freeze.",
                description = "Do not move a muscle. Do not speak, send that message, or react. Emotions want you to act blindly.",
                isChecked = step0Checked,
                onToggle = { step0Checked = !step0Checked }
            )
        }

        // Step T
        item {
            StopStepCard(
                letter = "T",
                title = "Take a step back.",
                description = "Physically remove yourself from the person or screen. Take an intentional slow, deep breath to disconnect.",
                isChecked = step1Checked,
                onToggle = { step1Checked = !step1Checked }
            )
        }

        // Step O
        item {
            StopStepCard(
                letter = "O",
                title = "Observe without judgment.",
                description = "Notice what is happening inside (racing heart, anger, shame) and outside (what are the factual events without interpretation)?",
                isChecked = step2Checked,
                onToggle = { step2Checked = !step2Checked }
            )
        }

        // Step P
        item {
            StopStepCard(
                letter = "P",
                title = "Proceed mindfully (Wise Mind).",
                description = "Ask: 'What will make this better? What will make this worse?' Act with calm intention rather than emotional impulse.",
                isChecked = step3Checked,
                onToggle = { step3Checked = !step3Checked }
            )
        }

        item {
            OutlinedTextField(
                value = userObservationNotes,
                onValueChange = { userObservationNotes = it },
                label = { Text("What did you observe? (Optional reflection)") },
                placeholder = { Text("e.g. My heart was pounding, I wanted to yell, but I waited 5 minutes.") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                minLines = 2
            )
        }

        item {
            Button(
                onClick = { showDistressLogDialog = true },
                enabled = allDone,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (allDone) "Complete & Log STOP Practice" else "Complete All 4 Steps Above")
            }
        }
    }

    if (showDistressLogDialog) {
        DbtDistressLogDialog(
            skillName = "STOP Skill Walkthrough",
            category = "Distress Tolerance",
            initialNotes = userObservationNotes,
            onDismiss = { showDistressLogDialog = false },
            onSave = { before, after, notes ->
                viewModel.logDbtExercise(
                    skillName = "STOP Skill",
                    category = "Distress Tolerance",
                    distressBefore = before,
                    distressAfter = after,
                    notes = notes
                )
                showDistressLogDialog = false
            }
        )
    }
}

@Composable
fun StopStepCard(
    letter: String,
    title: String,
    description: String,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
            else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (isChecked) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isChecked) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = letter,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun Grounding54321View(viewModel: MainViewModel) {
    var stepIndex by remember { mutableIntStateOf(0) }
    var showDistressLogDialog by remember { mutableStateOf(false) }

    val steps = listOf(
        Triple("5 Things You See", "Look around you. Notice small, unexpected details: light reflecting on glass, a wood grain pattern, or shadows on the wall.", Icons.Default.Visibility),
        Triple("4 Things You Can Touch", "Bring awareness to physical contact: the pressure of your feet against the floor, texture of your denim, or temperature of your hands.", Icons.Default.TouchApp),
        Triple("3 Things You Can Hear", "Close your eyes or look down. Listen for background layers: a humming refrigerator, passing cars, or your own breath.", Icons.Default.Hearing),
        Triple("2 Things You Can Smell", "Breathe in deeply through your nose. Notice the subtle scent of paper, fabric softener, hand lotion, or fresh air.", Icons.Default.SelfImprovement),
        Triple("1 Thing You Can Taste", "Focus on the sensation in your mouth. Take a sip of water, notice the lingering taste of mint, tea, or swallow mindfully.", Icons.Default.Psychology)
    )

    val current = steps[stepIndex]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "5-4-3-2-1 Sensory Grounding",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "Anchors awareness away from catastrophic internal thoughts and back into immediate sensory reality.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        item {
            LinearProgressIndicator(
                progress = { (stepIndex + 1) / 5f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Step ${stepIndex + 1} of 5",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = current.third,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = current.first,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = current.second,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (stepIndex > 0) {
                            OutlinedButton(
                                onClick = { stepIndex -= 1 },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text("Previous")
                            }
                        }

                        Button(
                            onClick = {
                                if (stepIndex < 4) {
                                    stepIndex += 1
                                } else {
                                    showDistressLogDialog = true
                                }
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(if (stepIndex < 4) "Next Sensory Step" else "Complete Grounding")
                        }
                    }
                }
            }
        }
    }

    if (showDistressLogDialog) {
        DbtDistressLogDialog(
            skillName = "5-4-3-2-1 Sensory Grounding",
            category = "Mindfulness & Grounding",
            onDismiss = { showDistressLogDialog = false },
            onSave = { before, after, notes ->
                viewModel.logDbtExercise(
                    skillName = "5-4-3-2-1 Sensory Grounding",
                    category = "Mindfulness",
                    distressBefore = before,
                    distressAfter = after,
                    notes = notes
                )
                showDistressLogDialog = false
            }
        )
    }
}

@Composable
fun OppositeActionView(viewModel: MainViewModel) {
    val emotions = listOf(
        OppositeActionItem("Fear / Anxiety", "Avoid, retreat, flee, hide", "Gently approach the non-dangerous situation. Stand tall, keep eye contact, take slow breaths."),
        OppositeActionItem("Anger", "Attack, lash out, scream, blame", "Gently withdraw, soften facial muscles and open palms (Half-Smile & Willing Hands), practice empathy."),
        OppositeActionItem("Sadness / Depression", "Isolate, stay in bed, withdraw, give up", "Get active! Walk outside, contact a friend, perform small tasks, listen to energizing music."),
        OppositeActionItem("Shame / Guilt (Unjustified)", "Hide, apologize excessively, punish oneself", "Hold head high, maintain eye contact, validate yourself, participate fully in social settings."),
        OppositeActionItem("Jealousy / Envy", "Devalue the person, obsess over competition", "Wish them well, count your own genuine values, focus on personal growth.")
    )

    var selectedEmotion by remember { mutableStateOf(emotions[0]) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "DBT Opposite Action Engine",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Every emotion has an action urge. When the emotion doesn't fit the objective facts, executing the exact opposite action shifts the emotion.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                    )
                }
            }
        }

        item {
            Text(
                text = "Select an Emotion to Regulate:",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(emotions) { emotion ->
                    FilterChip(
                        selected = selectedEmotion.emotion == emotion.emotion,
                        onClick = { selectedEmotion = emotion },
                        label = { Text(emotion.emotion) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = selectedEmotion.emotion,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Natural Emotional Urge (What it makes you want to do):",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedEmotion.urge,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "DBT Opposite Action (What regulates the feeling):",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = selectedEmotion.oppositeAction,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedButton(
                        onClick = {
                            viewModel.askAssistantAbout("How can I practice DBT Opposite Action for ${selectedEmotion.emotion}? What does 'all the way' mean in DBT?")
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(imageVector = Icons.Default.Psychology, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ask Assistant for Coaching on This")
                    }
                }
            }
        }
    }
}

data class OppositeActionItem(val emotion: String, val urge: String, val oppositeAction: String)

@Composable
fun DbtDistressLogDialog(
    skillName: String,
    category: String,
    initialNotes: String = "",
    onDismiss: () -> Unit,
    onSave: (distressBefore: Int, distressAfter: Int, notes: String) -> Unit
) {
    var distressBefore by remember { mutableFloatStateOf(7f) }
    var distressAfter by remember { mutableFloatStateOf(4f) }
    var notes by remember { mutableStateOf(initialNotes) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log DBT Skill Practice",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Skill: $skillName",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Distress Before: ${distressBefore.toInt()}/10",
                    style = MaterialTheme.typography.bodySmall
                )
                Slider(
                    value = distressBefore,
                    onValueChange = { distressBefore = it },
                    valueRange = 1f..10f,
                    steps = 8
                )

                Text(
                    text = "Distress After: ${distressAfter.toInt()}/10",
                    style = MaterialTheme.typography.bodySmall
                )
                Slider(
                    value = distressAfter,
                    onValueChange = { distressAfter = it },
                    valueRange = 1f..10f,
                    steps = 8,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.secondary,
                        activeTrackColor = MaterialTheme.colorScheme.secondary
                    )
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    placeholder = { Text("What did you notice during practice?") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(distressBefore.toInt(), distressAfter.toInt(), notes)
                }
            ) {
                Text("Save Log")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
