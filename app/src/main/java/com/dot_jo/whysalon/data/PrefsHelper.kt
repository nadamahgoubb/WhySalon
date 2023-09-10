package com.dot_jo.whysalon.data

import android.content.Context
import android.content.SharedPreferences
import com.dot_jo.whysalon.data.response.LoginResponse
import com.dot_jo.whysalon.util.Constants
import com.google.gson.Gson

object PrefsHelper {

    private lateinit var preferences: SharedPreferences
    private const val PREFS_NAME = "shared_prefs"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(
            PREFS_NAME, Context.MODE_PRIVATE
        )
    }

    fun setLanguage(language: String) {
        preferences.edit().putString(Constants.LANG, language).apply()
    }

    fun getLanguage(): String {
        return preferences.getString(Constants.LANG, Constants.EN).toString()
    }

    fun setLoggedBefore(logged: Boolean) {
        preferences.edit().putBoolean(Constants.IS_LOGGED_BEFORE, logged).apply()
    }

    fun isLoggedBefore(): Boolean {
        return preferences.getBoolean(Constants.IS_LOGGED_BEFORE, false)
    }

    fun saveToken(token: String?) {
        preferences.edit().putString(Constants.TOKEN, token).apply()

    }

    fun getToken(): String? {
        return preferences.getString(Constants.TOKEN, "")
    }


    fun clear() {
        saveUserData(null)
        preferences.edit().putString(Constants.PHONE, "").toString()
        preferences.edit().putString(Constants.NAME, "").apply()
        preferences.edit().putString(Constants.TOKEN, "").apply()
        preferences.edit().putString(Constants.DEAFAULT_ADDRES, "").apply()
    }

    fun setFCMToken(token: String) {
        preferences.edit().putString(Constants.FCM_TOKEN, token).apply()
    }

    fun getFcmToken(): String {
        return preferences.getString(Constants.FCM_TOKEN, "").toString()
    }

    fun saveUserData(user: LoginResponse?) {
        //set variables of 'myObject', etc.

        var prefsEditor = preferences.edit()
        var gson = Gson()
        //  String
        var json = gson.toJson(user);
        prefsEditor.putString(Constants.USER, json);
        prefsEditor.commit();
    }

    fun getUserData(): LoginResponse? {
        //set variables of 'myObject', etc.

        val gson = Gson()
        val json: String? = preferences.getString(Constants.USER, "")
        return gson.fromJson(json, LoginResponse::class.java)
    }

}