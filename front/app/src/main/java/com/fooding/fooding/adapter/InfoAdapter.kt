package com.fooding.fooding.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fooding.fooding.R
import com.fooding.fooding.data.vo.PostFood
import com.fooding.fooding.vo.FoodInfo2
import kotlinx.android.synthetic.main.item_info.view.*

class InfoAdapter(items:ArrayList<PostFood>) : RecyclerView.Adapter<InfoAdapter.MainViewHolder>() {
    var items = ArrayList<PostFood>()

    init {
        this.items = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int) = MainViewHolder(parent)


    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holer: MainViewHolder, position: Int) {
        items[position].let { item ->
            with(holer) {
                tvName.text = item.foodName
                tvCalorie.text = item.cal.toString()
                tvCarbs.text = item.carbs.toString()
                tvProteins.text = item.proteins.toString()
                tvFats.text = item.proteins.toString()
//                etQuantity.setText("1")
//                var result = etQuantity.getText().toString().toDouble() * item.cal.toString().toDouble()
//                tvTotal.text = result.toString()
            }
        }
    }

    inner class MainViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)) {
        var tvName = itemView.tv_name
        var tvCalorie = itemView.tv_calorie
        var tvCarbs = itemView.tv_carbs
        var tvProteins = itemView.tv_proteins
        var tvFats = itemView.tv_fats
    }
}