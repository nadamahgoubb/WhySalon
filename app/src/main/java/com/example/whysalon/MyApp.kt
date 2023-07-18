package com.example.whysalon

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.whysalon.data.PrefsHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp: Application() {


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        PrefsHelper.init(applicationContext)


    }

    /*   override fun attachBaseContext(newBase: Context) {
           val locale = Locale(Constants.EN)
           val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, locale)
           super.attachBaseContext(localeUpdatedContext)
       }*/


}