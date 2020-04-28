package com.fooding.fooding.data.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Menu(
    val id: Int,
    val user: String,
    val foods: List<Food>,
    val time: String,
    val created_at: String? = null
) : Parcelable