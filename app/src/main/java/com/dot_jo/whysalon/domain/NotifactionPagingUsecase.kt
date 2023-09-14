package com.dot_jo.whysalon.domain

import androidx.paging.PagingSource
import com.dot_jo.whysalon.base.BasePagingUseCase
import com.dot_jo.whysalon.base.PagingParams
import com.dot_jo.whysalon.data.Repository
import com.dot_jo.whysalon.data.pagingSource.NotiactionPagingSource
import com.dot_jo.whysalon.data.response.NotificationItem
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class NotifactionPagingUsecase @Inject constructor(private val repo: Repository) :
    BasePagingUseCase<NotificationItem, PagingParams>() {

    override fun getPagingSource(params: PagingParams?): PagingSource<Int, NotificationItem> {
        var res= NotiactionPagingSource(repo, params!!)
        return res    }
}
