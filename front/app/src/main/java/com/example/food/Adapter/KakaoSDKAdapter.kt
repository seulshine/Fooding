package com.example.food.Adapter

import com.example.food.Food
import com.kakao.auth.*

class KakaoSDKAdapter : KakaoAdapter() {
    override fun getSessionConfig(): ISessionConfig {
        return object : ISessionConfig {
            override fun isSaveFormData(): Boolean = true

            override fun getAuthTypes(): Array<AuthType> = arrayOf(AuthType.KAKAO_ACCOUNT)

            override fun isSecureMode(): Boolean = true

            override fun getApprovalType(): ApprovalType? = ApprovalType.INDIVIDUAL

            override fun isUsingWebviewTimer(): Boolean = false
        }
    }

    override fun getApplicationConfig(): IApplicationConfig = IApplicationConfig { Food.instance?.getFoodContext() }
}