package com.dot_jo.whysalon.ui.fragment.booking

import com.dot_jo.whysalon.base.Action
import com.dot_jo.whysalon.data.response.BookingResponse
import com.dot_jo.whysalon.data.response.OtpChangePassswordResponse

sealed class BookingAction() : Action {

    data class ShowLoading(val show: Boolean) : BookingAction()
    data class ShowFailureMsg(val message: String?) : BookingAction()

      data class ShowBookingDeleted(val data : OtpChangePassswordResponse): BookingAction ()
      data class ShowBookingList(val data : BookingResponse): BookingAction ()
    data  class ShowHistoryList(val data : BookingResponse) : BookingAction()
}
