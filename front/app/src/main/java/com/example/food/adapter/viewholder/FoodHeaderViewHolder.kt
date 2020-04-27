package com.example.food.adapter.viewholder

import android.view.ViewGroup
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import com.example.food.R
import com.example.food.data.vo.Meal
import com.example.food.util.inflate
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.item_main_food_header.view.*


class FoodHeaderViewHolder(private val parent: ViewGroup) :
    GroupViewHolder(parent.inflate(R.layout.item_main_food_header)) {
    private val mealName = itemView.tv_main_meal_type
    private val totalCalories = itemView.tv_main_total_calories
    private val foodExpandButton = itemView.img_main_meal_expand

    fun bind(mealItem: Meal) {
        mealName.text = mealItem.type
        totalCalories.text = mealItem.foodList.sumByDouble { it.calories }.toString()
    }

    override fun expand() {
        animateExpand()
    }

    override fun collapse() {
        animateCollapse()
    }

    private fun animateExpand() {
        val rotate = RotateAnimation(
            360F,
            180F,
            RELATIVE_TO_SELF,
            0.5f,
            RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 300
        rotate.fillAfter = true
        foodExpandButton.animation = rotate
    }

    private fun animateCollapse() {
        val rotate = RotateAnimation(
            180F,
            360F,
            RELATIVE_TO_SELF,
            0.5f,
            RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 300
        rotate.fillAfter = true
        foodExpandButton.animation = rotate
    }

}