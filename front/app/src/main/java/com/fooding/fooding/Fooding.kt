package com.fooding.fooding

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.fooding.fooding.adapter.KakaoSDKAdapter
import com.fooding.fooding.util.Dlog
import com.kakao.auth.KakaoSDK

class Fooding : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this
        debug = isDebuggable(this)
        KakaoSDK.init(KakaoSDKAdapter())
    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getFoodContext() : Fooding {
        checkNotNull(instance) { "this application does not inherit com.example.food" }
        return instance!!
    }

    private fun isDebuggable(context: Context): Boolean {
        var debuggable = false;

        val pm: PackageManager = context.packageManager;
        try {
            val appinfo: ApplicationInfo = pm.getApplicationInfo(context.packageName, 0);
            debuggable = (0 != (appinfo.flags and ApplicationInfo.FLAG_DEBUGGABLE))
        } catch (e: PackageManager.NameNotFoundException) {
            Dlog.e(e.message.toString())
        }

        return debuggable;
    }

    companion object {
        var instance: Fooding? = null
        var debug: Boolean = false
    }
}