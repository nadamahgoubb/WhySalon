package com.dot_jo.whysalon.fcm

import com.dot_jo.whysalon.base.BaseUseCase
import com.dot_jo.whysalon.base.DevResponse
import com.dot_jo.whysalon.base.ErrorResponse
import com.dot_jo.whysalon.base.NetworkResponse
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.data.Repository
import com.dot_jo.whysalon.data.param.UpdateFcmTokenParam
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FcmUseCase @Inject constructor(
    val repo: Repository
)  : BaseUseCase<DevResponse<Any>, Any>() {

    fun generateFcmToken(callBack: (String?) -> Unit = {}) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            PrefsHelper.setFCMToken(token)
            sendFcmTokenToServer(UpdateFcmTokenParam(token,0,PrefsHelper.getLanguage()))
        })
    }

    fun generateFcmTokenIfNotExist() {

        if (PrefsHelper.getFcmToken().isNullOrBlank()) {
            generateFcmToken()
        }
    }
    fun sendFcmTokenToServer(params: UpdateFcmTokenParam) {
        invoke(CoroutineScope(Dispatchers.IO), params = params)
    }
    override fun executeRemote(params: Any?): Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>> {
        return flow {
            emit(repo.updateFcmToken(params  as UpdateFcmTokenParam) )

    } as Flow<NetworkResponse<DevResponse<Any>, ErrorResponse>>


    }
}