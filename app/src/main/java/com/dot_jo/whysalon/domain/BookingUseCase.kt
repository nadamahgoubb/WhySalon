package com.dot_jo.whysalon.domain


import com.dot_jo.whysalon.base.BaseUseCase
import com.dot_jo.whysalon.base.DevResponse
import com.dot_jo.whysalon.base.ErrorResponse
import com.dot_jo.whysalon.base.NetworkResponse
  import com.dot_jo.whysalon.data.param.DeleteFromBookingParam
import com.dot_jo.whysalon.data.webService.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class BookingUseCase @Inject constructor(private val repository: Repository) :
    BaseUseCase<DevResponse<Any>, Any>() {

companion object {
    val history = 1
}
    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
        return if (params is DeleteFromBookingParam) {
                 flow {
                    emit(repository.deleteBooking(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

        }   else if (params ==history) {
                 flow {
                    emit(repository.getHistory())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

        }   else {
            flow {
                emit(repository.getBooking())
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        }

    }
}


