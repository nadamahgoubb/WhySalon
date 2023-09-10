package com.dot_jo.whysalon.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ServicesResponse(
    @SerializedName("services") var services: ArrayList<ServicesItem>? = arrayListOf(),
) : Parcelable

@Parcelize
data class ServicesItem(
    @SerializedName("description") var description: String? = null,
    @SerializedName("email") var email: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("duration") var duration: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("price_before") var price_before: String? = null,
    @SerializedName("category_id") var category_id: String? = null,
    @SerializedName("images") var images: ArrayList<ImageItem>? = arrayListOf(),
    // for package details
    @SerializedName("services_count") var servicesCount: Int? = null,

    ) : Parcelable

@Parcelize
data class CategoriesResponse(
    @SerializedName("categories") var services: ArrayList<CategoriesItem>? = arrayListOf(),
) : Parcelable

@Parcelize
data class CategoriesItem(
    @SerializedName("description") var description: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("name") var name: String? = null,
var checked :Int =0

    ) : Parcelable

@Parcelize
data class PackagesResponse(
    @SerializedName("packages") var packages: ArrayList<ServicesItem>? = arrayListOf(),
) : Parcelable

@Parcelize
data class ServiceDetails(

    @SerializedName("service") var service: ServicesItem? = null,

    ) : Parcelable

@Parcelize
data class ImageItem(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null,

    ) : Parcelable

@Parcelize
data class PackageDetails(
    @SerializedName("package") var packagee: ServicesItem? = ServicesItem(),
    @SerializedName("services") var services: ArrayList<ServicesItem> = arrayListOf()


) : Parcelable

@Parcelize
data class OffersResponse(
    @SerializedName("offers") var offers: ArrayList<OfferssItem> = arrayListOf()


) : Parcelable


@Parcelize
data class OfferssItem(
      @SerializedName("id") var id: String? = null,
    @SerializedName("image") var image: String? = null,
      @SerializedName("price") var price: String? = null,
    @SerializedName("category_id") var category_id: String? = null,
    @SerializedName("service_id") var service_id: String? = null,
    @SerializedName("service") var services:   ServicesItem? =  null



) : Parcelable
