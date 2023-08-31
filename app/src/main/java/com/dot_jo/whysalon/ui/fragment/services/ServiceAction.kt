package com.dot_jo.whysalon.ui.fragment.services

import com.dot_jo.whysalon.base.Action
import com.dot_jo.whysalon.data.response.CategoriesResponse
import com.dot_jo.whysalon.data.response.PackagesResponse

sealed class ServiceAction() : Action {

    data class ShowLoading(val show: Boolean) : ServiceAction()
    data class ShowFailureMsg(val message: String?) : ServiceAction()
    data class PackagesSucess(val data: PackagesResponse) : ServiceAction()
    data class CategoriesSucess(val data: CategoriesResponse) :ServiceAction()

}
