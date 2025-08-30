package com.ecosorter.ui.camera

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ecosorter.R
import com.ecosorter.util.Constants
import kotlinx.coroutines.launch

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.onPermissionGranted()
        } else {
            viewModel.onPermissionDenied()
        }
    }
    
    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.onImageSelected(it, context) }
    }
    
    LaunchedEffect(Unit) {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.camera))
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
        when {
            uiState.isPermissionDenied -> {
                PermissionDeniedScreen(
                    onRequestPermission = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            uiState.isAnalyzing -> {
                AnalyzingScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            }
            uiState.cameraError != null -> {
                ErrorScreen(
                    error = uiState.cameraError,
                    onRetry = {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            else -> {
                CameraPreview(
                    onTakePhoto = { viewModel.takePhoto() },
                    onChooseFromGallery = { galleryLauncher.launch("image/*") },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun CameraPreview(
    onTakePhoto: () -> Unit,
    onChooseFromGallery: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(3f / 4f),
            update = { previewView ->
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()
                    
                    val preview = Preview.Builder().build()
                    val imageCapture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()
                    
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    
                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )
                        
                        preview.setSurfaceProvider(previewView.surfaceProvider)
                        this.preview = preview
                        this.imageCapture = imageCapture
                    } catch (e: Exception) {
                        // Handle camera initialization error
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(
                onClick = onChooseFromGallery,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = stringResource(id = R.string.choose_from_gallery)
                )
            }
            
            FilledTonalButton(
                onClick = onTakePhoto,
                modifier = Modifier.size(72.dp)
            ) {
                Text(text = stringResource(id = R.string.take_photo))
            }
        }
    }
}