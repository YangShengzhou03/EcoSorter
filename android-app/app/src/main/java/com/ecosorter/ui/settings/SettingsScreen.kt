package com.ecosorter.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecosorter.R

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.settings_title))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item {
                SettingsSection(title = stringResource(id = R.string.general))
            }
            
            item {
                SettingsItem(
                    icon = Icons.Default.Language,
                    title = stringResource(id = R.string.language),
                    subtitle = uiState.language,
                    onClick = { /* TODO: Language selection */ }
                )
            }
            
            item {
                SettingsItem(
                    icon = Icons.Default.Star,
                    title = stringResource(id = R.string.theme),
                    subtitle = uiState.theme,
                    onClick = { /* TODO: Theme selection */ }
                )
            }
            
            item {
                SettingsItem(
                    icon = Icons.Default.Notifications,
                    title = stringResource(id = R.string.notifications),
                    subtitle = if (uiState.notificationsEnabled) "启用" else "禁用",
                    onClick = { viewModel.toggleNotifications() }
                )
            }
            
            item {
                SettingsSection(title = stringResource(id = R.string.about))
            }
            
            item {
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = stringResource(id = R.string.version_info),
                    subtitle = "v${uiState.versionName} (${uiState.versionCode})",
                    onClick = {}
                )
            }
            
            item {
                SettingsItem(
                    icon = Icons.Default.PrivacyTip,
                    title = stringResource(id = R.string.privacy_policy),
                    subtitle = null,
                    onClick = { /* TODO: Open privacy policy */ }
                )
            }
            
            item {
                SettingsItem(
                    icon = Icons.Default.PrivacyTip,
                    title = stringResource(id = R.string.terms_of_service),
                    subtitle = null,
                    onClick = { /* TODO: Open terms of service */ }
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SettingsSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(
            start = 24.dp,
            top = 24.dp,
            bottom = 8.dp
        ),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun SettingsItem(
    icon: androidx.compose.material.icons.Icons,
    title: String,
    subtitle: String?,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge
                )
                
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            if (subtitle == null) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}