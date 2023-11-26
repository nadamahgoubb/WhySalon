package com.dot_jo.whysalon.data


import com.dot_jo.whysalon.base.PagingParams
import com.dot_jo.whysalon.data.param.AddBookingParams
import com.dot_jo.whysalon.data.param.AddReBookingParams
import com.dot_jo.whysalon.data.param.AddToCartParams
import com.dot_jo.whysalon.data.param.CheckCuponParams
import com.dot_jo.whysalon.data.param.CheckEmailParam
import com.dot_jo.whysalon.data.param.CheckOtpWithEmailParam
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
import com.dot_jo.whysalon.data.param.GetTimesReBookingParams
import com.dot_jo.whysalon.data.param.OffersParam
import com.dot_jo.whysalon.data.param.RateParam
import com.dot_jo.whysalon.data.param.ReBookingParam
import com.dot_jo.whysalon.data.param.loginbyGoogleParams
import com.dot_jo.whysalon.data.param.toMap
import com.dot_jo.whysalon.data.webService.ApiInterface
import com.dot_jo.whysalon.util.FileManager.toMultiPart
import javax.inject.Inject


class Repository @Inject constructor(private val api: ApiInterface) {
    suspend fun login(param: LoginParams) = api.login(param.email, param.password)
    suspend fun loginbyGoogle(param: loginbyGoogleParams) = api.loginbyGoogle(param.id )

    suspend fun register(param: RegisterParams) =
        api.register(param.name, param.email,param.country_code, param.phone, param.password,param.google_id , param.date_of_birth)
 suspend fun checkMailInRegisteration(param: CheckEmailParam) =
        api.checkMailInRegisteration(  param.email )

    suspend fun registerGuest() = api.registerGuest()

    suspend fun resetpassword(param: ResetPasswordParams) =
        api.resetPassword(param.email, param.otp, param.password)

    suspend fun checkEmail(param: CheckEmailParam) = api.checkEmail(param.email)
    suspend fun checkOtp(param: CheckOtpWithEmailParam) = api.checkOtp(param.email, param.otp)

    suspend fun getServicesInCategory(param: ServicesByCategoryParams) =
        api.getServicesInCategory(param.id)//categoryId

    suspend fun getCategories() = api.getCategories()
    suspend fun getCategoriesAndServices() = api.getCategoriesAndServices()
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
        api.updateProfile(params.toMap(), params.image?.toMultiPart("image"))


    suspend fun changePassword(params: changePasswordParam) =
        api.changePassword(params.oldPassword, params.newPassword)

    suspend fun changeNotifactionStatus() = api.changeNotifactionStatus()
    suspend fun getCart() = api.getCart()

    suspend fun addToCart(params: AddToCartParams) =
        api.addToCart(params.price, params.package_id, params.service_id)

    suspend fun deleteCart(params: DeleteFronCartParams) = api.deleteFronCart(params.package_id)
    suspend fun getOffers(params: OffersParam) = api.getOffers(params.category_id)
    suspend fun addReBooking(params: AddReBookingParams) =
        api.addReBooking(params.barber_id, params.date, params.time, params.order_id,params.payment_method, params.discount_code,params.phone, params.country_code)
    suspend fun addBooking(params: AddBookingParams) =
        api.addBooking(params.barber_id, params.date, params.time, params.payment_method, params.discount_code,params.phone, params.country_code
        )

    suspend fun getBarbar() = api.getBarbar()
    suspend fun checkCupon(param: CheckCuponParams) = api.checkCupon(param.code)
    suspend fun deleteBooking(param: DeleteFromBookingParam) = param.booking_id?.let {
        api.deleteBooking(
            it
        )
    }
     suspend fun rebooking(param: ReBookingParam) = param.booking_id?.let {
        api.rebooking(
            it
        )
    }

    suspend fun getTimesByBarbarID(params: GetTimesParams) =
        api.getTimesByBarbarID(params.barber_id, params.date,)
    suspend fun getTimesByBarbarIDReBooking(params: GetTimesReBookingParams) =
        api.getTimesByBarbarIDReBooking(params.barber_id, params.date, params?.orderId)

    suspend fun rate(params: RateParam) =
        api.rate(params.rate, params.comment, params.barber_id, params.order_id)

    suspend fun getBooking() = api.getBooking()
    suspend fun getHistory() = api.getHistory()
    suspend fun getSetting() = api.getSetting()
    suspend fun getabout() = api.getabout()
    suspend fun getNotifications(param: PagingParams) = api.getNotifications(param.page)
    suspend fun getContactUsData() = api.getContactUsData()
}