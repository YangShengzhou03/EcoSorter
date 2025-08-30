package com.ecosorter.di

import android.content.Context
import androidx.room.Room
import com.ecosorter.data.local.EcoSorterDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module for database dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): EcoSorterDatabase {
        return Room.databaseBuilder(
            context,
            EcoSorterDatabase::class.java,
            EcoSorterDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
         .build()
    }

    @Provides
    fun provideGarbageItemDao(database: EcoSorterDatabase) = database.garbageItemDao()
}