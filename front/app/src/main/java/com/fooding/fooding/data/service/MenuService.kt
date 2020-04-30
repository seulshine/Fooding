package com.fooding.fooding.data.service

import com.fooding.fooding.data.vo.*
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface MenuService {

    @POST("api/menu")
    @Headers("accept: application/json", "content-type: application/json")
    fun postDeferredMenuAsync(@Body requestBody: PostMenu) : Deferred<HashMap<Any, Any>>

    @GET("api/user/{email}")
    fun getDeferredUserInfoAsync(@Path("email") pathVariable: String) : Deferred<ApiUser>

    @POST("api/menu/date")
    fun getDeferredDailyMenu(@Body requestBody: GetDailyMenu) : Deferred<Any>

    @Multipart
    @POST("api/image_detect")
    fun postDeferredImage(@Part imageFile: MultipartBody.Part, @Part("image") requestBody: RequestBody) : Deferred<GetImageInfo>
}