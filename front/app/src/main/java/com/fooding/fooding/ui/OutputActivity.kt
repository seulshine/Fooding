package com.fooding.fooding.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.recyclerview.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.fooding.fooding.Adapter.NutritionAdapter
import com.fooding.fooding.R


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

        val spinner = findViewById(R.id.spn_type) as Spinner

        var foodType:String
        val items = resources.getStringArray(R.array.type_array)
        //android.R.layout.simple_spinner_dropdown_item 을 사용했습니다.
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)

        spinner.adapter = myAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when(position) {
                    0   ->  {
                        foodType="아침식사"
                    }
                    1   ->  {
                        foodType="점심식사"
                    }
                    2   ->  {
                        foodType="점심식사"
                    }
                    3   ->  {
                        foodType="저녁식사"
                    }
                    else -> {
                        foodType="아침식사"
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

    }



}
