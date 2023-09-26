package com.dot_jo.whysalon

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dot_jo.whysalon.data.PrefsHelper
import com.dot_jo.whysalon.util.LanguageUtils.Companion.updateLocale
import com.yariksoffice.lingver.Lingver
import dagger.hilt.android.HiltAndroidApp
import java.util.Locale

@HiltAndroidApp
class MyApp: Application() {


    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        PrefsHelper.init(applicationContext)
        val locale = Locale(PrefsHelper.getLanguage())
        Lingver.init(this, Locale("ar", "EG"))
        updateLocale(applicationContext,locale)

    }

    /*   override fun attachBaseContext(newBase: Context) {
           val locale = Locale(Constants.EN)
           val localeUpdatedContext: ContextWrapper = ContextUtils.updateLocale(newBase, locale)
           super.attachBaseContext(localeUpdatedContext)
       }*/


}