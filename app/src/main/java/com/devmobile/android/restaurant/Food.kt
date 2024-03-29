package com.devmobile.android.restaurant

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devmobile.android.restaurant.enums.FoodSection
import com.devmobile.android.restaurant.enums.TempoPreparo

@Entity(tableName = "foods")
data class Food(
    @PrimaryKey(autoGenerate = true) var mId: Long,
    @ColumnInfo(name = "name") var mName: String,
    @ColumnInfo(name = "section") var mSection: FoodSection,
    @ColumnInfo(name = "imageId") var mImageId: Long,
    @ColumnInfo(name = "timeIconId") var mTimeIconId: Long,
    @ColumnInfo(name = "timeToPrepare") var mTimeToPrepare: TempoPreparo,
    @ColumnInfo(name = "description") var mDescription: String?
)
