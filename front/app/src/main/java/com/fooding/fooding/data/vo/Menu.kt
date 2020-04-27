package com.fooding.fooding.data.vo

import android.os.Parcelable
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Menu(
    val type: String,
    var foodList: List<Food>
) : Parcelable, ExpandableGroup<Food>(type, foodList)