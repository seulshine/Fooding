package com.example.food.data.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize



@Parcelize
data class Food(
    val name: String,
    val calories: Double,
    val carbohydrate: Double,
    val protein: Double,
    val fat: Double,
    val standard: String
) : Parcelable