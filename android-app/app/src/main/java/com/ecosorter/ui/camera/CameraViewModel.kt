package com.ecosorter.ui.camera

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ecosorter.data.model.GarbageItem
import com.ecosorter.data.remote.GarbageClassificationApi
import com.ecosorter.data.local.dao.GarbageItemDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val garbageClassificationApi: GarbageClassificationApi,
    private val garbageItemDao: GarbageItemDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState: StateFlow<CameraUiState> = _uiState.asStateFlow()

    fun onPermissionGranted() {
        _uiState.update { it.copy(isPermissionDenied = false) }
    }

    fun onPermissionDenied() {
        _uiState.update { it.copy(isPermissionDenied = true) }
    }

    fun takePhoto() {
        // Implementation for taking photo with camera
        _uiState.update { it.copy(isAnalyzing = true) }
        
        viewModelScope.launch {
            try {
                // Simulate image processing
                kotlinx.coroutines.delay(2000)
                
                // Create a sample result for demonstration
                val sampleItem = GarbageItem.createSample()
                garbageItemDao.insert(sampleItem)
                
                _uiState.update {
                    it.copy(
                        isAnalyzing = false,
                        lastResult = sampleItem
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isAnalyzing = false,
                        cameraError = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }

    fun onImageSelected(uri: Uri, context: Context) {
        _uiState.update { it.copy(isAnalyzing = true) }
        
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = createTempFileFromUri(uri, context)
                
                val requestFile = RequestBody.create(
                    MediaType.parse("image/jpeg"),
                    file
                )
                
                val imagePart = MultipartBody.Part.createFormData(
                    "image",
                    file.name,
                    requestFile
                )
                
                val response = garbageClassificationApi.classifyGarbageFromImage(imagePart)
                
                // Save to database
                val garbageItem = GarbageItem(
                    garbageType = response.garbageType,
                    confidence = response.confidence,
                    category = response.category,
                    subCategory = response.subCategory,
                    imageUri = uri.toString()
                )
                
                garbageItemDao.insert(garbageItem)
                
                _uiState.update {
                    it.copy(
                        isAnalyzing = false,
                        lastResult = garbageItem
                    )
                }
                
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isAnalyzing = false,
                        cameraError = e.message ?: "Image processing failed"
                    )
                }
            }
        }
    }

    private fun createTempFileFromUri(uri: Uri, context: Context): File {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("ecosorter", ".jpg", context.cacheDir)
        
        inputStream?.use { input ->
            tempFile.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        
        return tempFile
    }

    fun clearError() {
        _uiState.update { it.copy(cameraError = null) }
    }
}

data class CameraUiState(
    val isPermissionDenied: Boolean = false,
    val isAnalyzing: Boolean = false,
    val lastResult: GarbageItem? = null,
    val cameraError: String? = null
)