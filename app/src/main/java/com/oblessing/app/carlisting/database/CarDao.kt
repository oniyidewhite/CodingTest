package com.oblessing.app.carlisting.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CarDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: CarCacheEntity)

    @Query("SELECT * FROM cars")
    suspend fun get(): List<CarCacheEntity>
}