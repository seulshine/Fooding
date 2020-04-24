package com.example.food.data.vo

import android.os.Parcelable
import com.example.food.data.vo.Food
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Meal(
    val type: String,
    var foodList: List<Food>
) : Parcelable, ExpandableGroup<Food>(type, foodList)