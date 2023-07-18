package com.example.whysalon.util

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.whysalon.util.Constants
import com.example.whysalon.R
import com.example.whysalon.util.Resource.Companion.loading
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import java.io.IOException
import java.lang.Math.toRadians
import java.util.*
import kotlin.coroutines.CoroutineContext

fun getAddress(context: Context, lat: Double, lng: Double): String? {
    var latitude = lat
    var longitude = lng
    if (lat>90 || lat<-90){
        latitude = 31.5
    }
    if (lng>180 || lng<-180){
        longitude = 31.1
    }
    val geocoder = Geocoder(context, Locale.getDefault())

    return try {
        val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude,1) as List<Address>
        if(addresses.isNotEmpty()) {
            val obj: Address = addresses[0]
            val governorate = obj.adminArea
            val region = obj.subAdminArea
            val add: String = obj.getAddressLine(0)
            Log.v("IGA", "Address$add")
            "$region - $governorate"

        }else{
            ""
        }
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        null
    }
}

fun getDistanceBetweenLocation(StartP: LatLng, EndP: LatLng) :Float{
    val radius = 6371 // radius of earth in Km
    val lat1: Double = StartP.latitude
    val lat2: Double = EndP.latitude
    val lon1: Double = StartP.longitude
    val lon2: Double = EndP.longitude
    val dLat = toRadians(lat2 - lat1)
    val dLon = toRadians(lon2 - lon1)
    Log.d("7imaZzz", "lat1: $lat1 \nlat2: $lat2 \n lng1: $lon1 \n lng2: $lon2")
    val a = (kotlin.math.sin(dLat / 2) * kotlin.math.sin(dLat / 2)
            + (kotlin.math.cos(toRadians(lat1))
            * kotlin.math.cos(toRadians(lat2)) * kotlin.math.sin(dLon / 2)
            * kotlin.math.sin(dLon / 2)))
    val c = 2 * kotlin.math.asin(kotlin.math.sqrt(a))
    Log.d("7imaZzz",
        "getDistanceBetweenLocation: ${String.format(Locale(Constants.EN), "%.2f", radius * c)}")
    return String.format(Locale(Constants.EN), "%.2f", radius * c).toFloat()
}

var job: Job? = null
fun getAddressForTextView(
    context: Context,
    latitude: Double,
    longitude: Double,
    tvAddress: TextView? = null,
    etAddress: EditText? = null
){
    job?.cancel()

    job  = object : CoroutineScope {

        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main // to run code in Main(UI) Thread

        // call this method to cancel a coroutine when you don't need it anymore,
        // e.g. when user closes the screen

        fun execute() = launch {
            onPreExecute()
            val result = doInBackground() // runs in background thread without blocking the Main Thread
            onPostExecute(result)
        }

        private suspend fun doInBackground(): Address? = withContext(Dispatchers.IO) { // to run code in Background Thread
            Log.d("7imaZz", "doInBackground: background")
            val geocoder = Geocoder(context, Locale.getDefault())
            var address: Address? = null
            try {
                runCatching {
                    val list = geocoder.getFromLocation(latitude, longitude, 1)
                    if (list != null && list.size > 0) {
                        address = list[0]
                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }
            return@withContext address
        }

        // Runs on the Main(UI) Thread
        private fun onPreExecute() {
            Log.d("7imaZz", "onPreExecute: loading")
            if (tvAddress!=null)
                tvAddress.text = context.getText(R.string.loading)
            etAddress?.setText(context.getText(R.string.loading))
        }

        // Runs on the Main(UI) Thread
        @SuppressLint("SetTextI18n")
        private fun onPostExecute(address: Address?) {
            Log.d("7imaZz", "onPostExecute: post")
            // hide progress
            if (address != null) {
                // if throufare   get sub threofare
                val wholeAddress = address.getAddressLine(0)
                val governorate = address.adminArea
                val region = address.subAdminArea
                val city = if (address.locality != null) address.locality else ""
                //                    tvAddress.setText(wholeAddress);
                Log.d("7imaZz", "onPostExecute: $wholeAddress")
                if (tvAddress!=null)
                    tvAddress.text = "$city - $region - $governorate"
                etAddress?.setText("$city - $region - $governorate")
            } else {
                if (tvAddress!=null)
                    tvAddress.text = context.getText(R.string.error)
                etAddress?.setText(context.getText(R.string.error))
            }
            job?.cancel()
        }
    }.execute()
}

