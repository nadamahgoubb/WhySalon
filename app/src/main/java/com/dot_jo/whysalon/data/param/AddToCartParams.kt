package com.dot_jo.whysalon.data.param


data class AddToCartParams(
    var package_id: String?,
    var service_id: String?,
    var price: String
)

data class DeleteFronCartParams(
    var package_id: String = "",
)

data class AddReBookingParams(
    var barber_id: String,
    var date: String,
    var time: String,
    var order_id: String = "",
)
data class AddBookingParams(
    var barber_id: String,
    var date: String,
    var time: String,
    var  payment_method: String, var discount_code: String?,
    var   phone: String?,var country_code: String?,
)

data class GetTimesParams(
    var barber_id: String,
    var date: String,
)

data class CheckCuponParams(
    var code: String,
 )

data class GetTimesReBookingParams(
    var barber_id: String,
    var date: String,
    var orderId: String? = null,
)