package com.ecosorter.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

/**
 * Data class representing a garbage item with classification results
 */
@Entity(tableName = "garbage_items")
@Serializable
data class GarbageItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    
    // Classification results
    val garbageType: GarbageType,
    val confidence: Float,
    val category: String,
    val subCategory: String? = null,
    
    // Image information
    val imagePath: String? = null,
    val imageUri: String? = null,
    val thumbnailPath: String? = null,
    
    // Location data (optional)
    val latitude: Double? = null,
    val longitude: Double? = null,
    
    // Timestamps
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    
    // Additional metadata
    val notes: String? = null,
    val isFavorite: Boolean = false
) {
    /**
     * Enum representing different types of garbage
     */
    @Serializable
    enum class GarbageType {
        RECYCLABLE,    // 可回收物
        HAZARDOUS,     // 有害垃圾
        WET,           // 湿垃圾
        DRY,           // 干垃圾
        UNKNOWN        // 未知类型
    }

    /**
     * Get display name for the garbage type
     */
    fun getTypeDisplayName(): String {
        return when (garbageType) {
            GarbageType.RECYCLABLE -> "可回收物"
            GarbageType.HAZARDOUS -> "有害垃圾"
            GarbageType.WET -> "湿垃圾"
            GarbageType.DRY -> "干垃圾"
            GarbageType.UNKNOWN -> "未知类型"
        }
    }

    /**
     * Get color resource for the garbage type
     */
    fun getTypeColor(): Int {
        return when (garbageType) {
            GarbageType.RECYCLABLE -> android.R.color.holo_green_light
            GarbageType.HAZARDOUS -> android.R.color.holo_red_light
            GarbageType.WET -> android.R.color.holo_orange_light
            GarbageType.DRY -> android.R.color.darker_gray
            GarbageType.UNKNOWN -> android.R.color.darker_gray
        }
    }

    /**
     * Get disposal instructions based on garbage type
     */
    fun getDisposalInstructions(): String {
        return when (garbageType) {
            GarbageType.RECYCLABLE -> "请清洗干净后放入可回收物垃圾桶"
            GarbageType.HAZARDOUS -> "请放入有害垃圾专用回收箱"
            GarbageType.WET -> "请去除包装后放入湿垃圾垃圾桶"
            GarbageType.DRY -> "请放入干垃圾垃圾桶"
            GarbageType.UNKNOWN -> "无法识别，请咨询当地垃圾分类指南"
        }
    }

    /**
     * Check if the classification confidence is high enough
     */
    fun isConfident(): Boolean = confidence >= 0.7f

    companion object {
        /**
         * Create a sample garbage item for testing
         */
        fun createSample(): GarbageItem {
            return GarbageItem(
                garbageType = GarbageType.RECYCLABLE,
                confidence = 0.85f,
                category = "塑料瓶",
                subCategory = "PET塑料"
            )
        }
    }
}