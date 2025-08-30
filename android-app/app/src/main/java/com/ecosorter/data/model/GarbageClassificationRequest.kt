package com.ecosorter.data.model

import kotlinx.serialization.Serializable

/**
 * Request model for garbage classification from URL
 */
@Serializable
data class GarbageClassificationRequest(
    val imageUrl: String,
    val model: String = "default",
    val confidenceThreshold: Float = 0.5f,
    val maxResults: Int = 5,
    val includeMetadata: Boolean = true
)

/**
 * Response model for garbage classification
 */
@Serializable
data class GarbageClassificationResponse(
    val success: Boolean,
    val predictions: List<Prediction>,
    val processingTime: Double,
    val modelVersion: String,
    val timestamp: Long
) {
    @Serializable
    data class Prediction(
        val garbageType: GarbageItem.GarbageType,
        val confidence: Float,
        val category: String,
        val subCategory: String? = null,
        val boundingBox: BoundingBox? = null,
        val metadata: Map<String, String> = emptyMap()
    )

    @Serializable
    data class BoundingBox(
        val x: Float,
        val y: Float,
        val width: Float,
        val height: Float
    )

    /**
     * Get the top prediction
     */
    fun getTopPrediction(): Prediction? {
        return predictions.maxByOrNull { it.confidence }
    }

    /**
     * Check if any prediction meets confidence threshold
     */
    fun hasConfidentPrediction(threshold: Float = 0.7f): Boolean {
        return predictions.any { it.confidence >= threshold }
    }

    /**
     * Get predictions filtered by confidence threshold
     */
    fun getConfidentPredictions(threshold: Float = 0.7f): List<Prediction> {
        return predictions.filter { it.confidence >= threshold }
    }
}