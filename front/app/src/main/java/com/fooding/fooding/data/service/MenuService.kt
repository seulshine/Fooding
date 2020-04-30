package com.fooding.fooding.data.service

import com.fooding.fooding.data.vo.GetMenu
import com.fooding.fooding.data.vo.PostMenu
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.*

interface MenuService {

    @POST("api/menu")
    @Headers("accept: application/json", "content-type: application/json")
    fun postDeferredMenuAsync(@Body requestBody: PostMenu) : Deferred<HashMap<Any, Any>>

    @GET("api/user/{email}")
    fun getDeferredMenu(@Path("email") pathVariable: String) : Deferred<GetMenu>
}