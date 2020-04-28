package com.fooding.fooding.data.restapi

import com.fooding.fooding.data.service.MenuService
import com.fooding.fooding.data.service.UserService
import com.fooding.fooding.data.vo.GetMenu
import com.fooding.fooding.data.vo.PostMenu
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RestApi {
    private val menuService: MenuService
    private val userService: UserService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://3.34.40.40:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
        menuService = retrofit.create()
        userService = retrofit.create()
    }

    suspend fun postMenu(requestBody: PostMenu) : HashMap<String, String> {
        return menuService.postDeferredMenu(requestBody).await()
    }

    suspend fun getMenu(pathVariable: String) : GetMenu {
        return menuService.getDeferredMenu(pathVariable).await()
    }
}