package com.dot_jo.whysalon.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.data.response.CategoriesItem
import com.dot_jo.whysalon.databinding.ItemFullServiceBinding
import com.dot_jo.whysalon.ui.interfaces.HomeClickListener
import com.dot_jo.whysalon.util.ext.loadImage


class ServicesAdapter(
    var listener: HomeClickListener
) : RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder>() {


    var _binding: ItemFullServiceBinding? = null
    var list = mutableListOf<CategoriesItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesViewHolder {
        context = parent.context
        _binding = ItemFullServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServicesViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: ServicesViewHolder, position: Int) {

        var currentItem = list[position]
         holder.binding.tvService.setText(currentItem.name)
         holder.binding.tvDesc.setText(currentItem.description)
        holder.binding.imgWishItem.loadImage(currentItem.image)
         holder.binding.root.setOnClickListener {
            listener.onCategoryClickListener(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ServicesViewHolder(val binding: ItemFullServiceBinding) :
        RecyclerView.ViewHolder(binding.root)

}


