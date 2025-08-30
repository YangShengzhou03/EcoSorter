package com.ecosorter.data.remote

import com.ecosorter.data.model.GarbageClassificationRequest
import com.ecosorter.data.model.GarbageClassificationResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

/**
 * API interface for garbage classification service
 */
interface GarbageClassificationApi {

    /**
     * Classify garbage from image file
     */
    @Multipart
    @POST("classify")
    suspend fun classifyGarbageFromImage(
        @Part image: MultipartBody.Part,
        @Part("model") model: RequestBody = RequestBody.create(
            okhttp3.MediaType.parse("text/plain"), 
            "default"
        )
    ): GarbageClassificationResponse

    /**
     * Classify garbage from image URL
     */
    @POST("classify/url")
    suspend fun classifyGarbageFromUrl(
        @Body request: GarbageClassificationRequest
    ): GarbageClassificationResponse

    /**
     * Batch classify multiple images
     */
    @Multipart
    @POST("classify/batch")
    suspend fun batchClassifyGarbage(
        @Part images: List<MultipartBody.Part>,
        @Part("model") model: RequestBody = RequestBody.create(
            okhttp3.MediaType.parse("text/plain"), 
            "default"
        )
    ): List<GarbageClassificationResponse>

    /**
     * Get supported garbage categories
     */
    @POST("categories")
    suspend fun getSupportedCategories(): Map<String, List<String>>

    /**
     * Get model information
     */
    @POST("model/info")
    suspend fun getModelInfo(): Map<String, Any>
}