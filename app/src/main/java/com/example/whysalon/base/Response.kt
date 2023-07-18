package com.example.whysalon.base

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
open class BaseResponse(

    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: Boolean = false
    // val Error: String?,
) : Parcelable

data class DevResponse<T>(
    @SerializedName("data")
    var data:  T? = null

) : BaseResponse() {
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

data class MainResponsePaging<T>(
    val Status: String?,
    val Error: Array<String>?,
    val dataPaging: DataPaging<T>
)

data class DataPaging<T>(
    val page: Int? = 0,
    val listDataPaging: ListDataPaging<T>?,
    val total_pages: Int = 0
)

data class ListDataPaging<T>(

    @SerializedName("Data")
    val listOfData: List<T?>? = null
)

enum class ServerStatusCodes(val code: Int) {
    SUCCESS(200),
    FAIL(401), //fail with message
    SOCIAL_REGISTER(402), //login with social and needs to complete data
    TOKEN_EXPIRED(403),
    ACTIVE_ACCOUNT(405),
}
