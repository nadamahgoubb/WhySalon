package com.dot_jo.whysalon.ui.adapter.booking

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.BookingsItem
import com.dot_jo.whysalon.databinding.ItemHistoryBinding
import com.dot_jo.whysalon.ui.interfaces.HistoryClickListener
import com.dot_jo.whysalon.util.ext.loadImage
import com.dot_jo.whysalon.util.ext.roundTo
import com.dot_jo.whysalon.util.getDuration

class HistoryAdapter(
    var listener: HistoryClickListener
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {


    var _binding: ItemHistoryBinding? = null
    var list = mutableListOf<BookingsItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        context = parent.context
        _binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (list.size > 1) {

            val layoutParams: ViewGroup.LayoutParams = _binding!!.root.layoutParams
            layoutParams.width = (parent.width * 0.90).toInt()
            _binding!!.root.setLayoutParams(layoutParams)

        }
        return HistoryViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        var currentItem = list[position]
        holder.binding.tvBarberName.setText(currentItem.barber?.name)
        var serviceType = ""
        var image: String? = null
        currentItem.carts.forEach {
            if (it.packagee == null) {
                serviceType = serviceType + it.service?.name +" - "
                image = it.service?.image
            } else {
                serviceType = serviceType + it.packagee?.name+" - "
                image = it.packagee?.image

            }
        }
         holder.binding.tvServicesType.setText(
            context.resources.getString(R.string.service_type) + serviceType.substring(
                0, serviceType.length - 3
            ).toString()
        )
             holder.binding.tvDuration.setText( currentItem.duration+" " +context.resources.getString(R.string.min))

        holder.binding.tvMoney.setText(
            currentItem.final_total_after_discount?.toDoubleOrNull()?.roundTo(2)
                .toString() + context.resources.getString(R.string.sr)
        )
        holder.binding.ivService.loadImage(image)
        holder.binding.ivBarber.loadImage(currentItem.barber?.image)

        holder.binding.lytRebook.setOnClickListener {
            listener.onHistoryClickLisenter(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

}


