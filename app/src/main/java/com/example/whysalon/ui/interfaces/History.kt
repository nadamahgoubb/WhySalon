package com.example.whysalon.ui.interfaces

import com.example.whysalon.data.response.TimeResponse


interface HistoryClickListener {
    fun onHistoryClickListener()

}

interface FilterTimeClickListener {
    fun onTimeChoosenListener(b: Boolean)

}
interface RatingResaonsClickListener {
    fun onRatingResaonsListener(b: ArrayList<TimeResponse>)

}