package com.dot_jo.whysalon.data.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize



@Parcelize
data class ServiceResponse(
    var lat: String?,
    var long: String?,
    var  city: String?,var district: String?, var street: String?, var building: String?,var floor: String?,var flat: String? , var fullAddress:String?
) : Parcelable

