package com.fooding.fooding.adapter.viewholder

import android.view.ViewGroup
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import com.fooding.fooding.R
import com.fooding.fooding.data.vo.Menu
import com.fooding.fooding.util.inflate
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder
import kotlinx.android.synthetic.main.item_main_food_header.view.*


class FoodHeaderViewHolder(private val parent: ViewGroup) :
    GroupViewHolder(parent.inflate(R.layout.item_main_food_header)) {
    private val mealName = itemView.tv_main_meal_type
    private val totalCalories = itemView.tv_main_total_calories
    private val foodExpandButton = itemView.img_main_meal_expand

    fun bind(menuItem: Menu) {
        mealName.text = menuItem.type
        totalCalories.text = menuItem.foodList.sumByDouble { it.cal }.toString()
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
