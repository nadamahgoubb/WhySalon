package com.dot_jo.whysalon.ui.adapter.booking

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.BookingsItem
import com.dot_jo.whysalon.databinding.ItemBookingBinding
import com.dot_jo.whysalon.ui.interfaces.CancelBookingListener
import com.dot_jo.whysalon.util.convertPttern
import com.dot_jo.whysalon.util.ext.loadImage
import com.dot_jo.whysalon.util.ext.roundTo
import com.dot_jo.whysalon.util.getDuration


class BookingAdapter(
    var listener: CancelBookingListener
) : RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {


    var _binding: ItemBookingBinding? = null
    var list = mutableListOf<BookingsItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        context = parent.context
        _binding = ItemBookingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookingViewHolder(_binding!!)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {

        var currentItem = list[position]
        holder.binding.tvBarberName.setText(currentItem.barber?.name)
        holder.binding.tvCancel.isVisible = currentItem.status ==0
        var serviceType = ""
        var image: String? = null
        currentItem.carts.forEach {
            if (it.packagee == null) {
                serviceType = serviceType + it.service?.name +" - "
                image = it.service?.image
            } else {
                serviceType = serviceType + it.packagee?.name +" - "
                image = it.packagee?.image

            }
        }
       // holder.binding.tvServicesType.setText(context.resources.getString(R.string.service_type) + serviceType)
        holder.binding.tvServicesType.setText(
            context.resources.getString(R.string.service_type) + serviceType.substring(
                0, serviceType.length - 3
            ).toString()
        )
        currentItem.date?.let {
            holder.binding.tvDate.setText(
                context.resources.getString(R.string.date_) + convertPttern(
                    it, "dd MMMM"
                ) + " - " + currentItem.from
            )
        }
        holder.binding.tvMoney.setText(
            currentItem.final_total_after_discount?.toDoubleOrNull()?.roundTo(2)
                .toString() + context.resources.getString(R.string.sr)
        )
        currentItem.duration?.let {
            holder.binding.tvTiming.setText(it+" " +context.resources.getString(R.string.min))
                //getDuration(it, context))
        }
        holder.binding.ivService.loadImage(image)
        holder.binding.ivBarber.loadImage(currentItem.barber?.image)

        holder.binding.tvCancel.setOnClickListener {
            listener.onCancelBooking(currentItem, position)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(item: BookingsItem) {
        item?.let {
            list.remove(it)
            notifyDataSetChanged()
            if (list.size == 0) listener.onCancelBooking(null, -1)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class BookingViewHolder(val binding: ItemBookingBinding) :
        RecyclerView.ViewHolder(binding.root)

}


