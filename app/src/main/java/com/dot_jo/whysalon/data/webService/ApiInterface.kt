package com.dot_jo.whysalon.data.webService


import com.dot_jo.whysalon.base.BasePagingResponse
import com.dot_jo.whysalon.base.DevResponse
import com.dot_jo.whysalon.base.ErrorResponse
import com.dot_jo.whysalon.base.NetworkResponse
import com.dot_jo.whysalon.data.response.AboutUsResponse
import com.dot_jo.whysalon.data.response.BarbarsResponse
import com.dot_jo.whysalon.data.response.BookingResponse
import com.dot_jo.whysalon.data.response.CartResponse
import com.dot_jo.whysalon.data.response.CategoiesAndServicesResponse
import com.dot_jo.whysalon.data.response.CategoriesResponse
import com.dot_jo.whysalon.data.response.ChangeNotifactionStatus
import com.dot_jo.whysalon.data.response.ContactUsResponse
import com.dot_jo.whysalon.data.response.CuponResponse
import com.dot_jo.whysalon.data.response.LoginResponse
import com.dot_jo.whysalon.data.response.NotificationItem
import com.dot_jo.whysalon.data.response.OffersResponse
import com.dot_jo.whysalon.data.response.OtpChangePassswordResponse
import com.dot_jo.whysalon.data.response.OtpCheckEmailAfterRegisterResponse
import com.dot_jo.whysalon.data.response.OtpCheckEmailResponse
import com.dot_jo.whysalon.data.response.PackageDetails
import com.dot_jo.whysalon.data.response.PackagesResponse
import com.dot_jo.whysalon.data.response.PrivacyPolicyResponse
import com.dot_jo.whysalon.data.response.RebookingResponse
import com.dot_jo.whysalon.data.response.ServicesResponse
import com.dot_jo.whysalon.data.response.ServiceDetails
import com.dot_jo.whysalon.data.response.TimesOfBarbarResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import javax.inject.Singleton


