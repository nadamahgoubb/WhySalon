package com.dot_jo.whysalon.ui.fragment.home

import com.dot_jo.whysalon.base.Action
import com.dot_jo.whysalon.data.response.CategoriesResponse
import com.dot_jo.whysalon.data.response.OffersResponse
import com.dot_jo.whysalon.data.response.PackagesResponse
import com.dot_jo.whysalon.data.response.ServicesResponse

sealed class HomeAction() : Action {

    data class ShowLoading(val show: Boolean) : HomeAction()
    data class ShowFailureMsg(val message: String?) : HomeAction()
    data class PackagesSucess(val data: PackagesResponse) : HomeAction()
    data class CategoriesSucess(val data: CategoriesResponse) :HomeAction()
   data  class ServicesByCategory(val data: ServicesResponse) : HomeAction()

    data  class ShowOffers(val data : OffersResponse): HomeAction  ()


}
