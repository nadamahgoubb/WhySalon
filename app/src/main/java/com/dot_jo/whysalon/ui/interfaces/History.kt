package com.dot_jo.whysalon.ui.interfaces

import com.dot_jo.whysalon.data.response.BarbarItem
import com.dot_jo.whysalon.data.response.BookingsItem
import com.dot_jo.whysalon.data.response.CartsItemResponse
import com.dot_jo.whysalon.data.response.CategoriesItem
import com.dot_jo.whysalon.data.response.OfferssItem
import com.dot_jo.whysalon.data.response.ServicesItem


interface HomeClickListener {
    fun onCategoryClickListener(item: CategoriesItem)
    fun onPackagesClickListener(item: ServicesItem)

}
interface OffersClickListener {
    fun onOffersClickListener(item: OfferssItem)

}interface BarbarClickListener {
    fun onBarbarClickListener(item: BarbarItem?)

}
interface HistoryClickListener {
    fun onHistoryClickLisenter(id: BookingsItem)

}

interface BasketClickListener {
    fun onDeleteClickLisenter(id: CartsItemResponse)

}

interface FilterTimeClickListener {
    fun onTimeChoosenListener(b: String?)

}
interface FilterOffersByCategoryClickListener {
    fun onFilterOffersByCategory(item: CategoriesItem?)

}
