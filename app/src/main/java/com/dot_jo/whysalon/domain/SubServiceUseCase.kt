package com.dot_jo.whysalon.domain


import com.dot_jo.whysalon.base.BaseUseCase
import com.dot_jo.whysalon.base.DevResponse
import com.dot_jo.whysalon.base.ErrorResponse
import com.dot_jo.whysalon.base.NetworkResponse
import com.dot_jo.whysalon.data.param.ServicesByCategoryParams
import com.dot_jo.whysalon.data.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class SubServiceUseCase @Inject constructor(private val repository: Repository) :
    BaseUseCase<DevResponse<Any>, Any>() {
    companion object HomeUseCase {
        val packags = 1
        val categories = 2
    }

    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
        return if (params is ServicesByCategoryParams) {
            if (params.type == packags) {
                flow {
                    emit(repository.getPackageDetails(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            } else {
                flow {
                    emit(repository.getServiceDetails(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

            }


        } else {
            flow {
                emit(null)
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        }

    }
}


