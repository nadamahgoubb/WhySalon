package com.dot_jo.whysalon.ui.fragment.auth


import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseViewModel
import com.dot_jo.whysalon.data.param.CheckEmailParam
import com.dot_jo.whysalon.data.param.CheckOtpWithEmailParam
import com.dot_jo.whysalon.data.param.LoginParams
import com.dot_jo.whysalon.data.param.RegisterParams
import com.dot_jo.whysalon.data.param.ResetPasswordParams
import com.dot_jo.whysalon.data.param.loginbyGoogleParams
import com.dot_jo.whysalon.data.response.LoginResponse
import com.dot_jo.whysalon.data.response.OtpCheckEmailAfterRegisterResponse
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
    var registerParam: RegisterParams? = null
    var emailVerified =""

fun isValidParamsChangePass(  newpass: String, confirmpass: String) {
      if (newpass.isNullOrBlank()) {
        produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_new_password)))
        false

    }
    else if (confirmpass.isNullOrBlank()) {
        produce(AuthAction.ShowFailureMsg(getString(R.string.empty_confirm_password)))
        false

    } else if (confirmpass.length<8 ||newpass.length<8 ) {
        produce(AuthAction.ShowFailureMsg(getString(R.string.passmust_be_at_least_8_characters)))
        false

    }else if (!confirmpass.equals(newpass)) {
        produce(AuthAction.ShowFailureMsg(getString(R.string.both_passwords_must_match)))
        false

    }
    else
          email?.let { sendOtp(it, otp.toString(), confirmpass) }


}  
fun isVaildLogin(
        email: String?, password: String?, soical: Boolean
    ) { // 1 FOR NORMAL  // 0 FOR LOGIN WITH BIOMETRIC
        if (email.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_msg_email)))
            false
        } else if (password.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_password)))
            false
        } else {


            loginWithPhoneNumber(email, password, soical)

            true

        }
    }

    fun loginWithPhoneNumber(email: String, password: String, social: Boolean ) {
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
                                    res.data.data as LoginResponse, social
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

  fun loginWithGoogle(accountId: String ) {
                if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope, loginbyGoogleParams(
                      accountId
                    ) // static 0 for android devices
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(
                                AuthAction.LoginSuccess(
                                    res.data.data as LoginResponse, true
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
                    viewModelScope, CheckEmailParam( email, false)
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
    }   fun checkOtp(email: String, otp: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                authUserCase.invoke(
                    viewModelScope, CheckOtpWithEmailParam( email, otp)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AuthAction.OtpChecked(res.data.data as OtpCheckEmailResponse))
                        }
                    }
                }
            }
        } else {
            produce(AuthAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun checkEmailAfterRegisteration(  email: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(AuthAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope, CheckEmailParam( email, true)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(AuthAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(AuthAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(AuthAction.EmailCheckedAfterRegister(res.data.data as OtpCheckEmailAfterRegisterResponse   ))
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

    fun isVaildRegisteration(name: String, email: String, country_code: String, phone: String, pass: String, repeated_pass: String,date_of_birth: String?, google_id:String?, social :Boolean) {
        if (name.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_name_msg)))
            false
        } else if (email.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_msg_email)))
            false
        } else if (!social&&phone.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_phone_number)))
            false
        } else if (pass.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.msg_empty_password)))
            false
        } else if (repeated_pass.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_confirm_password)))
            false
        }  else if (!social&&date_of_birth.isNullOrBlank()) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.empty_date_of_birth)))
            false
        } else if (!repeated_pass.equals(pass)) {
            produce(AuthAction.ShowFailureMsg(getString(R.string.both_passwords_must_match)))
            false
        } else {
            if(emailVerified == email ) {
                register(
                    RegisterParams(
                        name,
                        email,
                        country_code, phone,
                        pass ,google_id,date_of_birth), social)
            }
            else {
                checkEmailAfterRegisteration(email)
            }

            this.registerParam =       RegisterParams(
                name,
                email,
                country_code, phone,
                pass,google_id, date_of_birth
            )

            true

        }
    }


      fun register(params: RegisterParams, social: Boolean) {
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
                                    it, social
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
