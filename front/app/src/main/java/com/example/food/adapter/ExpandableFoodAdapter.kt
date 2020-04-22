package com.example.food.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food.R
import com.example.food.data.Food
import com.example.food.util.inflate

class ExpandableFoodAdapter(private val foodList: List<Food>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val header = 0
    private val body = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when(viewType) {
        0 -> FoodHeaderViewHolder(parent)
        else -> FoodBodyViewHolder(parent)
    }

    override fun getItemCount(): Int = foodList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    inner class FoodHeaderViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_main_food_header)) {
        fun bind(foodItem: Food) {

        }
    }

    inner class FoodBodyViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_main_food_body)) {

    }
}