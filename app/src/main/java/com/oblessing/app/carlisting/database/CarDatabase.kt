package com.oblessing.app.carlisting.database

import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.oblessing.app.core.utils.debug
import java.lang.Exception

@Database(entities = [CarCacheEntity::class], version = 1)
@TypeConverters(value = [ImageUrlConverter::class])
abstract class CarDatabase : RoomDatabase() {
    abstract fun carDao(): CarDao

    companion object {
        const val DATABASE_NAME = "db_car"
    }
}

class ImageUrlConverter {
    private val TAG = "ImageUrlConverter"
    private val gson = Gson()

    @TypeConverter
    fun storedStringToImageURls(value: String): List<String> {
        try {
            return gson.fromJson(value, object : TypeToken<List<String>>() {}.type)
        } catch (ex: Exception) {
            debug {
                Log.w(TAG, ex)
            }
        }

        return emptyList()
    }

    @TypeConverter
    fun imageUrlToStoredString(cl: List<String>): String {
        try {
            return gson.toJson(cl)
        } catch (ex: Exception) {
            debug {
                Log.w(TAG, ex)
            }
        }
        return "{}"
    }
}

