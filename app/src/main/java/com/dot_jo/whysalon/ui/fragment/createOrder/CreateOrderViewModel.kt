package com.dot_jo.whysalon.ui.fragment.createOrder


import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseViewModel
import com.dot_jo.whysalon.data.param.AddBookingParams
import com.dot_jo.whysalon.data.param.AddReBookingParams
import com.dot_jo.whysalon.data.param.CheckCuponParams
import com.dot_jo.whysalon.data.param.GetTimesParams
import com.dot_jo.whysalon.data.param.GetTimesReBookingParams
import com.dot_jo.whysalon.data.response.BarbarItem
import com.dot_jo.whysalon.data.response.BarbarsResponse
import com.dot_jo.whysalon.data.response.CuponResponse
import com.dot_jo.whysalon.data.response.OtpChangePassswordResponse
import com.dot_jo.whysalon.data.response.TimesOfBarbarResponse
import com.dot_jo.whysalon.domain.CreateOrderUseCase
import com.dot_jo.whysalon.util.ArabicToEnglish
import com.dot_jo.whysalon.util.NetworkConnectivity
import com.dot_jo.whysalon.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CreateOrderViewModel @Inject constructor(
    app: Application, val useCase: CreateOrderUseCase
) : BaseViewModel<CreateOrderAction>(app) {

    var total: String? = null
    var totalAfterDiscount: String? = null
    var date: String? = null
    var barbar: BarbarItem? = null
    fun addReBooking(
        barber_id: String, date: String, time: String,orderId:String, payment_method: String, discount_code: String?,phone: String?,country_code: String?,) {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {

            produce(CreateOrderAction.ShowLoading(true))
            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, AddReBookingParams(
                        barber_id, date, time,orderId
                    , payment_method , discount_code ,phone  ,country_code )


                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(CreateOrderAction.ShowBookingAdded(res.data.data as OtpChangePassswordResponse))
                        }
                    }
                }
            }
        } else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun addBooking(
        barber_id: String, date: String, time: String, payment_method: String, discount_code: String?,phone: String?,country_code: String?,) {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {

            produce(CreateOrderAction.ShowLoading(true))
            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, AddBookingParams(
                        barber_id, date, time,
                        payment_method , discount_code,phone ,country_code
                    )

                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(CreateOrderAction.ShowBookingAdded(res.data.data as OtpChangePassswordResponse))
                        }
                    }
                }
            }
        } else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun checkCupon(
        code: String ) {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {

            produce(CreateOrderAction.ShowLoading(true))
            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, CheckCuponParams(
                     code
                    )

                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(CreateOrderAction.ShowCuponVaildation(res.data.data as CuponResponse))
                        }
                    }
                }
            }
        } else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun getBarbars(  ) {
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(CreateOrderAction.ShowBarbars(res.data.data as BarbarsResponse))
                        }
                    }
                }
            }
        } else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }

    }
    fun getTimes(date :String) {
        this.date = ArabicToEnglish( date)
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                 useCase.invoke(
                    viewModelScope, barbar?.id?.let { GetTimesParams(it, ArabicToEnglish(date)) }
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(CreateOrderAction.ShowTimes(res.data.data as TimesOfBarbarResponse))
                        }
                    }
                }
            }
        } else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }

    }

    fun getTimesReBooking(date :String,orderId:String?=null) {
        this.date =ArabicToEnglish( date)
        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) }) {
            produce(CreateOrderAction.ShowLoading(true))

            viewModelScope.launch {
                useCase.invoke(
                    viewModelScope, barbar?.id?.let { GetTimesReBookingParams(it, ArabicToEnglish(date),orderId) }
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(CreateOrderAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(CreateOrderAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(CreateOrderAction.ShowTimes(res.data.data as TimesOfBarbarResponse))
                        }
                    }
                }
            }
        } else {
            produce(CreateOrderAction.ShowFailureMsg(getString(R.string.no_internet)))
        }

    }

}
