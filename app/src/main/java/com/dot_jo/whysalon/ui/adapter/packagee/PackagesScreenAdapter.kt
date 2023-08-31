package com.dot_jo.whysalon.ui.adapter.packagee

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.ServicesItem
import com.dot_jo.whysalon.databinding.ItemPackagesBinding
import com.dot_jo.whysalon.ui.interfaces.HomeClickListener
import com.dot_jo.whysalon.util.ext.loadImage
import com.dot_jo.whysalon.util.ext.roundTo
import com.dot_jo.whysalon.util.getDuration


class PackagesScreenAdapter(
    var listener: HomeClickListener
) : RecyclerView.Adapter<PackagesScreenAdapter.PackagesScreenViewHolder>() {


    var _binding: ItemPackagesBinding? = null
    var list = mutableListOf<ServicesItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context




override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackagesScreenViewHolder {
    context = parent.context
         _binding = ItemPackagesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PackagesScreenViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: PackagesScreenViewHolder, position: Int) {

        var currentItem = list[position]
        holder.binding.tvMoneyTitle.setText(context.resources.getString(R.string.price_from)+currentItem?.price?.toDoubleOrNull()?.roundTo(2)+context.resources.getString(
            R.string.sr))

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

    class PackagesScreenViewHolder(val binding: ItemPackagesBinding) :
        RecyclerView.ViewHolder(binding.root)

}


