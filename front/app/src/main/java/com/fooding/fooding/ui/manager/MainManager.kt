package com.fooding.fooding.ui.manager

import com.fooding.fooding.data.restapi.RestApi
import com.fooding.fooding.data.vo.GetDailyMenu
import com.fooding.fooding.data.vo.GetImageInfo
import com.fooding.fooding.data.vo.PostImage
import com.fooding.fooding.util.Dlog
import kotlinx.coroutines.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MainManager : CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default+ job

    fun getUserInfo(pathVariable: String) {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = RestApi().getUserInfo(pathVariable)
                println(response)
            } catch (e: Exception) {
                Dlog.e(e.message.toString())
            }
        }
    }

    suspend fun getDailyMenu(requestBody: GetDailyMenu) : Any {
        return RestApi().getDailyMenu(requestBody)
    }

    suspend fun postImage(imageFile: MultipartBody.Part, requestBody: RequestBody) : GetImageInfo {
        return RestApi().postImage(imageFile, requestBody)
    }
}