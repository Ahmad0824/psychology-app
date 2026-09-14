package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Transform
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.ScreenTab
import com.example.ui.components.CrisisSafetyCard
import com.example.ui.screens.AssistantScreen
import com.example.ui.screens.CbtScreen
import com.example.ui.screens.DbtScreen
import com.example.ui.screens.LibraryScreen
import com.example.ui.theme.CrisisRed
import com.example.ui.theme.PsychoEduTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PsychoEduTheme {
                val currentTab by viewModel.currentTab.collectAsStateWithLifecycle()
                val showCrisisModal by viewModel.showCrisisModal.collectAsStateWithLifecycle()

                Scaffold(
                    contentWindowInsets = WindowInsets.safeDrawing,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = stringResource(R.string.app_name),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            actions = {
                                IconButton(
                                    onClick = { viewModel.toggleCrisisModal(true) },
                                    modifier = Modifier.testTag("emergency_crisis_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Emergency,
                                        contentDescription = "Immediate Crisis Help",
                                        tint = CrisisRed
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 4.dp
                        ) {
                            NavigationBarItem(
                                selected = currentTab == ScreenTab.ASSISTANT,
                                onClick = { viewModel.setTab(ScreenTab.ASSISTANT) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Chat,
                                        contentDescription = "AI Assistant"
                                    )
                                },
                                label = { Text("Assistant") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag("nav_assistant")
                            )

                            NavigationBarItem(
                                selected = currentTab == ScreenTab.CBT,
                                onClick = { viewModel.setTab(ScreenTab.CBT) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Transform,
                                        contentDescription = "CBT Reframing"
                                    )
                                },
                                label = { Text("CBT") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag("nav_cbt")
                            )

                            NavigationBarItem(
                                selected = currentTab == ScreenTab.DBT,
                                onClick = { viewModel.setTab(ScreenTab.DBT) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.SelfImprovement,
                                        contentDescription = "DBT Tools"
                                    )
                                },
                                label = { Text("DBT") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag("nav_dbt")
                            )

                            NavigationBarItem(
                                selected = currentTab == ScreenTab.LIBRARY,
                                onClick = { viewModel.setTab(ScreenTab.LIBRARY) },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = "Psychology Library"
                                    )
                                },
                                label = { Text("Library") },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.testTag("nav_library")
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Crossfade(targetState = currentTab, label = "tab_crossfade") { tab ->
                            when (tab) {
                                ScreenTab.ASSISTANT -> AssistantScreen(viewModel = viewModel)
                                ScreenTab.CBT -> CbtScreen(viewModel = viewModel)
                                ScreenTab.DBT -> DbtScreen(viewModel = viewModel)
                                ScreenTab.LIBRARY -> LibraryScreen(viewModel = viewModel)
                            }
                        }
                    }

                    if (showCrisisModal) {
                        AlertDialog(
                            onDismissRequest = { viewModel.toggleCrisisModal(false) },
                            title = null,
                            text = {
                                CrisisSafetyCard(
                                    onDismiss = { viewModel.toggleCrisisModal(false) }
                                )
                            },
                            confirmButton = {
                                TextButton(onClick = { viewModel.toggleCrisisModal(false) }) {
                                    Text("Close")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}
