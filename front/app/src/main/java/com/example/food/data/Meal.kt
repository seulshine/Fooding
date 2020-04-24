package com.example.food.data

import android.os.Parcelable
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Meal(
    val type: String,
    var foodList: List<Food>
) : Parcelable, ExpandableGroup<Food>(type, foodList)