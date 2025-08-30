package com.ecosorter.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.ecosorter.data.model.GarbageItem
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

/**
 * Data Access Object for GarbageItem operations
 */
@Dao
interface GarbageItemDao {

    /**
     * Insert a new garbage item
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(garbageItem: GarbageItem): Long

    /**
     * Insert multiple garbage items
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(garbageItems: List<GarbageItem>)

    /**
     * Update an existing garbage item
     */
    @Update
    suspend fun update(garbageItem: GarbageItem)

    /**
     * Delete a garbage item
     */
    @Delete
    suspend fun delete(garbageItem: GarbageItem)

    /**
     * Delete all garbage items
     */
    @Query("DELETE FROM garbage_items")
    suspend fun deleteAll()

    /**
     * Get all garbage items ordered by creation date (newest first)
     */
    @Query("SELECT * FROM garbage_items ORDER BY createdAt DESC")
    fun getAll(): Flow<List<GarbageItem>>

    /**
     * Get garbage item by ID
     */
    @Query("SELECT * FROM garbage_items WHERE id = :id")
    fun getById(id: Long): Flow<GarbageItem?>

    /**
     * Get garbage items by type
     */
    @Query("SELECT * FROM garbage_items WHERE garbageType = :type ORDER BY createdAt DESC")
    fun getByType(type: GarbageItem.GarbageType): Flow<List<GarbageItem>>

    /**
     * Get garbage items with high confidence (>= 0.7)
     */
    @Query("SELECT * FROM garbage_items WHERE confidence >= 0.7 ORDER BY createdAt DESC")
    fun getConfidentItems(): Flow<List<GarbageItem>>

    /**
     * Get favorite garbage items
     */
    @Query("SELECT * FROM garbage_items WHERE isFavorite = 1 ORDER BY createdAt DESC")
    fun getFavorites(): Flow<List<GarbageItem>>

    /**
     * Search garbage items by category or notes
     */
    @Query("""
        SELECT * FROM garbage_items 
        WHERE category LIKE '%' || :query || '%' 
           OR notes LIKE '%' || :query || '%'
        ORDER BY createdAt DESC
    """)
    fun search(query: String): Flow<List<GarbageItem>>

    /**
     * Get garbage items created within a date range
     */
    @Query("SELECT * FROM garbage_items WHERE createdAt BETWEEN :start AND :end ORDER BY createdAt DESC")
    fun getByDateRange(start: LocalDateTime, end: LocalDateTime): Flow<List<GarbageItem>>

    /**
     * Get statistics by garbage type
     */
    @Query("""
        SELECT garbageType, COUNT(*) as count, AVG(confidence) as avgConfidence
        FROM garbage_items 
        GROUP BY garbageType
        ORDER BY count DESC
    """)
    fun getStatistics(): Flow<Map<GarbageItem.GarbageType, Pair<Int, Float>>>

    /**
     * Get total count of garbage items
     */
    @Query("SELECT COUNT(*) FROM garbage_items")
    fun getTotalCount(): Flow<Int>

    /**
     * Get recent garbage items (last 7 days)
     */
    @Query("""
        SELECT * FROM garbage_items 
        WHERE createdAt >= datetime('now', '-7 days')
        ORDER BY createdAt DESC
    """)
    fun getRecentItems(): Flow<List<GarbageItem>>

    /**
     * Toggle favorite status
     */
    @Query("UPDATE garbage_items SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun setFavorite(id: Long, isFavorite: Boolean)

    /**
     * Update notes for a garbage item
     */
    @Query("UPDATE garbage_items SET notes = :notes WHERE id = :id")
    suspend fun updateNotes(id: Long, notes: String?)

    /**
     * Get items with low confidence for review
     */
    @Query("SELECT * FROM garbage_items WHERE confidence < 0.7 ORDER BY confidence ASC")
    fun getLowConfidenceItems(): Flow<List<GarbageItem>>
}