package com.ecosorter.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ecosorter.data.model.GarbageItem
import com.ecosorter.data.local.dao.GarbageItemDao
import java.time.LocalDateTime

/**
 * Room database for EcoSorter application
 */
@Database(
    entities = [GarbageItem::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class EcoSorterDatabase : RoomDatabase() {
    abstract fun garbageItemDao(): GarbageItemDao

    companion object {
        const val DATABASE_NAME = "ecosorter.db"
    }
}

/**
 * Type converters for Room database
 */
class Converters {
    @androidx.room.TypeConverter
    fun fromLocalDateTime(value: LocalDateTime?): Long? {
        return value?.toEpochSecond(java.time.ZoneOffset.UTC)
    }

    @androidx.room.TypeConverter
    fun toLocalDateTime(value: Long?): LocalDateTime? {
        return value?.let {
            LocalDateTime.ofEpochSecond(it, 0, java.time.ZoneOffset.UTC)
        }
    }

    @androidx.room.TypeConverter
    fun fromGarbageType(value: GarbageItem.GarbageType?): String? {
        return value?.name
    }

    @androidx.room.TypeConverter
    fun toGarbageType(value: String?): GarbageItem.GarbageType? {
        return value?.let { GarbageItem.GarbageType.valueOf(it) }
    }
}