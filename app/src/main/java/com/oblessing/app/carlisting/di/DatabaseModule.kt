package com.oblessing.app.carlisting.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import com.oblessing.app.carlisting.database.CarDao
import com.oblessing.app.carlisting.database.CarDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): CarDatabase =
        Room.databaseBuilder(context, CarDatabase::class.java, CarDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    fun providesCarCacheDao(database: CarDatabase): CarDao = database.carDao()
}