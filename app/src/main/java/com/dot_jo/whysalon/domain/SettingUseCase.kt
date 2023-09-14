package com.dot_jo.whysalon.domain


import com.dot_jo.whysalon.base.BaseUseCase
import com.dot_jo.whysalon.base.DevResponse
import com.dot_jo.whysalon.base.ErrorResponse
import com.dot_jo.whysalon.base.NetworkResponse
import com.dot_jo.whysalon.data.param.EditProfileParam
import com.dot_jo.whysalon.data.param.changePasswordParam
import com.dot_jo.whysalon.data.Repository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class SettingUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {


        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return      if (params ==3) {
                flow {
                  emit(repository.getSetting( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }


            else {
                flow {
                    emit(repository.getContactUsData( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


