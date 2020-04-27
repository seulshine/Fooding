package com.example.food.adapter

import android.content.Context
import android.view.ViewGroup
import com.example.food.adapter.viewholder.FoodBodyViewHolder
import com.example.food.adapter.viewholder.FoodHeaderViewHolder
import com.example.food.data.vo.Food
import com.example.food.data.vo.Meal
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class FoodAdapter(private val mContext: Context, private val mealList: List<Meal>) : ExpandableRecyclerViewAdapter<GroupViewHolder, ChildViewHolder>(mealList) {

    override fun onCreateGroupViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        return FoodHeaderViewHolder(parent)
    }

    override fun onCreateChildViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        return FoodBodyViewHolder(parent)
    }

    override fun onBindChildViewHolder(
        holder: ChildViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?,
        childIndex: Int
    ) {
        val food = group!!.items[childIndex]
        holder as FoodBodyViewHolder
        holder.bind(food as Food)
    }

    override fun onBindGroupViewHolder(
        holder: GroupViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        val meal = group
        holder as FoodHeaderViewHolder
        holder.bind(meal as Meal)
    }
}