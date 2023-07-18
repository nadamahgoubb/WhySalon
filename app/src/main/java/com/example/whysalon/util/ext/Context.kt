package com.example.whysalon.util.ext

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.example.whysalon.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Int.localize(context: Context): String = try {
    context.resources.getString(this)
} catch (e: Exception) {
    toString()
}

fun View.showKeyboard() = (this.context as? Activity)?.showKeyboard()
fun View.hideKeyboard() = (this.context as? Activity)?.hideKeyboard()

fun Context.showKeyboard() = (this as? Activity)?.showKeyboard()
fun Context.hideKeyboard() = (this as? Activity)?.hideKeyboard()


val Context.actionBarSize
    get() = theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        .let { attrs -> attrs.getDimension(0, 0F).toInt().also { attrs.recycle() } }

val Context.connectivityManager: ConnectivityManager
    get() =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.getPxFromDimenRes(@DimenRes res: Int) = resources.getDimensionPixelSize(res)


fun Context.showConfirmDialog(
    message: String?,
    onYesCallback: () -> Unit = {},
    onNoCallBack: () -> Unit = {}
) {
    MaterialAlertDialogBuilder(this)
        .setMessage(message)
        .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
            dialog.dismiss()
            onYesCallback.invoke()
        }
        .setNegativeButton(getString(R.string.no)) { dialog, _ ->
            dialog.dismiss()
            onNoCallBack.invoke()
        }
        .show()
}

fun Context.showAppToast(message: String?) {
    if (!message.isNullOrBlank())
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

}

fun Context.getDrawableFromRes(@DrawableRes drawable: Int): Drawable? =
    ContextCompat.getDrawable(this, drawable)

fun Context.getAppDrawableFromColor(@ColorRes color: Int): Drawable =
    ColorDrawable(getAppColorFromRes(color))


fun Context.getAppColorFromRes(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}

fun Context.getAppColorStateListFromRes(@ColorRes color: Int): ColorStateList {
    return ColorStateList.valueOf(getAppColorFromRes(color))
}

fun Context.getColorFromRes(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}


