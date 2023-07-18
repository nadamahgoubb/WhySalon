package com.example.whysalon.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whysalon.R
import com.example.whysalon.data.response.TimeResponse
import com.example.whysalon.databinding.ItemFilterTimeBinding
import com.example.whysalon.ui.interfaces.FilterTimeClickListener


class FilterTimeAdapter(var listener : FilterTimeClickListener) : RecyclerView.Adapter<FilterTimeAdapter.FilterOfferViewHolder>() {
    lateinit var context: Context

var lastDefaultPosition =-1
    var list = mutableListOf<TimeResponse>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterOfferViewHolder {
        context = parent.context
        return FilterOfferViewHolder(
            ItemFilterTimeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }


    override fun onBindViewHolder(holder: FilterOfferViewHolder, position: Int) {
var currentItem = list.get(position)
holder.binding.tvTitle.setText(currentItem.time)


        if (currentItem.default==true){

            holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_black_white_border)
            holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.white))

        }else{
            holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_white_grey_border)
            holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.black))

        }
        holder.binding.tvTitle.setOnClickListener {
            if (holder.binding.tvTitle.isChecked) {

                holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_black_white_border)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.white))
listener.onTimeChoosenListener(true)
                currentItem.default= true
                selectOneItemOnly(currentItem,position)

            } else {
                holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_white_grey_border)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.black))
                listener.onTimeChoosenListener(false)
            }

        }
    }
    fun selectOneItemOnly(item: TimeResponse, position: Int) {
        if (lastDefaultPosition != -1) {
            var lastDeafult = list[lastDefaultPosition]
lastDeafult.default= false
            updateItem(
                lastDeafult,
                position
            )
        }
        updateItem(item,position)
        lastDefaultPosition = position

    }

    fun updateItem(item: TimeResponse, position: Int) {
      list.indexOfFirst { item.id == it.id }.takeIf { it > -1 }?.let { pos ->
            list[pos] = item
            notifyItemChanged(pos)
       }
    }

    override fun getItemCount(): Int = list.size


    class FilterOfferViewHolder(val binding: ItemFilterTimeBinding) :
        RecyclerView.ViewHolder(binding.root)


}

