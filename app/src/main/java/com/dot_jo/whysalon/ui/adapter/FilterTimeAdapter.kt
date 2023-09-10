package com.dot_jo.whysalon.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.databinding.ItemFilterTimeBinding
import com.dot_jo.whysalon.ui.interfaces.FilterTimeClickListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date


class FilterTimeAdapter(var listener: FilterTimeClickListener) :
    RecyclerView.Adapter<FilterTimeAdapter.FilterOfferViewHolder>() {
    lateinit var context: Context

    var lastDefaultPosition = -1
    var list = mutableListOf<String>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    var selected = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterOfferViewHolder {
        context = parent.context
        return FilterOfferViewHolder(
            ItemFilterTimeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat", "DefaultLocale")
    override fun onBindViewHolder(holder: FilterOfferViewHolder, position: Int) {
        var currentItem = list.get(position)


        holder.binding.tvTitle.text = LocalTime.parse(currentItem).format(DateTimeFormatter.ofPattern("h:mma"))


        if (currentItem == selected) {
            holder.binding.tvTitle.isChecked = true
            holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_black_white_border)
            holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.white))

        } else {
            holder.binding.tvTitle.isChecked = false
            holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_white_grey_border)
            holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.black))

        }
        holder.binding.tvTitle.setOnClickListener {
            if (holder.binding.tvTitle.isChecked) {

                holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_black_white_border)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.white))
                selected = currentItem
                listener.onTimeChoosenListener(currentItem)
                selectOneItemOnly(currentItem, position)

            } else {
                holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_white_grey_border)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.black))
                selected = ""
                lastDefaultPosition = -1
                listener.onTimeChoosenListener(null)
            }

        }
    }

    fun selectOneItemOnly(item: String, position: Int) {
        if (lastDefaultPosition != -1) {
            var lastDeafult = list[lastDefaultPosition]
//lastDeafult.selected= false
            updateItem(
                lastDeafult,
                position
            )
        }
        updateItem(item, position)
        lastDefaultPosition = position

    }

    fun updateItem(item: String, position: Int) {
        list.indexOfFirst { item == it }.takeIf { it > -1 }?.let { pos ->
            list[pos] = item
            notifyItemChanged(pos)
        }
    }

    override fun getItemCount(): Int = list.size


    class FilterOfferViewHolder(val binding: ItemFilterTimeBinding) :
        RecyclerView.ViewHolder(binding.root)


}

