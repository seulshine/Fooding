package com.example.food.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food.R
import com.example.food.vo.FoodInfo
import kotlinx.android.synthetic.main.item_info.view.*
import kotlinx.android.synthetic.main.item_nutrition.view.*

class NutritionAdapter: RecyclerView.Adapter<NutritionAdapter.MainViewHolder>() {
    var items: MutableList<FoodInfo> = mutableListOf(
        FoodInfo("칼로리", "490kcal"),
        FoodInfo("탄수화물", "20g"),
        FoodInfo("단백질", "21g"),
        FoodInfo("지방", "11g"),
        FoodInfo("당", "2g"),
        FoodInfo("나트륨", "8g"))

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        items[position].let { item ->
            with(holer) {
                tvTitle.text = item.title
                tvContent.text = item.quantity
            }
        }
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_nutrition, parent, false)) {
        val tvTitle = itemView.tv_title
        val tvContent = itemView.tv_content
    }
}