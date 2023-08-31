package com.dot_jo.whysalon.base

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.SerializedName

data class BasePagingResponse<T> (

    @SerializedName("current_page"   ) var currentPage  : Int?             = null,
    @SerializedName("data"           ) var data         : ArrayList<T>  = arrayListOf(),
    @SerializedName("first_page_url" ) var firstPageUrl : String?          = null,
    @SerializedName("from"           ) var from         : Int?             = null,
    @SerializedName("last_page"      ) var lastPage     : Int?             = null,
    @SerializedName("per_page"       ) var perPage      : Int?             = null,
    @SerializedName("to"             ) var to           : Int?             = null,
    @SerializedName("total"          ) var total        : Int?             = null

) : BaseResponse(){
    constructor(parcel: Parcel) : this()

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Parcelable> {
        override fun createFromParcel(parcel: Parcel): DevResponse<Parcelable> {
            return DevResponse(parcel)
        }

        override fun newArray(size: Int): Array<Parcelable?> {
            return arrayOfNulls(size)
        }
    }
}

/*

    @Parcelize
    data class ClientChatResponse(
        val id: Int? = null,
        val name: String? = null,
        val image: String? = null
    ) : Parcelable
*/



