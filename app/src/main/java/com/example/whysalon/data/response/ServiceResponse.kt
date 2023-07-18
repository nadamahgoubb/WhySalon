package com.example.whysalon.data.response

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize



@Parcelize
data class ServiceResponse(
    var lat: String?,
    var long: String?,
    var  city: String?,var district: String?, var street: String?, var building: String?,var floor: String?,var flat: String? , var fullAddress:String?
) : Parcelable


 data class ServiceItem(
    var img: Drawable,
    var name: String?,
    var service: String?,
    var cost: String?
  )
