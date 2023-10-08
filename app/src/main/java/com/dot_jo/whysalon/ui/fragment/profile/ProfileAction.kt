package com.dot_jo.whysalon.ui.fragment.profile

import com.dot_jo.whysalon.base.Action
import com.dot_jo.whysalon.data.response.AboutUsResponse
import com.dot_jo.whysalon.data.response.ChangeNotifactionStatus
import com.dot_jo.whysalon.data.response.ContactUsResponse
import com.dot_jo.whysalon.data.response.LoginResponse
import com.dot_jo.whysalon.data.response.NotificationsResponse
import com.dot_jo.whysalon.data.response.OtpChangePassswordResponse
import com.dot_jo.whysalon.data.response.OtpCheckEmailAfterRegisterResponse
import com.dot_jo.whysalon.data.response.PrivacyPolicyResponse
import com.dot_jo.whysalon.ui.fragment.auth.AuthAction

sealed class ProfileAction() : Action {

    data class ShowLoading(val show: Boolean) : ProfileAction()
    data class ShowFailureMsg(val message: String?) : ProfileAction()
    data class ShowFailureUpdatingImage(val message: String?) : ProfileAction()
    data class ShowProfile(val data: LoginResponse) : ProfileAction()
    data class DeleteProfile(val data: OtpChangePassswordResponse) : ProfileAction()
    data class ChangePassword (val data: OtpChangePassswordResponse) :ProfileAction()
   data  class NotifactionChanged(val data: ChangeNotifactionStatus) : ProfileAction()
   data  class getNotifaction(val data: NotificationsResponse) : ProfileAction()
    data  class ShowProfileUpdates(val data : OtpChangePassswordResponse) : ProfileAction()
    data  class ShowPrivacy(val data: PrivacyPolicyResponse) : ProfileAction()
    data  class ShowAboutUs(val data: AboutUsResponse) : ProfileAction()
     data  class ShowContactUsResponse(val data : ContactUsResponse) : ProfileAction()
    data class EmailCheckedAfterRegister(
        val data: OtpCheckEmailAfterRegisterResponse,
    ) : ProfileAction()


}
