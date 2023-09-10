package com.dot_jo.whysalon.domain


import com.dot_jo.whysalon.base.BaseUseCase
import com.dot_jo.whysalon.base.DevResponse
import com.dot_jo.whysalon.base.ErrorResponse
import com.dot_jo.whysalon.base.NetworkResponse
import com.dot_jo.whysalon.data.param.OffersParam
import com.dot_jo.whysalon.data.param.ServicesByCategoryParams
import com.dot_jo.whysalon.data.param.UpdateFcmTokenParam
import com.dot_jo.whysalon.data.Repository
import com.dot_jo.whysalon.data.param.AddToCartParams
import com.dot_jo.whysalon.data.param.RateParam
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class HomeUseCase @Inject constructor(private val repository: Repository) :
    BaseUseCase<DevResponse<Any>, Any>() {
    companion object HomeUseCase {
        val packags = 1
        val categories = 2
    }

    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
        return if (params is ServicesByCategoryParams) {
            flow {
                emit(repository.getServicesInCategory(params))
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else if (params is UpdateFcmTokenParam) {
            flow {
                emit(repository.updateFcmToken(params))
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

        } else if (params is RateParam) {
            flow {
                emit(repository.rate(params))
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

        } else if (params == packags) {
            flow {
                emit(repository.getPackages())
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else if (params is OffersParam) {
            flow {
                emit(repository.getOffers(params))
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else if (params is AddToCartParams) {
            flow {
                emit(repository.addToCart(params))
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        } else {
            flow {
                emit(repository.getCategories())
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        }

    }
}


