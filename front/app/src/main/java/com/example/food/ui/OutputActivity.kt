package com.example.food.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.Adapter.NutritionAdapter
import com.example.food.R


class OutputActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_output)

        viewManager = LinearLayoutManager(this)
        viewAdapter = NutritionAdapter() // 이후 paraemter로 data 넘겨줌

        recyclerView = findViewById<RecyclerView>(R.id.rv_nutrition_list).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

    }



}
