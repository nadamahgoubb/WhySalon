package com.dot_jo.whysalon.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.data.response.Notification
import com.dot_jo.whysalon.databinding.ItemNotifactionBinding

class NotificationsAdapter(

) : RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder>() {


    var _binding: ItemNotifactionBinding? = null
    var list = mutableListOf<Notification>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        context = parent.context
        _binding = ItemNotifactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationsViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {

        var currentItem = list[position]
       holder.binding.tvService.text = currentItem.title
       holder.binding.tvDesc.text = currentItem.body
        holder.binding.tvDate.text = currentItem.time

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class NotificationsViewHolder(val binding: ItemNotifactionBinding) :
        RecyclerView.ViewHolder(binding.root)

}