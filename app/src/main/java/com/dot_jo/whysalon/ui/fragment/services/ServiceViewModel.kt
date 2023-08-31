package com.dot_jo.whysalon.ui.fragment.services


import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseViewModel
import com.dot_jo.whysalon.data.response.CategoriesResponse
import com.dot_jo.whysalon.data.response.PackagesResponse
import com.dot_jo.whysalon.domain.HomeUseCase
import com.dot_jo.whysalon.util.NetworkConnectivity
import com.dot_jo.whysalon.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ServiceViewModel @Inject constructor(app: Application, val useCase: HomeUseCase) :
    BaseViewModel<ServiceAction>(app) {

      fun getCategory( ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ServiceAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(ServiceAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(ServiceAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(   ServiceAction.CategoriesSucess(res.data.data as CategoriesResponse))
                         }
                    }
                }
            }
        } else {
            produce(ServiceAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun getPackages( ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(ServiceAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, HomeUseCase.packags
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(ServiceAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(ServiceAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(   ServiceAction.PackagesSucess(res.data.data as PackagesResponse))
                         }
                    }
                }
            }
        } else {
            produce(ServiceAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
}
