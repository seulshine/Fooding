package com.fooding.fooding.data.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val name: String,
    val user_email: String,
    val age: Int,
    val gender: String,
    var post_url: String? = null
) : Parcelable