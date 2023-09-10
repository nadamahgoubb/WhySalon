package com.dot_jo.whysalon.ui.adapter.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.ServicesItem
import com.dot_jo.whysalon.databinding.ItemRecomendationBinding
import com.dot_jo.whysalon.ui.interfaces.HomeClickListener
import com.dot_jo.whysalon.util.ext.loadImage
import com.dot_jo.whysalon.util.getDuration
import kotlin.math.roundToInt


class PackagesAdapter(
    var listener: HomeClickListener
) : RecyclerView.Adapter<PackagesAdapter.PackagesViewHolder>() {


    var _binding: ItemRecomendationBinding? = null
    var list = mutableListOf<ServicesItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackagesViewHolder {
        context = parent.context
        _binding =
            ItemRecomendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        if (list.size > 1) {

            val layoutParams: ViewGroup.LayoutParams = _binding!!.root.layoutParams
            layoutParams.width = (parent.width * 0.90).toInt()
            _binding!!.root.setLayoutParams(layoutParams)

        }
        return PackagesViewHolder(_binding!!)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PackagesViewHolder, position: Int) {

        var currentItem = list[position]
        holder.binding.tvMoney.text =
            context.resources.getString(R.string.price_from) + " " + currentItem?.price?.toDoubleOrNull()
                ?.roundToInt() + " "+context.resources.getString(
                R.string.sr
            )
        holder.binding.tvServicesType.text = currentItem.name
        //    holder.binding.tvtime.setText(currentItem.duration+ context.resources.getString(R.string.min))
        holder.binding.tvtime.text = currentItem?.duration?.toIntOrNull()
            ?.let { getDuration(it, context) }


        holder.binding.ivService.loadImage(currentItem.image)
        holder.binding.lytBook.setOnClickListener {
            listener.onBookNowClickListener(currentItem)
        }
        holder.itemView.setOnClickListener {
            listener.onPackagesClickListener(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class PackagesViewHolder(val binding: ItemRecomendationBinding) :
        RecyclerView.ViewHolder(binding.root)

}


