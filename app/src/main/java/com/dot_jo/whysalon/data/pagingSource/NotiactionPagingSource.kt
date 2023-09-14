package com.dot_jo.whysalon.data.pagingSource

import com.dot_jo.whysalon.base.BasePagingDataSource
import com.dot_jo.whysalon.base.BasePagingResponse
import com.dot_jo.whysalon.base.ErrorResponse
import com.dot_jo.whysalon.base.NetworkResponse
import com.dot_jo.whysalon.base.PagingParams
import com.dot_jo.whysalon.data.Repository
import com.dot_jo.whysalon.data.response.NotificationItem


class NotiactionPagingSource(private val repo: Repository,
                             private val params: PagingParams
) : BasePagingDataSource<NotificationItem>() {


    override val queryParams: PagingParams = params

    override suspend fun execute(): NetworkResponse<BasePagingResponse<NotificationItem>, ErrorResponse> {
        var data =repo.getNotifications(params )
        return data
    }

}