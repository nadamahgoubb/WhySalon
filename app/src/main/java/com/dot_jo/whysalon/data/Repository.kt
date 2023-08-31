package com.dot_jo.whysalon.data.webService


import com.dot_jo.whysalon.data.param.AddBookingParams
import com.dot_jo.whysalon.data.param.AddToCartParams
import com.dot_jo.whysalon.data.param.CheckEmailParam
import com.dot_jo.whysalon.data.param.DeleteFromBookingParam
import com.dot_jo.whysalon.data.param.EditProfileParam
import com.dot_jo.whysalon.data.param.LoginParams
import com.dot_jo.whysalon.data.param.RegisterParams
import com.dot_jo.whysalon.data.param.ResetPasswordParams
import com.dot_jo.whysalon.data.param.ServicesByCategoryParams
import com.dot_jo.whysalon.data.param.UpdateFcmTokenParam
import com.dot_jo.whysalon.data.param.changePasswordParam
import com.dot_jo.whysalon.data.param.DeleteFronCartParams
import com.dot_jo.whysalon.data.param.GetTimesParams
import com.dot_jo.whysalon.data.param.OffersParam
import com.dot_jo.whysalon.data.param.toMap
import com.dot_jo.whysalon.util.FileManager.toMultiPart
import javax.inject.Inject


class Repository @Inject constructor(private val api: ApiInterface) {
    suspend fun login(param: LoginParams) = api.login(param.email, param.password)

    suspend fun register(param: RegisterParams) =
        api.register(param.name, param.email, param.password)
   suspend fun registerGuest( ) = api.registerGuest( )

    suspend fun resetpassword(param: ResetPasswordParams) =
        api.resetPassword(param.email, param.otp, param.password)

    suspend fun checkEmail(param: CheckEmailParam) = api.checkEmail(param.email)

    suspend fun getServicesInCategory(param: ServicesByCategoryParams) =
        api.getServicesInCategory(param.id)//categoryId

    suspend fun getCategories() = api.getCategories()
    suspend fun getPackages() = api.getPackages()
    suspend fun getServiceDetails(param: ServicesByCategoryParams) =
        api.getServiceDetails(param.id) // service _id

    suspend fun getPackageDetails(param: ServicesByCategoryParams) =
        api.getPackageDetails(param.id) // service _id

     suspend fun showProfile() = api.showProfile()
    suspend fun deleteAccount() = api.deleteAccount()
    suspend fun updateFcmToken(params: UpdateFcmTokenParam) =
        api.updateFcmToken(params.fcm_token, params.mobile_id, params.lang)

    suspend fun updateProfile(params: EditProfileParam) =
        api.updateProfile(params.toMap(),params.image.toMultiPart("image"))


    suspend fun changePassword(params: changePasswordParam) =
        api.changePassword(params.oldPassword, params.newPassword)

    suspend fun changeNotifactionStatus() = api.changeNotifactionStatus()
    suspend fun getCart() = api.getCart()

    suspend fun addToCart(params: AddToCartParams) = api.addToCart(params.price, params.package_id, params.service_id)
    suspend fun deleteCart(params: DeleteFronCartParams) = api.deleteFronCart(params.package_id)
    suspend fun getOffers(params: OffersParam) = api.getOffers(params.category_id)
    suspend fun addBooking(params: AddBookingParams) = api.addBooking(params.barber_id, params.date, params.time)
    suspend fun getBarbar( ) = api.getBarbar( )
    suspend fun deleteBooking( param: DeleteFromBookingParam) = param.booking_id?.let {
        api.deleteBooking(
            it
        )
    }
    suspend fun getTimesByBarbarID(params: GetTimesParams) = api.getTimesByBarbarID( params.barber_id, params.date)
    suspend fun getBooking() = api.getBooking()
    suspend fun getHistory() = api.getHistory()
    suspend fun getSetting() = api.getSetting()
}