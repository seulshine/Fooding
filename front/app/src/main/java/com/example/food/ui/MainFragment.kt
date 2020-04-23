package com.example.food.ui

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.food.R
import com.example.food.adapter.FoodAdapter
import com.example.food.data.Food
import com.example.food.data.Meal
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.main_fragment.*
import java.text.SimpleDateFormat
import java.util.*

class MainFragment : Fragment() {
    private lateinit var selectedDate: String
    private val calendar by lazy { Calendar.getInstance() }
    private var isFabOpen = false
    private val fabOpen by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_open) }
    private val fabClose by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_close) }
    private val fabRotate by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_rotate) }
    private val fabRotateReversed by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_rotate_reversed) }
    private val foodList by lazy { ArrayList<Meal>() }
    private val foodAdapter by lazy { FoodAdapter(requireContext(), foodList) }

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        tv_main_datetime_picker.text = getStringDate()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.let {
                it[Calendar.YEAR] = year
                it[Calendar.MONTH] = month
                it[Calendar.DAY_OF_MONTH] = dayOfMonth
            }
            tv_main_datetime_picker.text = getStringDate()
        }

        tv_main_datetime_picker.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                dateSetListener,
                calendar[Calendar.YEAR],
                calendar[Calendar.MONTH],
                calendar[Calendar.DAY_OF_MONTH]
            ).show()
        }

        img_main_date_prev.setOnClickListener {
            changeCalendar(-1)
        }
        img_main_date_next.setOnClickListener {
            changeCalendar(1)
        }

        rec_main_food.apply {
            val linearLayout = LinearLayoutManager(context)
            layoutManager = linearLayout
        }
        addDummyData()
        if (rec_main_food.adapter == null) rec_main_food.adapter = foodAdapter

        fab_main.setOnClickListener {
            when (isFabOpen) {
                true -> {
                    fab_main.startAnimation(fabRotateReversed)
                    setFabStatus(fab_add, false, fabClose)
                    setFabStatus(fab_gallery, false, fabClose)
                    setFabStatus(fab_camera, false, fabClose)
                }
                else -> {
                    fab_main.startAnimation(fabRotate)
                    setFabStatus(fab_add, true, fabOpen)
                    setFabStatus(fab_gallery, true, fabOpen)
                    setFabStatus(fab_camera, true, fabOpen)
                }
            }
        }


    }

    private fun getStringDate(): String = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)

    private fun changeCalendar(dayDifference: Int) {
        calendar.let {
            it.add(Calendar.DAY_OF_MONTH, dayDifference)
        }
        tv_main_datetime_picker.text = getStringDate()
    }

    private fun setFabStatus(fab: FloatingActionButton, isOpen: Boolean, anim: Animation) {
        fab.startAnimation(anim)
        fab.isClickable = isOpen
        isFabOpen = isOpen
    }

    private fun addDummyData() {
        val meal = Meal("아침식사", listOf(
            Food("계란", 100.0, 100.0, 100.0, 100.0, "1개 당"),
            Food("식빵", 100.0, 100.0, 100.0, 100.0, "1조각 당")
        ))
        foodList.add(meal)
    }
}
