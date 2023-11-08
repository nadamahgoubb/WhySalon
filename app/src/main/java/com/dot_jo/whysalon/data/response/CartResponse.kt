package com.dot_jo.whysalon.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class CartResponse(
    @SerializedName("carts") var carts: ArrayList<CartsItemResponse> = arrayListOf(),
    @SerializedName("total") var total: String? = null,
    @SerializedName("tax") var tax: String? = null,
    @SerializedName("finalTotal") var finalTotal: String? = null,

    ) : Parcelable

@Parcelize
data class CartsItemResponse(

    @SerializedName("client_id") val client_id: String?,
    @SerializedName("id") val id: String?,
    @SerializedName("order_id") val order_id: String?,
    @SerializedName("package") val packagee: @RawValue PackageItem?,
    @SerializedName("package_id") val package_id: String?,
    @SerializedName("price") val price: String?,
    @SerializedName("service") val service: ServicesItem?,
    @SerializedName("service_id") val service_id: String?
) : Parcelable

@Parcelize
data class PackageItem(
    @SerializedName("description") var description: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("duration") var duration: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("category_id") var category_id: String? = null,
    @SerializedName("images") var images: ArrayList<ImageItem>? = arrayListOf(),
    // for package details
    @SerializedName("services_count") var servicesCount: Int? = null,
    @SerializedName("services") var services: ArrayList<ServicesItem>? = arrayListOf(),

    ) : Parcelable

@Parcelize
data class TimesOfBarbarResponse(
    @SerializedName("times") var times: ArrayList<String>? = null,
    @SerializedName("date") var date: String? = null,


    ) : Parcelable

@Parcelize
data class TimesItem(
    var time: String? = null,
    var selected: Boolean? = false,
) : Parcelable

@Parcelize
data class BarbarsResponse(
    @SerializedName("barbers") var barbers: ArrayList<BarbarItem>? = null,


    ) : Parcelable

@Parcelize

data class BarbarItem(


    @SerializedName("email")
    var email: String? = null,

    @SerializedName("id")
    var id: String? = null,

    @SerializedName("image")
    var image: String? = null,

    @SerializedName("name")
    var name: String? = null,

    @SerializedName("number_rates")
    var number_rates: Int? = null,

    @SerializedName("avg_rates")
    var avg_rates: String? = null,

    var seleted: Boolean? = false,
    @SerializedName("barber_days") var barberDays: ArrayList<BarberDays> = arrayListOf()

) : Parcelable

@Parcelize


data class BarberDays(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("barber_id") var barberId: Int? = null,
    @SerializedName("week_day_id") var weekDayId: Int? = null,
    @SerializedName("week_day_name") var weekDayName: String? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("to") var to: String? = null

) : Parcelable

@Parcelize


data class CuponResponse(

    @SerializedName("coupon") var coupon: CouponItem? = null,


) : Parcelable

@Parcelize


data class CouponItem(

     @SerializedName("code") var code: String? = null,
    @SerializedName("end_date") var end_date: String? = null,
    @SerializedName("percent") var percent: String? = null,

) : Parcelable

@Parcelize
data class BookingResponse(
    @SerializedName("bookings") var bookings: ArrayList<BookingsItem> = arrayListOf()

) : Parcelable

@Parcelize

data class RebookingResponse(
    @SerializedName("finalTotal") var finalTotal: String? = null,

    ) : Parcelable

@Parcelize
data class BookingsItem(


    @SerializedName("id") var id: String? = null,
    @SerializedName("status") var status: Int? = null,
    @SerializedName("tax") var tax: String? = null,
    @SerializedName("total") var total: String? = null,
    @SerializedName("final_total") var finalTotal: String? = null,
    @SerializedName("final_total_after_discount") var final_total_after_discount: String? = null,
    @SerializedName("date") var date: String? = null,
    @SerializedName("from") var from: String? = null,
    @SerializedName("to") var to: String? = null,
    @SerializedName("duration") var duration: String? = null,
    @SerializedName("payment_method") var paymentMethod: String? = null,
    @SerializedName("payment_method_name") var paymentMethodName: String? = null,
    @SerializedName("customer_number") var customerNumber: String? = null,
    @SerializedName("note") var note: String? = null,
    @SerializedName("client_id") var clientId: Int? = null,
    @SerializedName("barber_id") var barberId: Int? = null,
    @SerializedName("barber") var barber: BarbarItem? = BarbarItem(),
    @SerializedName("carts") var carts: ArrayList<CartsItemResponse> = arrayListOf(),
    @SerializedName("images") var images: ArrayList<ImageItem>? = null

) : Parcelable

/*

"barbers": [
{
    "id": 1,
    "name": "mahmoud",
    "email": "m@gmail.com",
    "image": "https://why-salon.tawajood.com/uploads/barbers/default.png",
    "notify": 1,
    "token": 0,
    "lang": null,
    "count_orders": null,
    "number_rates": null,
    "avg_rates": 5
}
]
}
}*/
