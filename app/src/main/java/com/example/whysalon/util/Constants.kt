package com.example.whysalon.util

import android.graphics.Color

object Constants {

   const val Notifaction: String= "notifaction"
    const val PayfortMerchantIdentifier ="00f1ca9c"
  const  val PayfortRequestPhrase ="46PXPeVmlpRrScEAVgVV9U)]"
  const  val PayfortAccessCode = "sAlBf91MyKXqrenFFaiR"
  const val ENCREPT_FINGER_PRINT: String ="EMAIL"
  /*const val PASS: String ="PASS"
  const val COUNTRY_CODE: String ="country_code"*/
    val Start: String = "START"
    const val splash = 0
    const val login = 1
    const val Otp = 2
    const val Register = 3
  const val walk_throught = 4
  const val LANDRY = "Landry"
    const val ORDER_ID = "order_id"
    const val ORDER  = "order"
    const val ORDER_ID_PREVIOUS = "order_id_previous"
    const val ORDER_ID_TO_RATE = "order_id_to_rate"
    const val PAYMENT = "Payment"
    const val PRCIE = "prcie"
    const val LAUNDRY_DATA = "laundry_data"
    const val LAUNDRY_RESULT_SEARCH = "laundry_result_search"
  //all laundries screen
    const val ALL_LAUNDRY_SCREEN = "all_laundry_screen" // key
    // value
    const val ALL_LAUNDRY = "all_laundry_"
    const val TOP_LAUNDRIES = "top_laundries"
    const val SEARCH = "search"
  const val QR_CODE: String ="qr_code"


    const val BASE_URL = "https://yourapi.com/api/"
    const val DATA_STORE_NAME = "my_datastore"
    const val ROOM_DATA_BASE_NAME = "my_datastore"
    const val IMAGE: String = "image"
    const val SERVICE: String = "service"
    const val MSG: String = "message"
    const val LANG = "lang"
    const val TOKEN = "token"
    const val DEAFAULT_ADDRES = "deafault_addres"
    const val NAME = "name"
    const val PHONE = "phone"
    const val EMAIL = "email"
    const val FCM_TOKEN = "fcm_token"
    const val IS_LOGGED_BEFORE = "is_looged_before"
    const val EN = "en"
    const val DEFAULT_LANG: String = EN
    const val AR = "ar"
    const val YES = "yes"
    const val NO = "no"
    const val self_delivery = "self_delivery"
    const val bydelivery = "bydelivery"
    const val home_drop_of = "home_drop_of"
    const val on_way_delivery = "on_way_delivery"
    const val self_drop_of = "self_drop_of"

    // in case on_way_delivery


    const val sub_home_drop_of = "home_drop_of"
    const val sub_self_drop_of = "self_drop_of"

  //payment
  const val wallet = 1
    const val cash = 2
    const val credit = 3

    // scan to recive
    const val drop_of_home = "drop_of_home"

    //
    const val UnAuth = "1"
    const val Authorized = "2"
    const val OutOfRange = "3"
  //  const val UnAuthWithAddress = "4"
    const val UnAuthWithoutAddress = "5"

    // home page un auth >> get loaction from
    const val GET_LOACTION_FROM_MAP = "1"
    const val GET_CURRENT_LOACTION = "2"

    // ADD ADDRESS
    const    val ADD_ADDREESS: String ="add_addreess"
    const    val  ADDREESS_UPDATEED: String ="addreess_updateed"
    const val ADD_CURRENT_LOCATION = "add_current_location"
  const    val Test_Environment_URL: String ="https://sbpaymentservices.payfort.com/FortAPI/paymentApi"
  const    val  Production_Environment_URL: String ="https://paymentservices.payfort.com/FortAPI/paymentApi"

// order status

  const val no_progress = "no_progress"
  const val waiting_for_driver = "no_progress"
  const val waiting_for_driver_confirmation = "waiting for driver confirmation"
  const val accepted = "accepted" //---------> order status , driver data
  const val indelivery = "indelivery"
  const val inprogress = "inprogress"
  const val completed = "completed"

   const val CHANNEL_Name = "LocationServiceTracking"

//
const val OPEN_LAUNDRY_FROM_HOME = "1"
const val OPEN_LAUNDRY_FROM_CREATE_ORDER = "2"


}