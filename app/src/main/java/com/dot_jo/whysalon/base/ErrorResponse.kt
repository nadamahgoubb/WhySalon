package com.dot_jo.whysalon.base

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ErrorResponse(
    @SerializedName("msg")
    var Error: String = "",
 @SerializedName("status")
    var status: Boolean = false,

) : Parcelable
