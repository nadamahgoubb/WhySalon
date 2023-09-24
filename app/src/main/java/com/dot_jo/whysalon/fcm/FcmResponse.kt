package com.dot_jo.whysalon.fcm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

 data class FcmResponse(
    val BARBER_Id: String?,
    val ORDER_I: String?,
    val BARBER: String?,


    ): Parcelable

