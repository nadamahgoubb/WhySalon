package com.example.whysalon.util

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.whysalon.R

object BundleUtil {
    const val TAG = "BundleUtil"

    //safe get data from bundle
    inline fun <reified T> Bundle?.getMyData(key: String): T? {

        if (this == null) {
             return null
        }
        return if (containsKey(key)) {
            try {
                get(key) as T
            } catch (e: Exception) {
                 null
            }
        } else {
             null
        }
    }

    inline fun <reified T> Intent?.getMyData(key: String): T? {
        if (this == null) {
             return null
        }
        return if (hasExtra(key)) {
            try {
                extras!!.get(key) as T
            } catch (e: Exception) {
                 null
            }
        } else {
             null
        }
    }

}