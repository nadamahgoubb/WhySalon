package com.dot_jo.whysalon.ui.adapter.services

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.ServicesItem
import com.dot_jo.whysalon.databinding.ItemServiceDetailsBinding
import com.dot_jo.whysalon.ui.interfaces.HomeClickListener
import com.dot_jo.whysalon.util.ext.loadImage
import com.dot_jo.whysalon.util.ext.roundTo
import com.dot_jo.whysalon.util.getDuration


class SubServicesAdapter(
    var listener: HomeClickListener
) : RecyclerView.Adapter<SubServicesAdapter.SubServicesViewHolder>() {


    var _binding: ItemServiceDetailsBinding? = null
    var list = mutableListOf<ServicesItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubServicesViewHolder {
        context = parent.context
        _binding =
            ItemServiceDetailsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubServicesViewHolder(_binding!!)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SubServicesViewHolder, position: Int) {

        var currentItem = list[position]
        holder.binding.tvMoneyTitle.setText(
            context.resources.getString(R.string.price_from) + " " + currentItem?.price?.toDoubleOrNull()
                ?.roundTo(2) + " " + context.resources.getString(
                R.string.sr
            )
        )
        holder.binding.tvService.setText(currentItem.name)
        holder.binding.tvService.setText(currentItem.name)
        holder.binding.tvTime.setText(currentItem.duration?.toIntOrNull()
            ?.let { getDuration(it, context) })


        holder.binding.imgWishItem.loadImage(currentItem.image)
        holder.binding.root.setOnClickListener {
            listener.onPackagesClickListener(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class SubServicesViewHolder(val binding: ItemServiceDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)

}


