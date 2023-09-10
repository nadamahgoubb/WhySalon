package com.dot_jo.whysalon.data.param

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class HomeParams()


@Parcelize
data class ServicesByCategoryParams(
    var id: String = "",
    var type: Int? = -1
) : Parcelable


@Parcelize
data class OffersParam(
    var category_id: String = "",
) : Parcelable


@Parcelize
data class RateParam(
    var rate: String = "",
    var comment: String = "",
    var order_id: String = "",
    var barber_id: String = "",
) : Parcelable
