package com.dot_jo.whysalon.domain


import com.dot_jo.whysalon.base.BaseUseCase
import com.dot_jo.whysalon.base.DevResponse
import com.dot_jo.whysalon.base.ErrorResponse
import com.dot_jo.whysalon.base.NetworkResponse
import com.dot_jo.whysalon.data.param.CheckEmailParam
import com.dot_jo.whysalon.data.param.LoginParams
import com.dot_jo.whysalon.data.param.RegisterParams
import com.dot_jo.whysalon.data.param.ResetPasswordParams
import com.dot_jo.whysalon.data.Repository
import com.dot_jo.whysalon.data.param.CheckOtpWithEmailParam
import com.dot_jo.whysalon.data.param.loginbyGoogleParams
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class AuthUseCase @Inject constructor(private val repository: Repository):
    BaseUseCase<DevResponse<Any>, Any>() {


        override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
            return if (params is LoginParams) {
                flow {
                    emit(repository.login(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
           else  if (params is loginbyGoogleParams) {
                flow {
                  emit(repository.loginbyGoogle(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }   else  if (params is ResetPasswordParams) {
                flow {
                  emit(repository.resetpassword(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else  if (params is CheckEmailParam) {
                if(params.inregister){
                    flow {
                        emit(repository.checkMailInRegisteration(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }else{
                    flow {
                        emit(repository.checkEmail(params))
                    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
                }

            }
            else  if (params is CheckOtpWithEmailParam) {
                flow {
                    emit(repository.checkOtp(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }else  if (params is RegisterParams) {
                flow {
                    emit(repository.register(params))
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }
            else {
                flow {
                    emit(repository.registerGuest())
                } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>
            }

        }
    }


