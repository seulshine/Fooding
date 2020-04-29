package com.fooding.fooding.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fooding.fooding.R
import com.fooding.fooding.adapter.FoodAdapter
import com.fooding.fooding.data.restapi.RestApi
import com.fooding.fooding.data.vo.Meal
import com.fooding.fooding.ui.manager.MainManager
import com.fooding.fooding.util.Dlog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.lang.Exception
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

    private var calories = 0.0
    private var carbohydrate = 0.0
    private var protein = 0.0
    private var fat = 0.0

    val REQUEST_IMAGE_CAPTURE = 1
    private var imageData: ByteArray? = null
    lateinit var currentPhotoPath : String

    companion object {
        fun newInstance() = MainFragment()
        private const val IMAGE_PICK_CODE = 999
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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
            setCalendar(-1)
        }
        img_main_date_next.setOnClickListener {
            setCalendar(1)
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

        fab_camera.setOnClickListener {
            startCapture()
        }

        fab_gallery.setOnClickListener {
            launchGallery()
        }

        setNutrition()

        img_chart_fill.setOnClickListener {
            chartAnimation(calories.toFloat(), 1000f)
        }

        MainManager().getMenuList("unsug@102")
    }

    private fun getStringDate(): String = SimpleDateFormat("yyyy-MM-dd").format(calendar.time)

    private fun setCalendar(dayDifference: Int) {
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

    }

    private fun calculateNutrition() {
        calories = 0.0
        carbohydrate = 0.0
        protein = 0.0
        fat = 0.0
        foodList.forEach { meal ->
            meal.foodList.forEach { food ->
                calories += food.cal.toInt()
                carbohydrate += food.carb
                protein += food.proteins
                fat += food.fat
            }
        }
    }

    private fun setNutrition() {
        calculateNutrition()
        tv_chart_current_calories.text = "$calories"
        tv_chart_crab_gram.text = "$carbohydrate g"
        tv_chart_pro_gram.text = "$protein g"
        tv_chart_fat_gram.text = "$fat g"
    }

    private fun chartAnimation(eaten: Float, target: Float) {
        val ratio: Float = if (eaten / target > 1) 1f else (eaten / target)
        val dp = requireContext().resources.displayMetrics.density

        img_chart_fill.layoutParams.width = (330 * ratio * dp).toInt()
        img_chart_fill.requestLayout()
    }

    fun startCapture(){
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try{
                    createImageFile()
                } catch(ex: IOException){
                    null
                }
                photoFile?.also{
                    val photoURI : Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.food.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    requireActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile() : File {
        val timeStamp : String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir : File? = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply{
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                requireActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        requireActivity().startActivityForResult(
            intent,
            IMAGE_PICK_CODE
        )
    }

    @Throws(IOException::class)
    private fun createImageData(uri: Uri) {
        val inputStream = requireActivity().contentResolver.openInputStream(uri)
        inputStream?.buffered()?.use {
            imageData = it.readBytes()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data?.data
            if (uri != null) {
                createImageData(uri)
            }
        }
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            val file = File(currentPhotoPath)
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(requireActivity().contentResolver, Uri.fromFile(file))
            }
            else{
                val decode = ImageDecoder.createSource(requireActivity().contentResolver,
                    Uri.fromFile(file))
                val bitmap = ImageDecoder.decodeBitmap(decode)
            }
        }
    }
}
