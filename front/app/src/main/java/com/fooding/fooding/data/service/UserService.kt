package com.fooding.fooding.data.service

import com.fooding.fooding.data.vo.User
import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface UserService {
    @POST("api/login")
    @Headers("accept: application/json", "content-type: application/json")
    fun postDeferredUser(@Body requestBody: User) : Deferred<HashMap<String, Any>>
}