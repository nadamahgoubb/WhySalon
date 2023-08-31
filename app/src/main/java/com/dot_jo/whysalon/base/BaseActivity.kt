package com.dot_jo.whysalon.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableBoolean
import androidx.databinding.ViewDataBinding
 import com.dot_jo.whysalon.util.ext.bindView
 import com.dot_jo.whysalon.util.LanguageUtils
import android.content.Context
import android.content.ContextWrapper
import com.dot_jo.whysalon.data.PrefsHelper
import java.util.Locale

 abstract class BaseActivity<B : ViewDataBinding> : AppCompatActivity() {

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



}