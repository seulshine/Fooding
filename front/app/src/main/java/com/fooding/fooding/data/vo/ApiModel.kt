package com.fooding.fooding.data.vo

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.File

@Parcelize
data class PostMenu(
    val user_email: String,
    val time: String,
    val eat_time: String? = null,
    val foods: List<PostFood>
) : Parcelable

@Parcelize
data class PostFood(
    val foodName: String,
    val cal: Double,
    val carbs: Double,
    val fats: Double,
    val proteins: Double
) : Parcelable

@Parcelize
data class ApiUser(
    val name: String,
    val email: String,
    val post_url: String,
    val menus: List<Menu>
) : Parcelable

@Parcelize
data class GetDailyMenu(
    val user_email: String,
    val eat_date: String
) : Parcelable

@Parcelize
data class PostImage(
    val image: File
) : Parcelable

@Parcelize
data class GetImageInfo(
    val result: List<Info>
) : Parcelable