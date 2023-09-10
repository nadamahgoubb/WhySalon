package com.dot_jo.whysalon.domain


import com.dot_jo.whysalon.base.BaseUseCase
import com.dot_jo.whysalon.base.DevResponse
import com.dot_jo.whysalon.base.ErrorResponse
import com.dot_jo.whysalon.base.NetworkResponse
import com.dot_jo.whysalon.data.param.AddToCartParams
import com.dot_jo.whysalon.data.param.DeleteFronCartParams
import com.dot_jo.whysalon.data.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class CartUseCase @Inject constructor(private val repository: Repository) :
    BaseUseCase<DevResponse<Any>, Any>() {


    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
        return if (params is AddToCartParams) {
                 flow {
                    emit(repository.addToCart(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>

        } else if (params is DeleteFronCartParams) {
                 flow {
                    emit(repository.deleteCart(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
           } else {
            flow {
                emit(repository.getCart())
            } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
        }

    }
}


