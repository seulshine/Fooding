package com.fooding.fooding.data.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Info(
    val name: String,
    val criteria: String? = null,
    val calories_cal: Double,
    val carbs_g : Double,
    val fat_g : Double,
    val protein_g : Double
) : Parcelable