package com.fooding.fooding.adapter

import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.fooding.fooding.R
import com.fooding.fooding.data.vo.Info
import com.fooding.fooding.util.inflate
import kotlinx.android.synthetic.main.item_main_add.view.*

class AddAdapter(val addList: ArrayList<Pair<Info, Int>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AddViewHolder(parent)
    }

    override fun getItemCount(): Int = addList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val addItem = addList[position]
        holder as AddViewHolder
        holder.bind(addItem, position)
    }

    inner class AddViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_main_add)) {
        val foodName = itemView.tv_main_add_name
        val calories = itemView.tv_main_add_calories
        val count = itemView.et_main_add_count
        val totalCalories = itemView.tv_main_add_total_calories
        val deleteButton = itemView.tv_main_add_delete

        fun bind(addItem: Pair<Info, Int>, position: Int) {
            val food = addItem.first
            val quantity = addItem.second
            foodName.text = food.FoodName
            calories.text = food.Calories_cal.toString()
            totalCalories.text = (food.Calories_cal * count.text.toString().toDouble()).toString()
            count.addTextChangedListener {
                if (it == null || it.isEmpty()) {
                    totalCalories.text = food.Calories_cal.toString()
                    addList[position] = Pair(food, 1)
                } else {
                    totalCalories.text = (food.Calories_cal * count.text.toString().toDouble()).toString()
                    addList[position] = Pair(food, count.text.toString().toInt())
                }
            }
            deleteButton.setOnClickListener {
                addList.removeAt(position)
                notifyItemRangeRemoved(position, 1)
            }
        }
    }
}