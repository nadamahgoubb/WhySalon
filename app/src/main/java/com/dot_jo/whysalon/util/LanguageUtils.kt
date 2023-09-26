package com.dot_jo.whysalon.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import com.dot_jo.whysalon.data.PrefsHelper
import com.yariksoffice.lingver.Lingver
import java.util.*
import javax.inject.Singleton

@Singleton
class LanguageUtils (base: Context) : ContextWrapper(base) {

    companion object {

        fun updateLocale(c: Context, localeToSwitchTo: Locale): ContextWrapper {
            var context = c
            val resources: Resources = context.resources
            Lingver.getInstance().setLocale(context, getLocale(PrefsHelper.getLanguage()))
            val configuration: Configuration = resources.configuration
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(localeToSwitchTo)
                LocaleList.setDefault(localeList)
                configuration.setLocales(localeList)
            } else {
                configuration.locale = localeToSwitchTo
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                context = context.createConfigurationContext(configuration)
            } else {
                resources.updateConfiguration(configuration, resources.displayMetrics)
            }
            return LanguageUtils(context)
        }
            fun getLocale(language: String): Locale {
                return if (language == Constants.EN) Locale("en", "US")
                else Locale("ar", "EG")
            }
        }
    }
