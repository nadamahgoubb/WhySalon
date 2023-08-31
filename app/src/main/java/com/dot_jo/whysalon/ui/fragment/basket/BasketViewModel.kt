package com.dot_jo.whysalon.ui.fragment.basket


import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseViewModel
import com.dot_jo.whysalon.data.param.AddToCartParams
import com.dot_jo.whysalon.data.param.DeleteFronCartParams
import com.dot_jo.whysalon.data.param.ServicesByCategoryParams
import com.dot_jo.whysalon.data.response.CartResponse
import com.dot_jo.whysalon.data.response.OtpChangePassswordResponse
import com.dot_jo.whysalon.data.response.PackageDetails
import com.dot_jo.whysalon.data.response.ServiceDetails
import com.dot_jo.whysalon.domain.CartUseCase
import com.dot_jo.whysalon.domain.HomeUseCase
import com.dot_jo.whysalon.domain.SubServiceUseCase
import com.dot_jo.whysalon.util.NetworkConnectivity
import com.dot_jo.whysalon.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BasketViewModel @Inject constructor(app: Application, val useCase: SubServiceUseCase ,val useCaseCart: CartUseCase) :
    BaseViewModel<BasketAction>(app) {

    fun getServiceDetails(service_id :String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(BasketAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,ServicesByCategoryParams(service_id,HomeUseCase.categories)

                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(BasketAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(BasketAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(BasketAction.ShowServiceDetails(res.data.data as ServiceDetails))
                        }
                    }
                }
            }
        } else {
            produce(BasketAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }

    fun getPackagesDetails(packageid :String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(BasketAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, ServicesByCategoryParams(packageid,HomeUseCase.packags)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(BasketAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(BasketAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(BasketAction.ShowPackageDetails(res.data.data as PackageDetails))
                        }
                    }
                }
            }
        } else {
            produce(BasketAction.ShowFailureMsg(getString(R.string.no_internet)))
        }

    }
    fun getCart( ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(BasketAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseCart.invoke(
                    viewModelScope,        ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(BasketAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(BasketAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(BasketAction.ShowCartData(res.data.data as CartResponse))
                        }
                    }
                }
            }
        } else {
            produce(BasketAction.ShowFailureMsg(getString(R.string.no_internet)))
        }

    }  fun addToBasket(packageid: String?,service_id: String?, price : String) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(BasketAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseCart.invoke(
                    viewModelScope,  AddToCartParams(packageid, service_id,price )      ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(BasketAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(BasketAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(BasketAction.AddItemToCart(res.data.data as OtpChangePassswordResponse))
                        }
                    }
                }
            }
        } else {
            produce(BasketAction.ShowFailureMsg(getString(R.string.no_internet)))
        }

    } fun deleteFromBasket(packageid: String ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(BasketAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCaseCart.invoke(
                    viewModelScope,  DeleteFronCartParams(packageid  )      ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(BasketAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(BasketAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(BasketAction.DeleteFromCart(res.data.data as OtpChangePassswordResponse))
                        }
                    }
                }
            }
        } else {
            produce(BasketAction.ShowFailureMsg(getString(R.string.no_internet)))
        }

    }
}
