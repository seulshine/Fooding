package com.fooding.fooding.data.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Info(
    val FoodName: String,
    val FoodName_kor: String,
    val Criteria: String? = null,
    val Calories_cal: Double,
    val Carbs_g : Double,
    val Fat_g : Double,
    val Protein_g : Double
) : Parcelable