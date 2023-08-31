package com.dot_jo.whysalon.ui.fragment.booking


import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseViewModel
import com.dot_jo.whysalon.data.param.DeleteFromBookingParam
import com.dot_jo.whysalon.data.response.BookingResponse
import com.dot_jo.whysalon.data.response.OtpChangePassswordResponse
import com.dot_jo.whysalon.domain.BookingUseCase
import com.dot_jo.whysalon.util.NetworkConnectivity
import com.dot_jo.whysalon.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class BookingViewModel @Inject constructor(
    app: Application, val useCase: BookingUseCase
) : BaseViewModel<BookingAction>(app) {


    fun getBookingList(
       ) {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {

            produce(BookingAction.ShowLoading(true))
            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,

                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(BookingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(BookingAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(BookingAction.ShowBookingList(res.data.data as BookingResponse))
                        }
                    }
                }
            }
        } else {
            produce(BookingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun getHistoryList(
       ) {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {

            produce(BookingAction.ShowLoading(true))
            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope, BookingUseCase.history

                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(BookingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(BookingAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(BookingAction.ShowHistoryList(res.data.data as BookingResponse))
                        }
                    }
                }
            }
        } else {
            produce(BookingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
    fun deleteBookingItem(orderId: String) {

        if (app.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {

            produce(BookingAction.ShowLoading(true))
            viewModelScope.launch {
                var res = useCase.invoke(
                    viewModelScope,DeleteFromBookingParam(orderId)
                ) { res ->
                    when (res) {
                        is Resource.Failure -> produce(BookingAction.ShowFailureMsg(res.message.toString()))
                        is Resource.Progress -> produce(BookingAction.ShowLoading(res.loading))
                        is Resource.Success -> {

                            produce(BookingAction.ShowBookingDeleted(res.data.data as OtpChangePassswordResponse))
                        }
                    }
                }
            }
        } else {
            produce(BookingAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }


}
