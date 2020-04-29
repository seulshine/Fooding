package com.fooding.fooding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fooding.fooding.R
import com.fooding.fooding.vo.FoodInfo2
import kotlinx.android.synthetic.main.item_info.view.*

class InfoAdapter: RecyclerView.Adapter<InfoAdapter.MainViewHolder>() {
    var items: MutableList<FoodInfo2> = mutableListOf(
        FoodInfo2("김치찌개", "128kcal", "128kcal"),
        FoodInfo2("계란", "77kcal",  "154kcal"),
        FoodInfo2("밥", "263kcal",  "263kcal")
    )

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        items[position].let { item ->
            with(holer) {
                tvName.text = item.name
                tvCalorie.text = item.calorie
                tvTotal.text = item.totalCalorie
            }
        }
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)) {
        var tvName = itemView.tv_name
        var tvCalorie = itemView.tv_calorie
        var tvTotal = itemView.tv_total_calorie
    }
}