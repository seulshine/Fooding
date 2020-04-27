package com.example.food.adapter.viewholder

import android.view.ViewGroup
import com.example.food.R
import com.example.food.data.vo.Food
import com.example.food.util.inflate
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import kotlinx.android.synthetic.main.item_main_food_body.view.*

class FoodBodyViewHolder(private val parent: ViewGroup) : ChildViewHolder(parent.inflate(R.layout.item_main_food_body)) {
    private val foodName = itemView.tv_main_food_name
    private val foodCalories = itemView.tv_main_food_calories
    private val foodEdit = itemView.img_main_food_edit
    private val foodDelete = itemView.img_main_food_delete

    fun bind(foodItem: Food) {
        foodName.text = foodItem.name
        foodCalories.text = foodItem.calories.toString()
    }
}
