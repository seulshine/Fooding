package com.fooding.fooding.ui

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Base64.NO_WRAP
import com.fooding.fooding.MainActivity
import com.fooding.fooding.R
import com.fooding.fooding.ui.manager.MainManager
import com.fooding.fooding.util.Dlog
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import com.kakao.util.helper.Utility.getPackageInfo
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {
    private val sessionCallback = object : ISessionCallback {
        override fun onSessionOpenFailed(exception: KakaoException?) {
            Dlog.e(exception?.message.toString())
        }

        override fun onSessionOpened() {
            UserManagement.getInstance().me(object : MeV2ResponseCallback() {
                override fun onSuccess(result: MeV2Response?) {
                    checkNotNull(result) { "session response null" }
                    val pref = applicationContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit()
                    pref.putString("email", result.kakaoAccount.email)
                    pref.putString("nickname", result.kakaoAccount.profile.nickname)
                    pref.putInt("age", 0)
                    pref.putString("gender", null)
                    pref.apply()
                    val intent = Intent(applicationContext, ProfileEditActivity::class.java)
                    startActivity(intent)
                }

                override fun onSessionClosed(errorResult: ErrorResult?) {
                    Dlog.e(errorResult?.errorMessage.toString())
                }

                override fun onFailure(errorResult: ErrorResult?) {
                    Dlog.d(errorResult?.errorMessage.toString())
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val pref = this.getSharedPreferences("PREFS", Context.MODE_PRIVATE)
        if (pref.getString("email", null) != null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Session.getCurrentSession().addCallback(sessionCallback)
//            Session.getCurrentSession().checkAndImplicitOpen()
        }
//        Session.getCurrentSession().addCallback(sessionCallback)
//        Session.getCurrentSession().checkAndImplicitOpen()
    }

    override fun onDestroy() {
        super.onDestroy()

        Session.getCurrentSession().removeCallback(sessionCallback)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            Dlog.d("Session get current session")
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun getHashKey(context: Context): String? {
        try {
            if (Build.VERSION.SDK_INT >= 28) {
                val packageInfo = getPackageInfo(context, PackageManager.GET_SIGNING_CERTIFICATES)
                val signatures = packageInfo.signingInfo.apkContentsSigners
                val md = MessageDigest.getInstance("SHA")
                for (signature in signatures) {
                    md.update(signature.toByteArray())
                    return String(Base64.encode(md.digest(), NO_WRAP))
                }
            } else {
                val packageInfo =
                    getPackageInfo(context, PackageManager.GET_SIGNATURES) ?: return null

                for (signature in packageInfo.signatures) {
                    try {
                        val md = MessageDigest.getInstance("SHA")
                        md.update(signature.toByteArray())
                        return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
                    } catch (e: NoSuchAlgorithmException) {
                        // ERROR LOG
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return null
    }
}
