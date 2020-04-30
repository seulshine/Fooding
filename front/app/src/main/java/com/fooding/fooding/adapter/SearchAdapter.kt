package com.fooding.fooding.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.fooding.fooding.R
import com.fooding.fooding.data.vo.Info
import com.fooding.fooding.util.inflate
import kotlinx.android.synthetic.main.dialog_add_food.view.*
import kotlinx.android.synthetic.main.item_main_search.view.*

class SearchAdapter(private val mContext: Context, private val searchList: List<Info>, val addList: ArrayList<Pair<Info, Int>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return SearchViewHolder(parent)
    }

    override fun getItemCount(): Int = searchList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val info = searchList[position]
        holder as SearchViewHolder
        holder.bind(info)
    }

    inner class SearchViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(parent.inflate(R.layout.item_main_search)) {
        val foodName = itemView.tv_main_search_name
        val calories = itemView.tv_main_search_calories
        val addButton = itemView.tv_main_search_add

        fun bind(infoItem: Info) {
            foodName.text = infoItem.FoodName
            calories.text = infoItem.Calories_cal.toString()
            addButton.setOnClickListener {
                addList.add(Pair(infoItem, 1))
                Toast.makeText(mContext, "${infoItem.FoodName}이/가 추가되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}