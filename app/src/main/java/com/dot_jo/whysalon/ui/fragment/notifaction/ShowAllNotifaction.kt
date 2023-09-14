package com.dot_jo.whysalon.ui.fragment.notifaction

import androidx.paging.PagingData
import com.dot_jo.whysalon.base.Action
import com.dot_jo.whysalon.data.response.NotificationItem

sealed class NotifactionAction() : Action {

    data class ShowLoading(val show: Boolean) : NotifactionAction()
    data class ShowFailureMsg(val message: String?) : NotifactionAction()

    data class ShowAllNotifaction(val data: PagingData<NotificationItem>): NotifactionAction ()


}
