package com.example.whysalon.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize



@Parcelize
data class TimeResponse(
    var id: Int ,
    var default: Boolean? = false,
    var time: String? ) : Parcelable
