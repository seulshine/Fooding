package com.fooding.fooding.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fooding.fooding.MainActivity
import com.fooding.fooding.R
import com.fooding.fooding.adapter.InfoAdapter
import com.fooding.fooding.data.restapi.RestApi
import com.fooding.fooding.data.vo.PostFood
import com.fooding.fooding.data.vo.PostMenu
import com.fooding.fooding.util.Dlog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class Output2Activity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var uploadButton: Button
    lateinit var foodType:String
    var simpleDate: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_output2)

        viewManager = LinearLayoutManager(this)
        var params = ArrayList<PostFood>()

        params.add(PostFood("김치찌개", 360.0, 20.0, 20.0, 20.0))
        params.add(PostFood("밥", 150.0, 10.0, 10.0, 10.0))
        viewAdapter = InfoAdapter(params) // 이후 paraemter로 data 넘겨줌

        recyclerView = findViewById<RecyclerView>(R.id.rv_info_list).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }

        uploadButton = findViewById(R.id.btn_upload)
        uploadButton.setOnClickListener{
            var foods = ArrayList<PostFood>()
            var datestr = simpleDate.format(Calendar.getInstance().getTime());
            foods.add(PostFood("치킨", 360.0, 20.0, 20.0, 20.0))
            var temp = PostMenu("unsug@102", foodType, datestr, foods)

            requestMenu(temp)

            val intent = Intent(getApplicationContext(), MainActivity::class.java)
            startActivity(intent)
        }

        val spinner = findViewById(R.id.spn_type) as Spinner


        val items = resources.getStringArray(R.array.type_array)
        //android.R.layout.simple_spinner_dropdown_item 을 사용했습니다.
        val myAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, items)

        spinner.adapter = myAdapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                //아이템이 클릭 되면 맨 위부터 position 0번부터 순서대로 동작하게 됩니다.
                when(position) {
                    0   ->  {
                        foodType="breakfast"
                    }
                    1   ->  {
                        foodType="lunch"
                    }
                    2   ->  {
                        foodType="dinner"
                    }
                    3   ->  {
                        foodType="snack"
                    }
                    else -> {
                        foodType="breakfast"
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }


    }

    private fun requestMenu(requestBody: PostMenu) {
        Dlog.d(requestBody.toString())
        runBlocking {
            CoroutineScope(coroutineContext).launch {
                try {
                    Dlog.d("hey")
                    val response = RestApi().postMenu(requestBody)
                    Dlog.d("why")
                    if (response.get("type")!!.equals("success")) {

                    }

                } catch (e: Throwable) {

                }
            }
        }


    }
}