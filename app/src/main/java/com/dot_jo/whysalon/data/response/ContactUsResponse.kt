package com.dot_jo.whysalon.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize



@Parcelize
data class ContactUsResponse(

    @SerializedName("address"           ) var address         : String?                    = null,
    @SerializedName("facebook_url"      ) var facebookUrl     : String?                    = null,
    @SerializedName("instagram_url"     ) var instagramUrl    : String?                    = null,
    @SerializedName("snapchat"          ) var snapchat        : String?                    = null,
    @SerializedName("contact_us_day"    ) var contactUsDay    : ArrayList<ContactUsDay>    = arrayListOf(),
    @SerializedName("contact_us_phones" ) var contactUsPhones : ArrayList<ContactUsPhones> = arrayListOf()
) : Parcelable



@Parcelize
data class ContactUsDay (

    @SerializedName("id"         ) var id        : Int?     = null,
    @SerializedName("from_day"   ) var fromDay   : FromDay? = FromDay(),
    @SerializedName("to_day"     ) var toDay     : FromDay?  = FromDay(),
    @SerializedName("from_time"  ) var fromTime  : String?  = null,
    @SerializedName("to_time"    ) var toTime    : String?  = null,
    @SerializedName("created_at" ) var createdAt : String?  = null,
    @SerializedName("updated_at" ) var updatedAt : String?  = null

) : Parcelable

@Parcelize
data class FromDay (

    @SerializedName("id"         ) var id        : Int?     = null,
     @SerializedName("name"    ) var name    : String?  = null,
    @SerializedName("created_at" ) var createdAt : String?  = null,
    @SerializedName("updated_at" ) var updatedAt : String?  = null

) : Parcelable


@Parcelize
data class ContactUsPhones (

    @SerializedName("id"           ) var id          : Int?    = null,
    @SerializedName("country_code" ) var countryCode : String? = null,
    @SerializedName("phone"        ) var phone       : String? = null,
    @SerializedName("created_at"   ) var createdAt   : String? = null,
    @SerializedName("updated_at"   ) var updatedAt   : String? = null

) : Parcelable
