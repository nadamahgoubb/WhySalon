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

@Parcelize
data class NotificationsResponse(
    @SerializedName("notifications") var notifications: Notifications? = null,
) : Parcelable
@Parcelize
data class Notifications(
    @SerializedName("data") var data: MutableList<Notification>? = null,
) : Parcelable
@Parcelize
data class Notification(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("body") var body: String? = null,
    @SerializedName("time") var time: String? = null,
    @SerializedName("offer_id") var offer_id: Int? = null,
    @SerializedName("client_id") var client_id: Int? = null,
    @SerializedName("barber_id") var barber_id: Int? = null,
    @SerializedName("order_id") var order_id: Int? = null,
    @SerializedName("order_status") var order_status: Int? = null,
) : Parcelable