@Singleton
interface ApiInterface {
    @POST("auth/login")
    @FormUrlEncoded
    suspend fun login(
        @Field("email") email: String, @Field("password") password: String
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @GET("auth/login/login-by-google/{id}")
     suspend fun loginbyGoogle(
        @Path("id") id: String
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>
   @POST("auth/register/check-email")
    @FormUrlEncoded
    suspend fun checkMailInRegisteration(
        @Field("email") email: String
    ): NetworkResponse<DevResponse<OtpCheckEmailAfterRegisterResponse>, ErrorResponse>

    @POST("auth/register")
    @FormUrlEncoded
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("country_code") country_code: String,
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("google_id") google_id: String?,
        @Field("date_of_birth") date_of_birth: String?
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @GET("auth/register/guest")
    suspend fun registerGuest(
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @POST("auth/reset-password/check-email")
    @FormUrlEncoded
    suspend fun checkEmail(
        @Field("email") email: String,
    ): NetworkResponse<DevResponse<OtpCheckEmailResponse>, ErrorResponse>
    @GET("auth/reset-password/check-otp/{email}/{otp}")
     suspend fun checkOtp(
        @Path("email") email: String,
        @Path("otp") otp: String

    ): NetworkResponse<DevResponse<OtpCheckEmailResponse>, ErrorResponse>

    @POST("auth/reset-password")
    @FormUrlEncoded
    suspend fun resetPassword(
        @Field("email") email: String,
        @Field("otp") otp: String,
        @Field("password") password: String
    ): NetworkResponse<DevResponse<OtpChangePassswordResponse>, ErrorResponse>

    @GET("services")
    suspend fun getServicesInCategory(
        @Query("category_id") category_id: String,
    ): NetworkResponse<DevResponse<ServicesResponse>, ErrorResponse>

    @GET("categories")
    suspend fun getCategories(
    ): NetworkResponse<DevResponse<CategoriesResponse>, ErrorResponse>

    @GET("package/{packagr_id}")
    suspend fun getPackageDetails(
        @Path("packagr_id") packagr_id: String
    ): NetworkResponse<DevResponse<PackageDetails>, ErrorResponse>

    @GET("packages")
    suspend fun getPackages(
    ): NetworkResponse<DevResponse<PackagesResponse>, ErrorResponse>

    @GET("service/{serviceId}")
    suspend fun getServiceDetails(
        @Path("serviceId") serviceId: String
    ): NetworkResponse<DevResponse<ServiceDetails>, ErrorResponse>


    @GET("profile/show")
    suspend fun showProfile(
    ): NetworkResponse<DevResponse<LoginResponse>, ErrorResponse>

    @GET("profile/delete-account")
    suspend fun deleteAccount(
    ): NetworkResponse<DevResponse<OtpChangePassswordResponse>, ErrorResponse>

    @POST("profile/update-fcm-token")
    @FormUrlEncoded
    suspend fun updateFcmToken(
        @Field("fcm_token") fcm_token: String,
        @Field("mobile_id") mobile_id: Int,
        @Field("lang") lang: String,
    ): NetworkResponse<DevResponse<OtpChangePassswordResponse>, ErrorResponse>

    @POST("profile/update")
    @Multipart
    @JvmSuppressWildcards
    suspend fun updateProfile(
        @PartMap updateMap: Map<String, RequestBody>,
        @Part image: MultipartBody.Part?
    ): NetworkResponse<DevResponse<OtpChangePassswordResponse>, ErrorResponse>

    @POST("profile/change-password")
    @FormUrlEncoded
    suspend fun changePassword(
        @Field("old_password") old_password: String,
        @Field("password") password: String,
    ): NetworkResponse<DevResponse<OtpChangePassswordResponse>, ErrorResponse>

    @GET("profile/change-status-notify")
    suspend fun changeNotifactionStatus(): NetworkResponse<DevResponse<ChangeNotifactionStatus>, ErrorResponse>

    @GET("cart")
    suspend fun getCart(): NetworkResponse<DevResponse<CartResponse>, ErrorResponse>

    @POST("cart/add")
    @FormUrlEncoded
    suspend fun addToCart(
        @Field("price") price: String,
        @Field("package_id") package_id: String?,
        @Field("service_id") service_id: String?,
    ): NetworkResponse<DevResponse<OtpChangePassswordResponse>, ErrorResponse>

    @GET("cart/delete/{package_id}")
    suspend fun deleteFronCart(
        @Path("package_id") package_id: String,
    ): NetworkResponse<DevResponse<OtpChangePassswordResponse>, ErrorResponse>

    @GET("offers")
    suspend fun getOffers(
        @Query("category_id") category_id: String,
    ): NetworkResponse<DevResponse<OffersResponse>, ErrorResponse>


    @POST("booking/times-by-date-and-barber-id")
    @FormUrlEncoded
    suspend fun getTimesByBarbarIDReBooking(
        @Field("barber_id") barber_id: String,
        @Field("date") date: String?, //yyyy-mm-dd
        @Field("order_id") orderId: String? ,
    ): NetworkResponse<DevResponse<TimesOfBarbarResponse>, ErrorResponse>


    @POST("booking/times-by-date-and-barber-id")
    @FormUrlEncoded
    suspend fun getTimesByBarbarID(
        @Field("barber_id") barber_id: String,
        @Field("date") date: String?, //yyyy-mm-dd
    ): NetworkResponse<DevResponse<TimesOfBarbarResponse>, ErrorResponse>

    @GET("barbers")
    suspend fun getBarbar(
    ): NetworkResponse<DevResponse<BarbarsResponse>, ErrorResponse>
    @FormUrlEncoded
    @POST("booking/check-coupon")
    suspend fun checkCupon(
        @Field("code") code: String?,
    ): NetworkResponse<DevResponse<CuponResponse>, ErrorResponse>

    @FormUrlEncoded
    @POST("booking/add-booking")
    suspend fun addReBooking(
        @Field("barber_id") barber_id: String,
        @Field("date") date: String?,
        @Field("time") time: String?,
        @Field("order_id") order_id: String = "",
    ): NetworkResponse<DevResponse<OtpChangePassswordResponse>, ErrorResponse>

    @FormUrlEncoded
    @POST("booking/add-booking")
    suspend fun addBooking(
        @Field("barber_id") barber_id: String,
        @Field("date") date: String?,
        @Field("time") time: String?,
        @Field("payment_method") payment_method: String?,
        @Field("discount_code") discount_code: String?,
        @Field("phone") phone: String?,
        @Field("country_code") country_code: String?,
    ): NetworkResponse<DevResponse<OtpChangePassswordResponse>, ErrorResponse>

    @GET("booking/delete-booking/{orderId}")
    suspend fun deleteBooking(
        @Path("orderId") orderId: String,
    ): NetworkResponse<DevResponse<OtpChangePassswordResponse>, ErrorResponse>

    @GET("booking/rebooking/{orderId}")
    suspend fun rebooking(
        @Path("orderId") orderId: String,
    ): NetworkResponse<DevResponse<RebookingResponse>, ErrorResponse>

    @GET("booking")
    suspend fun getBooking(
    ): NetworkResponse<DevResponse<BookingResponse>, ErrorResponse>

    @GET("history")
    suspend fun getHistory(
    ): NetworkResponse<DevResponse<BookingResponse>, ErrorResponse>

    @GET("settings")
    suspend fun getSetting(
    ): NetworkResponse<DevResponse<PrivacyPolicyResponse>, ErrorResponse>
 @GET("about-us")
    suspend fun getabout(
    ): NetworkResponse<DevResponse<AboutUsResponse>, ErrorResponse>

    @GET("notifications")
    suspend fun getNotifications(
        @Query("page") page: Int ): NetworkResponse<BasePagingResponse<NotificationItem>, ErrorResponse>

    @GET("contact-us")
    suspend fun getContactUsData(
    ): NetworkResponse<DevResponse<ContactUsResponse>, ErrorResponse>
  @GET("categories_and_services")
    suspend fun getCategoriesAndServices(
    ): NetworkResponse<DevResponse<CategoiesAndServicesResponse>, ErrorResponse>

    @FormUrlEncoded
    @POST("barber-rate")
    suspend fun rate(
        @Field("rate") rate: String,
        @Field("comment") comment: String,
        @Field("barber_id") barber_id: String,
        @Field("order_id") order_id: String,
    ): NetworkResponse<DevResponse<OtpChangePassswordResponse>, ErrorResponse>
}