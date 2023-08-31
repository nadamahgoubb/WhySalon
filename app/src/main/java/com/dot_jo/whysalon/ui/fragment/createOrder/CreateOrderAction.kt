package com.dot_jo.whysalon.ui.fragment.createOrder

import com.dot_jo.whysalon.base.Action
import com.dot_jo.whysalon.data.response.BarbarsResponse
import com.dot_jo.whysalon.data.response.OtpChangePassswordResponse
import com.dot_jo.whysalon.data.response.TimesOfBarbarResponse

sealed class CreateOrderAction() : Action {

    data class ShowLoading(val show: Boolean) : CreateOrderAction()
    data class ShowFailureMsg(val message: String?) : CreateOrderAction()

     data class  ShowBarbars(val data : BarbarsResponse): CreateOrderAction ()
    data class  ShowBookingAdded(val data :OtpChangePassswordResponse): CreateOrderAction ()
    data class  ShowTimes(val data: TimesOfBarbarResponse): CreateOrderAction ()

}
