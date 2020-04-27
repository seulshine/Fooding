package com.fooding.fooding.adapter

import com.fooding.fooding.Fooding
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

    override fun getApplicationConfig(): IApplicationConfig = IApplicationConfig { Fooding.instance?.getFoodContext() }
}