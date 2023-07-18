package com.example.whysalon.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize



@Parcelize
data class AddAddressParams(
    var lat: String?,
    var long: String?,
    var  city: String?,var district: String?, var street: String?, var building: String?,var floor: String?,var flat: String? , var fullAddress:String?
) : Parcelable
