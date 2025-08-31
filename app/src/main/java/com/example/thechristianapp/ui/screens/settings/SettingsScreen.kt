package com.example.thechristianapp.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Notifications Section
            item {
                Text(
                    text = "Notifications",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                NotificationSettings(
                    dailyVerseTime = uiState.dailyVerseTime,
                    onDailyVerseTimeChange = { viewModel.updateDailyVerseTime(it) },
                    prayerReminderTimes = uiState.prayerReminderTimes,
                    onPrayerReminderTimeChange = { viewModel.updatePrayerReminderTime(it) }
                )
            }
            
            // User Account Section
            item {
                Text(
                    text = "Account",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                UserAccountSection(
                    user = uiState.currentUser,
                    onSignIn = { viewModel.signIn() },
                    onSignOut = { viewModel.signOut() }
                )
            }
            
            // App Settings Section
            item {
                Text(
                    text = "App Settings",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                AppSettingsSection(
                    isDarkMode = uiState.isDarkMode,
                    onDarkModeChange = { viewModel.updateDarkMode(it) },
                    fontSize = uiState.fontSize,
                    onFontSizeChange = { viewModel.updateFontSize(it) }
                )
            }
            
            // About Section
            item {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            item {
                AboutSection(
                    appVersion = uiState.appVersion,
                    onPrivacyPolicy = { /* Navigate to privacy policy */ },
                    onTermsOfService = { /* Navigate to terms of service */ }
                )
            }
        }
    }
}

@Composable
private fun NotificationSettings(
    dailyVerseTime: LocalTime,
    onDailyVerseTimeChange: (LocalTime) -> Unit,
    prayerReminderTimes: List<LocalTime>,
    onPrayerReminderTimeChange: (LocalTime) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Daily Verse Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Daily Verse Time",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Receive a daily Bible verse",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                // Time picker would go here
                Text(
                    text = dailyVerseTime.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Prayer Reminder Times
            Text(
                text = "Prayer Reminder Times",
                style = MaterialTheme.typography.titleMedium
            )
            
            prayerReminderTimes.forEach { time ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = time.toString(),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    
                    IconButton(onClick = { /* Remove time */ }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Remove")
                    }
                }
            }
            
            OutlinedButton(
                onClick = { /* Add new time */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Prayer Time")
            }
        }
    }
}

@Composable
private fun UserAccountSection(
    user: com.example.thechristianapp.data.model.User?,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (user != null) {
                // User is signed in
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // User avatar would go here
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = user.displayName,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = user.email,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedButton(
                    onClick = onSignOut,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Sign Out")
                }
            } else {
                // User is not signed in
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Sign in to sync your data",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = onSignIn,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Filled.AccountCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Sign In with Google")
                    }
                }
            }
        }
    }
}

@Composable
private fun AppSettingsSection(
    isDarkMode: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Dark Mode Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dark Mode",
                    style = MaterialTheme.typography.titleMedium
                )
                
                Switch(
                    checked = isDarkMode,
                    onCheckedChange = onDarkModeChange
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Font Size Slider
            Text(
                text = "Font Size",
                style = MaterialTheme.typography.titleMedium
            )
            
            Slider(
                value = fontSize,
                onValueChange = onFontSizeChange,
                valueRange = 0.8f..1.4f,
                steps = 5
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Small", style = MaterialTheme.typography.bodySmall)
                Text("Large", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
private fun AboutSection(
    appVersion: String,
    onPrivacyPolicy: () -> Unit,
    onTermsOfService: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "The Christian App v$appVersion",
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedButton(
                onClick = onPrivacyPolicy,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Privacy Policy")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedButton(
                onClick = onTermsOfService,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Terms of Service")
            }
        }
    }
}
