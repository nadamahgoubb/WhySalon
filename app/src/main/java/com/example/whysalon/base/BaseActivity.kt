package com.example.whysalon.base

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
 import com.example.whysalon.util.ext.bindView
 import com.example.whysalon.util.LanguageUtils
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import com.example.whysalon.data.PrefsHelper
import java.util.Locale

 abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {
/*

    override fun attachBaseContext(newBase: Context) {
        // get chosen language from shread preference
        val locale = Locale(PrefsHelper.getLanguage())
        val localeUpdatedContext: ContextWrapper = LanguageUtils.updateLocale(newBase, locale)
        super.attachBaseContext(localeUpdatedContext)


    }*/

    override fun attachBaseContext(newBase: Context) {
        // get chosen language from shread preference
        val locale = Locale(PrefsHelper.getLanguage())
        val localeUpdatedContext: ContextWrapper = LanguageUtils.updateLocale(newBase, locale)
        super.attachBaseContext(localeUpdatedContext)


    }

    val baseShowProgress = ObservableBoolean()

    public override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = bindView()
    }

    public override fun onResume() {
        super.onResume()
    }

    lateinit var binding: B

    fun setOpacityBackground(view: View, @ColorRes color: Int) {
        view.setBackgroundColor(
            try {
                ContextCompat.getColor(this, color)
            } catch (e: Resources.NotFoundException) {
                0
            }
        )
    }


}