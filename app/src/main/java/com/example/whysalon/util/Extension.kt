package com.example.whysalon.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.whysalon.R/*
import com.example.whysalon.data.PrefsHelper
import com.example.whysalon.ui.activity.AuthActivity
import com.example.whysalon.ui.dialog.LoginFirstBotomSheetFragment
import com.example.whysalon.ui.dialog.OnClickLoginFirst*/
import com.google.android.material.snackbar.Snackbar


object Extension {

    fun Fragment.withFragment(dataBinding: ViewDataBinding) {
        dataBinding.lifecycleOwner = this.viewLifecycleOwner
    }

     fun showSnackBar(message: String,activity:Activity) {
        val snackbar = Snackbar.make(
           activity .findViewById<View>(android.R.id.content),
            message, Snackbar.LENGTH_SHORT
        )
        snackbar.show()
    }
    fun tintDrawable(drawable: Drawable?, tint: Int): Drawable? {
        var drawable = drawable
        drawable = DrawableCompat.wrap(drawable!!)
        DrawableCompat.setTint(drawable, tint)
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)
        return drawable
    }/*
    fun showLoginFirstBotomSheetFragment(activity: Activity, childFragmentManager: FragmentManager) {
        LoginFirstBotomSheetFragment.newInstance(object : OnClickLoginFirst {
            override fun onClick(choice: String) {
                if (choice.equals(Constants.YES)) {
                    PrefsHelper.clear()
                    var intent = Intent(activity, AuthActivity::class.java)
                    intent.putExtra(Constants.Start, Constants.login)
                   activity. startActivity(intent)
                    activity?.finish()
                } else {
                }
            }

        }).show(childFragmentManager, LoginFirstBotomSheetFragment::class.java.canonicalName)
    }*/
    fun hideProgressBar(progressBar: ProgressBar) {

        progressBar.visibility = View.INVISIBLE
    }

    fun showProgressBar(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }
    fun ImageView.loadImage(url: String) {
        Glide.with(this)
            .load(url)
            .apply(RequestOptions().placeholder(R.drawable.progress_animation))
            .into(this)
    }


    fun isEmailValid(email: String?): Boolean =
        !email.isNullOrEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
    public  fun chat(context: Context, country_code :String,contact :String) {
        //contact = PrefsHelper.get // use country code with your phone number
        var contactt = country_code + contact

        var  appPackage ="";
        val url = "https://api.whatsapp.com/send?phone=$contactt"
        if (isAppInstalled(context, "com.whatsapp.w4b")) {
            appPackage = "com.whatsapp.w4b";

            val pm: PackageManager = context.getPackageManager()
            pm.getPackageInfo(appPackage, PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)


        } else if (isAppInstalled(context, "com.whatsapp")) {
            appPackage = "com.whatsapp";
            val pm: PackageManager = context.getPackageManager()
            pm.getPackageInfo(appPackage, PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        } else {
            Toast.makeText(context,   context.getString(com.example.whysalon.R.string.whatsup_not_installed),
                Toast.LENGTH_LONG).show();
        }



        try {
            val pm: PackageManager = context.getPackageManager()
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        } catch (e: PackageManager.NameNotFoundException) {
            Toast.makeText(
                context,
                context.getString(com.example.whysalon.R.string.whatsup_not_installed),
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }
    }

    fun isAppInstalled( ctx:Context,  packageName:String) :Boolean{
        var  pm: PackageManager = ctx.getPackageManager();
        var  app_installed= false;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (e: PackageManager.NameNotFoundException  ) {
            app_installed = false;
        }
        return app_installed;
    }

}