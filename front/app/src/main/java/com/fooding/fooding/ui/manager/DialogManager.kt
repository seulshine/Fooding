package com.fooding.fooding.ui.manager

import com.fooding.fooding.data.restapi.RestApi
import com.fooding.fooding.data.vo.PostMenu

class DialogManager {
    suspend fun postMenu(requestBody: PostMenu) : HashMap<Any, Any> {
        return RestApi().postMenu(requestBody)
    }
}