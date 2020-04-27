package com.fooding.fooding.data.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val name: String,
    val email: String,
    val age: Int,
    var targetCalorie: Double
) : Parcelable