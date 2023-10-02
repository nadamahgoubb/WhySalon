package com.dot_jo.whysalon.data.param

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class AuthParams()


@Parcelize
data class LoginParams(

     var email: String = "",
    var password: String = "",


    ) : Parcelable


@Parcelize
data class CheckEmailParam(

    var email: String = "",
    var inregister: Boolean = true,

    ) : Parcelable


@Parcelize
data class CheckOtpWithEmailParam(

    var email: String = "",
    var otp: String = "",

    ) : Parcelable


@Parcelize
data class ResetPasswordParams(

    var email: String = "",
    var otp: String = "",
    var password:  String = ""
) : Parcelable

@Parcelize
data class RegisterParams(
    val name: String="",
    val email: String="",
    val country_code: String="",
    val phone: String="",
     val password: String="",

 ) : Parcelable
@Parcelize
data class UpdateFcmTokenParam(
val  fcm_token: String,
val mobile_id: Int,
val  lang: String,
 ) : Parcelable



@Parcelize
data class EditProfileParam(

    var email: String = "",
    var name: String = "",
    var country_code: String = "",
    var phone: String = "",
    var image:File

    ) : Parcelable


fun EditProfileParam.toMap(): Map<String, RequestBody>{

    val itemMap = hashMapOf<String, RequestBody>()

    itemMap["email"] = email.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())

    itemMap["name"] = name.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["country_code"] = country_code.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())
    itemMap["phone"] = phone.toString().toRequestBody("multipart/form-data".toMediaTypeOrNull())


    return itemMap
}
@Parcelize
data class changePasswordParam(

    var oldPassword: String = "",
    var newPassword: String = "",


    ) : Parcelable

