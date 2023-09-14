package com.dot_jo.whysalon.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.data.response.NotificationItem
import com.dot_jo.whysalon.databinding.ItemNotifactionBinding
import com.dot_jo.whysalon.util.convertPttern
class NotifactionAdapter(
 ) : RecyclerView.Adapter<NotifactionAdapter.NotifactionViewHolder>() {


    var _binding: ItemNotifactionBinding? = null
    var list = mutableListOf<NotificationItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifactionViewHolder {
        context = parent.context
        _binding = ItemNotifactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NotifactionViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: NotifactionViewHolder, position: Int) {

        var currentItem = list[position]
        holder.binding.tvDesc.text = currentItem.body.toString()
        holder.binding.tvService.text = currentItem.title.toString()
        holder.binding.tvDate.text = currentItem.time

    }


    override fun getItemCount(): Int {
        return list.size
    }

    class NotifactionViewHolder(val binding: ItemNotifactionBinding) : RecyclerView.ViewHolder(binding.root)

}


