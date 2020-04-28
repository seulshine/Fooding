package com.fooding.fooding.data.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Food(
    val id: Long,
    val name: String,
    val info: Info,
    val cal: Double,
    val carb: Double,
    val proteins: Double,
    val fat: Double
) : Parcelable