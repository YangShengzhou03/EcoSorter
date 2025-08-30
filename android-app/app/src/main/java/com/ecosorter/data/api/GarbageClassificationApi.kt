package com.ecosorter.data.api

import com.ecosorter.data.model.GarbageClassificationRequest
import com.ecosorter.data.model.GarbageClassificationResponse
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * API interface for garbage classification service
 */
interface GarbageClassificationApi {
    
    /**
     * Classify garbage from image URL
     */
    @POST("/classify")
    suspend fun classifyFromUrl(
        @Body request: GarbageClassificationRequest
    ): GarbageClassificationResponse
    
    /**
     * Classify garbage from base64 encoded image
     */
    @POST("/classify/base64")
    suspend fun classifyFromBase64(
        @Body request: Map<String, String>
    ): GarbageClassificationResponse
    
    /**
     * Get API status and version
     */
    @POST("/status")
    suspend fun getStatus(): Map<String, Any>
}