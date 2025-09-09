package com.example.thechristianapp.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.thechristianapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState) {
        when (uiState) {
            is SplashUiState.Loading -> {
                // Show loading state
            }
            is SplashUiState.Success -> {
                delay(1000) // Show success briefly
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
            is SplashUiState.Error -> {
                delay(2000) // Show error briefly
                navController.navigate("home") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // App Logo/Icon
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher),
                contentDescription = "App Icon",
                modifier = Modifier.size(120.dp)
            )
            
            // App Title
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            // Loading or Status
            when (uiState) {
                is SplashUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "Setting up your Bible app...",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                is SplashUiState.Success -> {
                    Text(
                        text = "Ready!",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
                is SplashUiState.Error -> {
                    Text(
                        text = "Setup incomplete, but you can still use the app",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
