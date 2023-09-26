package com.dot_jo.whysalon.ui.fragment.auth

import com.dot_jo.whysalon.base.Action
import com.dot_jo.whysalon.data.param.RegisterParams
import com.dot_jo.whysalon.data.response.LoginResponse
import com.dot_jo.whysalon.data.response.OtpCheckEmailAfterRegisterResponse
import com.dot_jo.whysalon.data.response.OtpCheckEmailResponse

sealed class AuthAction() : Action {

    data class ShowLoading(val show: Boolean) : AuthAction()
    data class ShowFailureMsg(val message: String?) : AuthAction()
     data  class OtpSuccess(val message: String) : AuthAction()
     data  class RegisterSucess(val data: LoginResponse) : AuthAction()
     data  class ContinueAsGuest(val data: LoginResponse) : AuthAction()
    data  class LoginSuccess(val data: LoginResponse) : AuthAction()
    data class EmailChecked(val data: OtpCheckEmailResponse) : AuthAction()
    data class EmailCheckedAfterRegister(
        val data: OtpCheckEmailAfterRegisterResponse,
     ) : AuthAction()

    data class OtpChecked(val data : OtpCheckEmailResponse) : AuthAction()
}
