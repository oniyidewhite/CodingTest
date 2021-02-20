package com.oblessing.app.carlisting.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "cars")
data class CarCacheEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "fuel")
    val fuel: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "pictures")
    val pictures: List<String>
)