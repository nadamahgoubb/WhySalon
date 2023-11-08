package com.dot_jo.whysalon.data.response

  import android.os.Parcelable
  import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
  import kotlinx.android.parcel.RawValue


@Parcelize

    data class Barber (

@SerializedName("id"           ) var id          : Int?                  = null,
@SerializedName("name"         ) var name        : String?               = null,
@SerializedName("email"        ) var email       : String?               = null,
@SerializedName("image"        ) var image       : String?               = null,
@SerializedName("notify"       ) var notify      : Int?                  = null,
@SerializedName("token"        ) var token       : Int?                  = null,
@SerializedName("lang"         ) var lang        : String?               = null,
@SerializedName("count_orders" ) var countOrders : String?               = null,
@SerializedName("number_rates" ) var numberRates : Int?                  = null,
@SerializedName("avg_rates"    ) var avgRates    : Double?               = null,


    ) : Parcelable

@Parcelize
data class Services (

    @SerializedName("id"          ) var id          : Int?              = null,
    @SerializedName("name"        ) var name        : String?           = null,
    @SerializedName("description" ) var description : String?           = null,
    @SerializedName("duration"    ) var duration    : Int?              = null,
    @SerializedName("price"       ) var price       : Double?           = null,
    @SerializedName("category_id" ) var categoryId  : Int?              = null,
    @SerializedName("image"       ) var image       : String?           = null,
    @SerializedName("images"      ) var images      : ArrayList<String> = arrayListOf()

) : Parcelable

@Parcelize
data class ServicesHomeItem (
    @SerializedName("service_id"          ) var serviceId          : Int?              = null,
@SerializedName("service_name"        ) var serviceName        : String?           = null,
@SerializedName("service_description" ) var serviceDescription : String?           = null,
@SerializedName("service_duration"    ) var serviceDuration    : Int?              = null,
@SerializedName("category_id"         ) var categoryId         : Int?              = null,
@SerializedName("service_image"       ) var serviceImage       : String?           = null,
@SerializedName("small_image"         ) var smallImage         : String?           = null,
@SerializedName("offer"               ) var offer              : Boolean?          = null,
@SerializedName("price"               ) var price              : Int?              = null,
@SerializedName("price_before"        ) var priceBefore        : Int?              = null,
@SerializedName("images"              ) var images             : @RawValue ArrayList<Images> = arrayListOf()


) : Parcelable

data class Images (

    @SerializedName("id"   ) var id   : Int?    = null,
    @SerializedName("name" ) var name : String? = null

)