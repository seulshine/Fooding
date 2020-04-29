package com.fooding.fooding.ui.manager

import com.fooding.fooding.data.restapi.RestApi
import com.fooding.fooding.data.vo.User
import com.fooding.fooding.util.Dlog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class ProfileManager : CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun postUser(requestBody: User) {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = RestApi().postUser(requestBody)
                println(response)
            } catch (e: Exception) {
                Dlog.e(e.message.toString())
            }
        }
    }
}