package com.dot_jo.whysalon.ui.adapter.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.OfferssItem
import com.dot_jo.whysalon.databinding.ItemOffersBinding
import com.dot_jo.whysalon.ui.interfaces.OffersClickListener
import com.dot_jo.whysalon.util.ext.loadImage
import kotlin.math.roundToInt


class OffersAdapter(
    var listener: OffersClickListener
) : RecyclerView.Adapter<OffersAdapter.OffersViewHolder>() {


    var _binding: ItemOffersBinding? = null
    var list = mutableListOf<OfferssItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context




override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OffersViewHolder {
    context = parent.context
         _binding = ItemOffersBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return OffersViewHolder(_binding!!)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OffersViewHolder, position: Int) {

        var currentItem = list[position]
         holder.binding.tvMoneyBefore.setText (currentItem?.services!!.price_before?.toDoubleOrNull()?.roundToInt().toString() +" "+context.resources.getString(
            R.string.sr))
        holder.binding.tvMoneyBefore.paintFlags =  holder.binding.tvMoneyBefore.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
         holder.binding.tvMoneyAfter.setText("( "+ currentItem?.services?.price?.toDoubleOrNull()?.roundToInt().toString()+" "+context.resources.getString(
            R.string.sr) + " )")
        holder.binding.tvServicesType.setText(context.resources.getString(R.string.service_type)+" "+currentItem.services?.name)
           holder.binding.imgWishItem.loadImage(currentItem.services?.small_image)

        holder.binding.root.setOnClickListener {
            listener.onOffersClickListener(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class OffersViewHolder(val binding: ItemOffersBinding) :
        RecyclerView.ViewHolder(binding.root)

}


