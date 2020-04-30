package com.fooding.fooding.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.fooding.fooding.MainActivity
import com.fooding.fooding.R
import com.fooding.fooding.data.vo.User
import com.fooding.fooding.ui.manager.ProfileManager
import com.fooding.fooding.util.Dlog
import kotlinx.android.synthetic.main.activity_profile_edit.*
import java.lang.Exception

class ProfileEditActivity : AppCompatActivity() {
    private var email: String? = null
    private lateinit var nickname: String
    private var age: Int = 0
    private var gender: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        val pref = applicationContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        email = pref.getString("email", null)
        nickname = pref.getString("nickname", null)!!
        age = pref.getInt("age", 0)
        gender = pref.getString("gender", null)
        if (email == null) {
            tv_profile_email.visibility = View.INVISIBLE
            et_profile_email.visibility = View.VISIBLE
        } else {
            tv_profile_email.visibility = View.VISIBLE
            et_profile_email.visibility = View.INVISIBLE
            tv_profile_email.text = email
        }
        et_profile_nickname.setText(nickname)
        if (age > 0) et_profile_age.setText(age.toString())

        val genderItems = resources.getStringArray(R.array.gender_array)
        spinner_proflie_gender.apply {
            val spinnerAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, genderItems)
            adapter = spinnerAdapter
            if (gender != null) {
                this.setSelection(spinnerAdapter.getPosition(gender))
            }
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    gender = spinnerAdapter.getItem(position)
                }
            }
        }

        btn_edit_submit.setOnClickListener {
            val data = User(et_profile_nickname.text.toString(), email!!, et_profile_age.text.toString().toInt(), gender!!)
            try {
                ProfileManager().postUser(data)
                val pref = applicationContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                pref.putString("email", email!!)
                pref.putString("nickname", data.name)
                pref.putInt("age", data.age)
                pref.putString("gender", gender)
                pref.apply()
                val intent = Intent(this@ProfileEditActivity, MainActivity::class.java)
                startActivity(intent)
            } catch (e: Exception) {
                Dlog.e(e.message.toString())
            }
        }

        btn_login_link.setOnClickListener {
            val pref = applicationContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
            pref.clear()
            pref.apply()
            startActivity(Intent(this@ProfileEditActivity, LoginActivity::class.java))
        }
    }
}
