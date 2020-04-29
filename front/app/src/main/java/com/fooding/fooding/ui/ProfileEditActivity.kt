package com.fooding.fooding.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.fooding.fooding.R
import com.fooding.fooding.data.vo.User
import com.fooding.fooding.ui.manager.ProfileManager
import kotlinx.android.synthetic.main.activity_profile_edit.*

class ProfileEditActivity : AppCompatActivity() {
    private var email: String? = null
    private lateinit var nickname: String
    private lateinit var postUrl: String
    private var age: Int = 0
    private lateinit var gender: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        val intent = intent
        nickname = intent.getStringExtra("nickname")!!
        postUrl = intent.getStringExtra("profileImage")!!
        email = intent.getStringExtra("email")
        if (email == null) {
            tv_profile_email.visibility = View.INVISIBLE
            et_profile_email.visibility = View.VISIBLE
        } else {
            tv_profile_email.visibility = View.VISIBLE
            et_profile_email.visibility = View.INVISIBLE
            tv_profile_email.text = email
        }
        et_profile_nickname.setText(nickname)

        val genderItems = resources.getStringArray(R.array.gender_array)
        spinner_proflie_gender.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genderItems)

        btn_edit_submit.setOnClickListener {
            val data = User(nickname, email!!, et_profile_age.text.toString().toInt(), postUrl)
            ProfileManager().postUser(data)
        }
    }
}
