package com.dot_jo.whysalon.ui.interfaces

import com.dot_jo.whysalon.data.response.BookingsItem


interface CancelBookingListener {
    fun onCancelBooking(item: BookingsItem?, position: Int)

}
interface ConfirmCancelBookingListener {
    fun onConfirmBooking( )

}