package com.dot_jo.whysalon.ui.fragment.home

import com.dot_jo.whysalon.base.Action
import com.dot_jo.whysalon.data.response.CartResponse
import com.dot_jo.whysalon.data.response.CategoiesAndServicesResponse
import com.dot_jo.whysalon.data.response.CategoriesResponse
import com.dot_jo.whysalon.data.response.OffersResponse
import com.dot_jo.whysalon.data.response.OtpChangePassswordResponse
import com.dot_jo.whysalon.data.response.PackagesResponse
import com.dot_jo.whysalon.data.response.ServicesResponse
import com.dot_jo.whysalon.ui.fragment.basket.BasketAction

sealed class HomeAction() : Action {

    data class ShowLoading(val show: Boolean) : HomeAction()
    data class UpdateFcmToken(val show: Boolean) : HomeAction()
    data class Rate(val data: OtpChangePassswordResponse) : HomeAction()
    data class ShowFailureMsg(val message: String?) : HomeAction()
    data class PackagesSucess(val data: PackagesResponse) : HomeAction()
    data class CategoriesSucess(val data: CategoriesResponse) : HomeAction()
    data class CategoiesAndServicesSucess(val data: CategoiesAndServicesResponse) : HomeAction()
    data class ServicesByCategory(val data: ServicesResponse) : HomeAction()
    data class AddItemToCart(val data: OtpChangePassswordResponse) : HomeAction()
    data class  ShowCartData(val data : CartResponse): HomeAction ()

    data class ShowOffers(val data: OffersResponse) : HomeAction()


}
