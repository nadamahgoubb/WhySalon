package com.dot_jo.whysalon.domain


import com.dot_jo.whysalon.base.BaseUseCase
import com.dot_jo.whysalon.base.DevResponse
import com.dot_jo.whysalon.base.ErrorResponse
import com.dot_jo.whysalon.base.NetworkResponse
import com.dot_jo.whysalon.data.param.AddBookingParams
import com.dot_jo.whysalon.data.param.GetTimesParams
import com.dot_jo.whysalon.data.Repository
import com.dot_jo.whysalon.data.param.AddReBookingParams
import com.dot_jo.whysalon.data.param.CheckCuponParams
import com.dot_jo.whysalon.data.param.GetTimesReBookingParams
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class CreateOrderUseCase @Inject constructor(private val repository: Repository) :
    BaseUseCase<DevResponse<Any>, Any>() {


    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
        return when (params) {
            is AddBookingParams -> {
                flow {
                    emit(repository.addBooking(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

            }
            is CheckCuponParams -> {
                flow {
                    emit(repository.checkCupon(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

            }
            is AddReBookingParams -> {
                flow {
                    emit(repository.addReBooking(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

            }

            is GetTimesParams -> {
                flow {
                    emit(repository.getTimesByBarbarID(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            is GetTimesReBookingParams -> {
                flow {
                    emit(repository.getTimesByBarbarIDReBooking(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

            else -> {
                flow {
                    emit(repository.getBarbar())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
        }

    }
}


