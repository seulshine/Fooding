package com.fooding.fooding.ui

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.fooding.fooding.R
import com.fooding.fooding.adapter.FoodAdapter
import com.fooding.fooding.data.restapi.RestApi
import com.fooding.fooding.data.vo.GetDailyMenu
import com.fooding.fooding.data.vo.Meal
import com.fooding.fooding.ui.dialog.FoodAddDialog
import com.fooding.fooding.ui.manager.MainManager
import com.fooding.fooding.util.Dlog
import com.fooding.fooding.util.RealPathUtil
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import kotlinx.android.synthetic.main.fragment_main.*
import kotlinx.coroutines.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext

class MainFragment : Fragment(), CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default+ job
    private val mainManager = MainManager()

    private lateinit var selectedDate: String
    private val calendar by lazy { Calendar.getInstance() }

    private var isFabOpen = false
    private val fabOpen by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_open) }
    private val fabClose by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_close) }
    private val fabRotate by lazy { AnimationUtils.loadAnimation(context, R.anim.fab_rotate) }
    private val fabRotateReversed by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.fab_rotate_reversed
        )
    }

    private val foodList by lazy { ArrayList<Meal>() }
    private val foodAdapter by lazy { FoodAdapter(requireContext(), foodList) }

    private var calories = 0.0
    private var carbohydrate = 0.0
    private var protein = 0.0
    private var fat = 0.0

    val REQUEST_IMAGE_CAPTURE = 1
    private var imageData: ByteArray? = null
    lateinit var currentPhotoPath: String

    private lateinit var nickname: String
    private lateinit var email: String
    private var gender: String? = null
    private var age: Int = 0

    private val dailyCaloriesMap = hashMapOf("여성" to intArrayOf(1000, 1400, 1500, 1800, 2000, 2000, 2100, 1900, 1800, 1600, 1600),
        "남성" to intArrayOf(1000, 1400, 1700, 2100, 2500, 2700, 2600, 2400, 2200, 2000, 2000))

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

        settingPermission()
        (activity as AppCompatActivity).setSupportActionBar(toolbar_main)
        setHasOptionsMenu(true)

        requireContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE)?.let {
            email = it.getString("email", null)!!
            nickname = it.getString("nickname", null)!!
            age = it.getInt("age", 0)
            gender = it.getString("gender", null)
        }

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

        fab_add.setOnClickListener {
            val dialog = FoodAddDialog(requireContext())
            dialog.start()
        }

        setNutrition()

        img_chart_fill.setOnClickListener {
            chartAnimation(calories.toFloat(), 1000f)
        }

        mainManager.getUserInfo("unsug@102")
        getDailyMenu(email, "2020-04-30")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_edit_profile -> {
                requireActivity().startActivity(Intent(context, ProfileEditActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDailyMenu(email: String, date: String) {
        CoroutineScope(coroutineContext).launch {
            try {
                val dailyMenu = mainManager.getDailyMenu(GetDailyMenu(email, date))
                println(dailyMenu)
            } catch (e: Exception) {
                Dlog.e(e.message.toString())
            }
        }
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

    private fun calcRecommendedCalories(): Int = when(age) {
        0 -> 0
        in 1..2 -> dailyCaloriesMap[gender]!![0]
        in 3..5 -> dailyCaloriesMap[gender]!![1]
        in 6..8 -> dailyCaloriesMap[gender]!![2]
        in 9..11 -> dailyCaloriesMap[gender]!![3]
        in 12..14 -> dailyCaloriesMap[gender]!![4]
        in 15..18 -> dailyCaloriesMap[gender]!![5]
        in 19..29 -> dailyCaloriesMap[gender]!![6]
        in 30..49 -> dailyCaloriesMap[gender]!![7]
        in 50..64 -> dailyCaloriesMap[gender]!![8]
        in 65..74 -> dailyCaloriesMap[gender]!![9]
        else -> dailyCaloriesMap[gender]!![10]
    }

    private fun setNutrition() {
        calculateNutrition()
        tv_chart_recommended_calories.text = "${calcRecommendedCalories()}"
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

    fun startCapture() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        "com.example.food.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(
                        takePictureIntent,
                        REQUEST_IMAGE_CAPTURE
                    )
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? =
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_",
            ".jpg",
            storageDir
        ).apply {
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
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(
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
            val uri: Uri = data?.data!!
            val file = File(RealPathUtil.getRealPath(requireContext(), uri))
            postImage(file)
            if (uri != null) {
                createImageData(uri)
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val file = File(currentPhotoPath)
            postImage(file)
            if (Build.VERSION.SDK_INT < 28) {
                val bitmap = MediaStore.Images.Media
                    .getBitmap(requireActivity().contentResolver, Uri.fromFile(file))
            } else {
                val decode = ImageDecoder.createSource(
                    requireActivity().contentResolver,
                    Uri.fromFile(file)
                )
                val bitmap = ImageDecoder.decodeBitmap(decode)
            }
        }
    }

    fun postImage(file: File) {
        runBlocking {
            CoroutineScope(coroutineContext).launch {
                try {
                    val fileReqBody = RequestBody.create(MediaType.parse("image/*"), file)
                    val imageFile = MultipartBody.Part.createFormData("image", file.name, fileReqBody)
                    val description = RequestBody.create(MediaType.parse("text/plain"), "image-type")
                    val result = mainManager.postImage(imageFile, description)
                    val intent = Intent(requireActivity(), Output2Activity::class.java)
                    intent.putExtra("foodInfo", result)
                    startActivity(intent)
                } catch (e: Exception) {
                    Dlog.e(e.message.toString())
                }
            }
        }
    }

    fun settingPermission() {
        var permis = object : PermissionListener {
            //            어떠한 형식을 상속받는 익명 클래스의 객체를 생성하기 위해 다음과 같이 작성
            override fun onPermissionGranted() {
                Toast.makeText(requireContext(), "권한 허가", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(requireContext(), "권한 거부", Toast.LENGTH_SHORT)
                    .show()
                requireActivity().finishAffinity() // 권한 거부시 앱 종료
            }
        }

        TedPermission.with(requireContext())
            .setPermissionListener(permis)
            .setRationaleMessage("카메라 사진 권한 필요")
            .setDeniedMessage("카메라 권한 요청 거부")
            .setPermissions(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                //                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
            )
            .check()
    }
}
