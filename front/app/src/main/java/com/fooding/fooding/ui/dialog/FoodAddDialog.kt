package com.fooding.fooding.ui.dialog

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.fooding.fooding.R
import com.fooding.fooding.adapter.AddAdapter
import com.fooding.fooding.adapter.SearchAdapter
import com.fooding.fooding.data.restapi.RestApi
import com.fooding.fooding.data.vo.Info
import com.fooding.fooding.data.vo.PostFood
import com.fooding.fooding.data.vo.PostMenu
import com.fooding.fooding.ui.manager.DialogManager
import com.fooding.fooding.util.Dlog
import com.kakao.usermgmt.StringSet.gender
import kotlinx.android.synthetic.main.dialog_add_food.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class FoodAddDialog(private val mContext: Context) : CoroutineScope {
    private val dialog = Dialog(mContext)
    private val searchList = arrayListOf<Info>()
    private val addList = arrayListOf<Pair<Info, Int>>()
    private val searchAdapter by lazy { SearchAdapter(mContext, searchList, addList) }
    private val addAdapter by lazy { AddAdapter(addList) }
    private var type = "아침식사"
    private val typeMap =
        hashMapOf("아침식사" to "breakfast", "점심식사" to "lunch", "저녁식사" to "dinner", "간식" to "snack")
    private val dialogManager by lazy { DialogManager() }

    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun start() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_add_food)

//        searchList.add(Info("밥", null, 100.0, 100.0, 100.0, 100.0))
//        searchList.add(Info("계란", null, 100.0, 100.0, 100.0, 100.0))
//        addList.add(Pair(Info("rice", null, 100.0, 100.0, 100.0, 100.0), 1))
//        addList.add(Pair(Info("sushi", null, 100.0, 100.0, 100.0, 100.0), 1))


        val typeItems = mContext.resources.getStringArray(R.array.type_array)
        dialog.spinner_search_type.apply {
            val spinnerAdapter =
                ArrayAdapter(mContext, android.R.layout.simple_spinner_dropdown_item, typeItems)
            adapter = spinnerAdapter
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    type = spinnerAdapter.getItem(position)!!
                }
            }
        }

        dialog.rec_main_search_result.apply {
            setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
        }
        if (dialog.rec_main_search_result.adapter == null) dialog.rec_main_search_result.adapter =
            searchAdapter

        dialog.rec_main_food_add.apply {
            setHasFixedSize(true)
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
        }
        if (dialog.rec_main_food_add.adapter == null) dialog.rec_main_food_add.adapter = addAdapter

        dialog.et_search_text.also { editText ->
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (editText.text.isEmpty()) {
                        dialog.rec_main_search_result.visibility = View.INVISIBLE
                        dialog.rec_main_food_add.visibility = View.VISIBLE
                        searchList.clear()
                        dialog.rec_main_search_result.adapter?.notifyDataSetChanged()
                        dialog.rec_main_food_add.adapter?.notifyDataSetChanged()
                    } else {
                        dialog.rec_main_search_result.visibility = View.VISIBLE
                        dialog.rec_main_food_add.visibility = View.INVISIBLE
                    }
                }
            })
        }


        dialog.tv_main_search_finish.setOnClickListener {
            CoroutineScope(coroutineContext).launch {
                try {
                    val foodList = addList.map {
                        PostFood(
                            it.first.FoodName,
                            it.first.Calories_cal * it.second,
                            it.first.Carbs_g * it.second,
                            it.first.Fat_g * it.second,
                            it.first.Protein_g * it.second
                        )
                    }
                    val pref = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)

                    val postMenu = PostMenu(
                        pref.getString("email", null)!!,
                        typeMap[type]!!,
                        null,
                        foodList
                    )
                    val response = dialogManager.postMenu(postMenu)
                    println(response)
                } catch (e: Exception) {
                    Dlog.e(e.message.toString())
                } finally {
                    dialog.dismiss()
                }
            }
        }

        dialog.tv_main_search_cancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }
}