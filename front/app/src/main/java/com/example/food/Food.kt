package com.example.food

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.example.food.util.Dlog

class Food : Application() {
    override fun onCreate() {
        super.onCreate()

        instance = this

    }

    override fun onTerminate() {
        super.onTerminate()
        instance = null
    }

    fun getFoodContext() : Food {
        checkNotNull(instance) { "this application does not inherit com.moyo.PostMap" }
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
        var instance: Food? = null
        var debug: Boolean? = null
    }
}