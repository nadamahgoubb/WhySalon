package com.dot_jo.whysalon.util.ext

import android.content.Context
import android.widget.Toast


fun Context.showAppToast(message: String?) {
    if (!message.isNullOrBlank())
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()

}
