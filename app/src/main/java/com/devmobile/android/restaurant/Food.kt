package com.devmobile.android.restaurant

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmobile.android.restaurant.enums.FoodSection

@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true) var mId: Int,
    @ColumnInfo(name = "name") var mName: String,
    @ColumnInfo(name = "section") var mSection: FoodSection,
    @ColumnInfo(name = "imageId") var mImageId: Int,
    @ColumnInfo(name = "timeIcon") var mTimeIcon: Int,
    @ColumnInfo(name = "description") var mDescription: String?
)
