package com.dot_jo.whysalon.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginResponse(
    @SerializedName("client") var client: Client? = null,
) : Parcelable

@Parcelize
data class Client(
    @SerializedName("token") var token: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("notify") var notify: String? = null,

) : Parcelable

@Parcelize
data class OtpCheckEmailResponse(
    @SerializedName("exists") var exists: Boolean? = null,
) : Parcelable

@Parcelize
data class OtpChangePassswordResponse(
    @SerializedName("scalar") var scalar: String? = null,
) : Parcelable

@Parcelize
data class ChangeNotifactionStatus(
    @SerializedName("notify") var notify: Boolean? = null,
) : Parcelable

@Parcelize
data class PrivacyPolicyResponse(
    @SerializedName("terms_and_conditions") var terms_and_conditions: String? = null,
    @SerializedName("privacy_policy") var privacy_policy: String? = null
) : Parcelable
