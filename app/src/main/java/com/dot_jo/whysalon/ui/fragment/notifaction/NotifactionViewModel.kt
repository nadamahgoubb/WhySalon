package com.dot_jo.whysalon.ui.fragment.notifaction

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.base.BaseViewModel
import com.dot_jo.whysalon.base.PagingParams
import com.dot_jo.whysalon.domain.NotifactionPagingUsecase
import com.dot_jo.whysalon.util.NetworkConnectivity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotifactionViewModel @Inject constructor(app: Application, val userCase: NotifactionPagingUsecase) :
    BaseViewModel<NotifactionAction>(app) {
 fun getNotifaction() {

        if (app?.let { it1 -> NetworkConnectivity.hasInternetConnection(it1) } == true) {
            produce(NotifactionAction.ShowLoading(true))
            userCase.invoke(
                viewModelScope, PagingParams()
            ) { data ->
                viewModelScope.launch {
                    produce(NotifactionAction.ShowAllNotifaction(data))
                }
            }
        } else {
            produce(NotifactionAction.ShowFailureMsg(getString(R.string.no_internet)))
        }
    }
}
