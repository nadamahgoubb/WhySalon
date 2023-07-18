package com.example.whysalon.data

import android.content.Context
import android.content.SharedPreferences
 import com.example.whysalon.util.Constants
import com.google.gson.Gson

object PrefsHelper {

    private lateinit var preferences: SharedPreferences
    private const val PREFS_NAME = "shared_prefs"

    fun init(context: Context){
      preferences = context.getSharedPreferences(
          PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun setLanguage(language: String) {
      preferences.edit().putString(Constants.LANG, language).apply()
    }

    fun getLanguage(): String {
        return preferences.getString(Constants.LANG, Constants.EN).toString()
    }

    fun setLoggedBefore (logged: Boolean) {
      preferences.edit().putBoolean(Constants.IS_LOGGED_BEFORE, logged).apply()
    }

    fun isLoggedBefore(): Boolean {
        return preferences.getBoolean(Constants.IS_LOGGED_BEFORE,false)
    }
    fun saveToken(token: String?) {
      preferences.edit().putString(Constants.TOKEN, token).apply()

    }

    fun getToken(): String {
        return preferences.getString(Constants.TOKEN, "").toString()
    }

    fun saveName(name: String?) {
      preferences.edit().putString(Constants.NAME, name).apply()

    }

    fun getName(): String {
        return preferences.getString(Constants.NAME, "").toString()
    }

    fun savePhone(name: String?) {
      preferences.edit().putString(Constants.PHONE, name).apply()

    }

    fun getPhone(): String {
        return preferences.getString(Constants.PHONE, "").toString()
    }



    fun clear()  {
        preferences.edit().putString(Constants.PHONE, "").toString()
      preferences.edit().putString(Constants.NAME, "").apply()
      preferences.edit().putString(Constants.TOKEN, null).apply()
      preferences.edit().putString(Constants.DEAFAULT_ADDRES, null).apply()
    }
    fun clearAddress()  {
      preferences.edit().putString(Constants.DEAFAULT_ADDRES, null).apply()
    }
    fun setFCMToken(token: String) {
      preferences.edit().putString(Constants.FCM_TOKEN, token).apply()
    }
    fun getFcmToken(): String {
        return preferences.getString(Constants.FCM_TOKEN,"").toString()
    }

}