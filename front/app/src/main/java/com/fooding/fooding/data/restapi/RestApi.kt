package com.fooding.fooding.data.restapi

import com.fooding.fooding.data.service.MenuService
import com.fooding.fooding.data.service.UserService
import com.fooding.fooding.data.vo.*
import com.fooding.fooding.util.Dlog
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

class RestApi {
    val menuService: MenuService
    private val userService: UserService

    init {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.34.40.40:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        menuService = retrofit.create(MenuService::class.java)
        userService = retrofit.create()
    }

    suspend fun postMenu(requestBody: PostMenu) : HashMap<Any, Any> {
        return menuService.postDeferredMenuAsync(requestBody).await()
    }

    suspend fun getUserInfo(pathVariable: String) : ApiUser {
        return menuService.getDeferredUserInfoAsync(pathVariable).await()
    }

    suspend fun getDailyMenu(requestBody: GetDailyMenu) : Any {
        return menuService.getDeferredDailyMenu(requestBody).await()
    }

    suspend fun postUser(requestBody: User) : HashMap<String, Any> {
        return userService.postDeferredUser(requestBody).await()
    }

    suspend fun postImage(imageFile: MultipartBody.Part, requestBody: RequestBody) : GetImageInfo {
        return menuService.postDeferredImage(imageFile, requestBody).await()
    }
}