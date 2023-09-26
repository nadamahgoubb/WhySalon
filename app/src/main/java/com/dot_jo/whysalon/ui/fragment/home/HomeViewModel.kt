package com.dot_jo.whysalon.ui.fragment.home


import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseViewModel
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.data.param.AddToCartParams
import com.dot_jo.whysalon.data.param.OffersParam
import com.dot_jo.whysalon.data.param.RateParam
import com.dot_jo.whysalon.data.param.ServicesByCategoryParams
import com.dot_jo.whysalon.data.param.UpdateFcmTokenParam
import com.dot_jo.whysalon.data.response.CartResponse
import com.dot_jo.whysalon.data.response.CategoriesResponse
import com.dot_jo.whysalon.data.response.OffersResponse
import com.dot_jo.whysalon.data.response.OtpChangePassswordResponse
import com.dot_jo.whysalon.data.response.PackagesResponse
import com.dot_jo.whysalon.data.response.ServicesResponse
import com.dot_jo.whysalon.domain.CartUseCase
import com.dot_jo.whysalon.domain.HomeUseCase
import com.dot_jo.whysalon.util.NetworkConnectivity
import com.dot_jo.whysalon.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(app: Application, val useCase: HomeUseCase , val useCaseCart: CartUseCase) :
    BaseViewModel<HomeAction>(app) {

    init {
        updateFcmToken()
    }

    private fun updateFcmToken() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,
                    UpdateFcmTokenParam(PrefsHelper.getFcmToken(), 0, PrefsHelper.getLanguage())
                ) { res ->
                    when (res) {
                        is Resource.Failure -> {} //produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(HomeAction.UpdateFcmToken(true))
                        }
                    }
                }
            }
        } else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun rateBarber(rate: String, comment: String, barberId: String, orderId: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,
                    RateParam(rate, comment, orderId, barberId)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {
                            produce(HomeAction.Rate(res.data.data as OtpChangePassswordResponse))
                        }
                    }
                }
            }
        } else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getCategory() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(HomeAction.CategoriesSucess(res.data.data as CategoriesResponse))
                        }
                    }
                }
            }
        } else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun getCart( ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseCart.invoke(
                    viewModelScope,        ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(HomeAction.ShowCartData(res.data.data as CartResponse))
                        }
                    }
                }
            }
        } else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }

    }

    fun getPackages() {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, HomeUseCase.packags
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(HomeAction.PackagesSucess(res.data.data as PackagesResponse))
                        }
                    }
                }
            }
        } else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getServicesInCategory(categoryId: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, ServicesByCategoryParams(id = categoryId)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(HomeAction.ServicesByCategory(res.data.data as ServicesResponse))
                        }

                    }
                }
            }
        } else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getOffers(categoryId: String = "") {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, OffersParam(categoryId)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(HomeAction.ShowOffers(res.data.data as OffersResponse))
                        }

                    }
                }
            }
        } else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun addToBasket(packageid: String?, service_id: String?, price: String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(HomeAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, AddToCartParams(packageid, service_id, price)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(HomeAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(HomeAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(HomeAction.AddItemToCart(res.data.data as OtpChangePassswordResponse))
                        }
                    }
                }
            }
        } else {
            produce(HomeAction.ShowFailureMsg(getString(R.string.no_internet)))
        }

    }
}
