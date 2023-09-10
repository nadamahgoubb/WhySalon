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
class ProfileUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {


        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params == 1) {
                flow {
                    emit(repository.showProfile( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
           else  if (params ==2) {
                flow {
                  emit(repository.deleteAccount( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }   else  if (params ==3) {
                flow {
                  emit(repository.getSetting( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params ==4) {
                flow {
                    emit(repository.getNotifications( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params is EditProfileParam) {
                flow {
                  emit(repository.updateProfile(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params is changePasswordParam) {
                flow {
                    emit(repository.changePassword(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else {
                flow {
                    emit(repository.changeNotifactionStatus( ))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


