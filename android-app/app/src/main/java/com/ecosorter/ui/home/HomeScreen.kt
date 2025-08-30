package com.ecosorter.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecosorter.R
import com.ecosorter.data.model.GarbageItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToCamera: () -> Unit,
    onNavigateToHistory: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(48.dp))
                
                // Welcome Section
                Text(
                    text = stringResource(id = R.string.welcome_message),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Scan Button
                FilledTonalButton(
                    onClick = onNavigateToCamera,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = stringResource(id = R.string.scan_garbage)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(id = R.string.scan_garbage))
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Recent Scans Section
                Text(
                    text = stringResource(id = R.string.recent_scans),
                    style = MaterialTheme.typography.titleLarge
                )
                
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            if (uiState.recentItems.isEmpty()) {
                item {
                    Text(
                        text = stringResource(id = R.string.no_recent_scans),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            } else {
                items(uiState.recentItems.take(5)) { item ->
                    GarbageItemCard(item = item)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                
                // Navigation Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = stringResource(id = R.string.history)
                        )
                    }
                    
                    IconButton(onClick = { /* TODO: Navigate to settings */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(id = R.string.settings)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun GarbageItemCard(item: GarbageItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.category,
                style = MaterialTheme.typography.titleMedium
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = item.getTypeDisplayName(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "${(item.confidence * 100).toInt()}% 置信度",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}