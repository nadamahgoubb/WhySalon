package com.dot_jo.whysalon.ui.fragment.basket

import com.dot_jo.whysalon.base.Action
import com.dot_jo.whysalon.data.response.CartResponse
import com.dot_jo.whysalon.data.response.OtpChangePassswordResponse
import com.dot_jo.whysalon.data.response.ServiceDetails

sealed class BasketAction() : Action {

    data class ShowLoading(val show: Boolean) : BasketAction()
    data class ShowFailureMsg(val message: String?) : BasketAction()

    data class  ShowServiceDetails(val data : ServiceDetails): BasketAction ()
    data class  ShowPackageDetails(val data : com.dot_jo.whysalon.data.response.PackageDetails): BasketAction ()
    data class  ShowCartData(val data :CartResponse): BasketAction ()
  data  class AddItemToCart(val data : OtpChangePassswordResponse) : BasketAction()
  data  class DeleteFromCart(val data : OtpChangePassswordResponse) : BasketAction()

}
