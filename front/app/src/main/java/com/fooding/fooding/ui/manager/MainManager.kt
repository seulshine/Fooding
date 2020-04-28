package com.fooding.fooding.ui.manager

import com.fooding.fooding.data.restapi.RestApi
import com.fooding.fooding.util.Dlog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MainManager : CoroutineScope {
    private var job: Job = Job()
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + job

    fun getMenuList(pathVariable: String) {
        CoroutineScope(coroutineContext).launch {
            try {
                val response = RestApi().getMenu(pathVariable)
                println(response)
            } catch (e: Exception) {
                Dlog.e(e.message.toString())
            }
        }
    }
}