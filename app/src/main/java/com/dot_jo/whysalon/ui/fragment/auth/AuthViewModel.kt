package com.dot_jo.whysalon.ui.fragment.auth


import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseViewModel
import com.dot_jo.whysalon.data.param.CheckEmailParam
import com.dot_jo.whysalon.data.param.LoginParams
import com.dot_jo.whysalon.data.param.RegisterParams
import com.dot_jo.whysalon.data.param.ResetPasswordParams
import com.dot_jo.whysalon.data.response.LoginResponse
import com.dot_jo.whysalon.data.response.OtpCheckEmailResponse
import com.dot_jo.whysalon.domain.AuthUseCase
import com.dot_jo.whysalon.util.NetworkConnectivity
import com.dot_jo.whysalon.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(app: Application, val authUserCase: AuthUseCase) :
    BaseViewModel<AuthAction>(app) {
    var email: String? = null
    var otp: String? = null

fun isValidParamsChangePass(  newpass: String, confirmpass: String) {
      if (newpass.isNullOrBlank()) {
        produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_new_password)))
        false

    }
    else if (confirmpass.isNullOrBlank()) {
        produce(AuthAction.ShowFailureMsg(getString(R.string.empt_Confirm_pass)))
        false

    }else if (!confirmpass.equals(newpass)) {
        produce(AuthAction.ShowFailureMsg(getString(R.string.password_dont_match)))
        false

    }
    else
          email?.let { sendOtp(it, otp.toString(), confirmpass) }


}  
fun isVaildLogin(
        email: String?, password: String?, type: Int
    ) { // 1 FOR NORMAL  // 0 FOR LOGIN WITH BIOMETRIC
        if (email.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_msg_email)))
            false
        } else if (password.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_password)))
            false
        } else {


            loginWithPhoneNumber(email, password, type)

            true

        }
    }

    fun loginWithPhoneNumber(email: String, password: String, type: Int) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope, LoginParams(
                        email, password
                    ) // static 0 for android devices
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(
                                AuthAction.LoginSuccess(
                                    res.data.data as LoginResponse
                                )
                            )


                        }
                    }
                }
            }
        } else {
            produce(AuthAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }




      fun checkEmail(email: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope, CheckEmailParam( email)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AuthAction.EmailChecked(res.data.data as OtpCheckEmailResponse))
                        }
                    }
                }
            }
        } else {
            produce(AuthAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
   private fun sendOtp(email: String, otp: String, newpassword :String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope, ResetPasswordParams(email, otp, newpassword )
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AuthAction.OtpSuccess(res.data.message))
                        }
                    }
                }
            }
        } else {
            produce(AuthAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun isVaildRegisteration(name: String, email: String, country_code: String, phone: String, pass: String, repeated_pass: String) {
        if (name.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_name_msg)))
            false
        } else if (email.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_msg_email)))
            false
        } else if (phone.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_phone_number)))
            false
        } else if (pass.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_password)))
            false
        } else if (repeated_pass.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_confirm_password)))
            false
        } else if (!repeated_pass.equals(pass)) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.password_dont_match)))
            false
        } else {

            register(
                RegisterParams(
                    name,
                    email,
                    country_code, phone,
                    pass,
                )
            )
            true

        }
    }


    private fun register(params: RegisterParams) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope, params
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            (res.data.data as LoginResponse)?.let {
                                AuthAction.RegisterSucess(
                                    it
                                )
                            }?.let { produce(it) }
                        }
                    }
                }
            }
        } else {
            produce(AuthAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
      fun continueAsGyest() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            (res.data.data as LoginResponse)?.let {
                                AuthAction.ContinueAsGuest(
                                    it
                                )
                            }?.let { produce(it) }
                        }
                    }
                }
            }
        } else {
            produce(AuthAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
}
