package com.dot_jo.whysalon.ui.fragment.profile

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseViewModel
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.data.param.CheckEmailParam
import com.dot_jo.whysalon.data.param.EditProfileParam
 import com.dot_jo.whysalon.data.param.changePasswordParam
import com.dot_jo.whysalon.data.response.AboutUsResponse
import com.dot_jo.whysalon.data.response.ChangeNotifactionStatus
import com.dot_jo.whysalon.data.response.ContactUsResponse
import com.dot_jo.whysalon.data.response.LoginResponse
import com.dot_jo.whysalon.data.response.NotificationsResponse
import com.dot_jo.whysalon.data.response.OtpChangePassswordResponse
import com.dot_jo.whysalon.data.response.OtpCheckEmailAfterRegisterResponse
import com.dot_jo.whysalon.data.response.PrivacyPolicyResponse
import com.dot_jo.whysalon.domain.AuthUseCase
import com.dot_jo.whysalon.domain.ProfileUseCase
import com.dot_jo.whysalon.domain.SettingUseCase
 import com.dot_jo.whysalon.util.NetworkConnectivity
import com.dot_jo.whysalon.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(app: Application, val useCase: ProfileUseCase,  val useCaseSetting: SettingUseCase ,val authUserCase: AuthUseCase) :
    BaseViewModel<ProfileAction>(app) {

      var editProfileParam: EditProfileParam? =null
      var emailVerified: String? =null

    fun showProfile() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ProfileAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, 1
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(ProfileAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(ProfileAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(ProfileAction.ShowProfile(res.data.data as LoginResponse))
                        }
                    }
                }
            }
        } else {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun editProfileImg(img: File) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ProfileAction.ShowLoading(true))

            viewModelScope.launch {
                var res =
                    useCase.invoke(viewModelScope, PrefsHelper.getUserData()?.client?.name?.let {
                        EditProfileParam(
                            PrefsHelper.getUserData()?.client?.email.toString(), it,
                            PrefsHelper.getUserData()?.client?.country_code.toString(),
                            PrefsHelper.getUserData()?.client?.phone.toString(),PrefsHelper.getUserData()?.client?.date_of_birth.toString(),  img,
                        )
                    }) { res ->
                        when (res) {
                            is Resource.Failure -> produce(ProfileAction.ShowFailureUpdatingImage(res.message.toString()))
                            is Resource.Progress -> produce(ProfileAction.ShowLoading(res.loading))
                            is Resource.Success -> {

                                produce(ProfileAction.ShowProfileUpdates(res.data.data as OtpChangePassswordResponse))
                            }
                        }
                    }
            }
        } else {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun isVaildEditProfile(name: String, email: String, country_code: String, phone: String ,date_of_birth: String? ) {
        if (name.isNullOrBlank()) {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.empty_name_msg)))
            false
        } else if (email.isNullOrBlank()) {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.empty_msg_email)))
            false
        }   else {
            if(PrefsHelper.getUserData()?.client?.email == email ) {
                editProfileData(
                    EditProfileParam(
                        email,
                        name,
                        country_code, phone,
                        date_of_birth, null) )
            }
            else {
                checkEmailAfterRegisteration(email)
            }

            this.editProfileParam =       EditProfileParam(
                email,
                name,
                country_code, phone,
                  date_of_birth, null
            )

            true

        }
    }
    fun checkEmailAfterRegisteration(  email: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ProfileAction.ShowLoading(true))

            viewModelScope.launch {
                var res = authUserCase.invoke(
                    viewModelScope, CheckEmailParam( email, true)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(ProfileAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(ProfileAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(ProfileAction.EmailCheckedAfterRegister(res.data.data as OtpCheckEmailAfterRegisterResponse))
                        }
                    }
                }
            }
        } else {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun editProfileData(data: EditProfileParam) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ProfileAction.ShowLoading(true))

            viewModelScope.launch {
                var res =
                    useCase.invoke(viewModelScope, data?.name?.let {
                        EditProfileParam(
                          data?.email.toString(), it,
                            data?.country_code.toString(),
                            data?.phone.toString(),data?.date_of_birth.toString(),  null,
                        )
                    }) { res ->
                        when (res) {
                            is Resource.Failure -> produce(ProfileAction.ShowFailureMsg(res.message.toString()))
                            is Resource.Progress -> produce(ProfileAction.ShowLoading(res.loading))
                            is Resource.Success -> {

                                produce(ProfileAction.ShowProfileUpdates(res.data.data as OtpChangePassswordResponse))
                            }
                        }
                    }
            }
        } else {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun deleteAccount() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ProfileAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, 2
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(ProfileAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(ProfileAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(ProfileAction.DeleteProfile(res.data.data as OtpChangePassswordResponse))
                        }
                    }
                }
            }
        } else {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun isValidParamsChangePass(oldpass: String, newpass: String, confirmpass: String) {
        if (oldpass.isNullOrBlank()) {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.msg_empty_password)))
            false

        } else if (newpass.isNullOrBlank()) {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.msg_empty_new_password)))
            false

        } else if (confirmpass.isNullOrBlank()) {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.empty_confirm_password)))
            false

        } else if (!confirmpass.equals(newpass)) {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.both_passwords_must_match)))
            false

        } else changePasswordParam(
            com.dot_jo.whysalon.data.param.changePasswordParam(
                oldpass, newpass
            )
        )


    }

    fun changePasswordParam(params: changePasswordParam) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ProfileAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, params
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(ProfileAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(ProfileAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(ProfileAction.ChangePassword(res.data.data as OtpChangePassswordResponse))
                        }
                    }
                }
            }
        } else {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun changeNotifactionStatus() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ProfileAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(ProfileAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(ProfileAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(ProfileAction.NotifactionChanged(res.data.data as ChangeNotifactionStatus))
                        }
                    }
                }
            }
        } else {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun getNotifaction() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(ProfileAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseSetting.invoke(
                    viewModelScope,4
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(ProfileAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(ProfileAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(ProfileAction.getNotifaction(res.data.data as NotificationsResponse))
                        }
                    }
                }
            }
        } else {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getPrivacyPolicy() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ProfileAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseSetting.invoke(
                    viewModelScope, 3
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(ProfileAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(ProfileAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            (res.data.data as PrivacyPolicyResponse)?.let {
                                produce(
                                    ProfileAction.ShowPrivacy(
                                        it
                                    )
                                )
                            }
                        }
                    }
                }
            }
        } else {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }  fun getAbouUs() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ProfileAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseSetting.invoke(
                    viewModelScope, 4
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(ProfileAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(ProfileAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            (res.data.data as AboutUsResponse)?.let {
                                produce(
                                    ProfileAction.ShowAboutUs(
                                        it
                                    )
                                )
                            }
                        }
                    }
                }
            }
        } else {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
  fun getContactusData() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ProfileAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseSetting.invoke(
                    viewModelScope
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(ProfileAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(ProfileAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            (res.data.data as ContactUsResponse)?.let {
                                produce(
                                    ProfileAction.ShowContactUsResponse(
                                        it
                                    )
                                )
                            }
                        }
                    }
                }
            }
        } else {
            produce(ProfileAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

}
