package com.example.whysalon.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whysalon.R
import com.example.whysalon.databinding.ItemFilterBinding


class FilterOfferAdapter() : RecyclerView.Adapter<FilterOfferAdapter.FilterOfferViewHolder>() {
    lateinit var context: Context


    var list = mutableListOf<String>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterOfferViewHolder {
        context = parent.context
        return FilterOfferViewHolder(
            ItemFilterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }


    override fun onBindViewHolder(holder: FilterOfferViewHolder, position: Int) {


        holder.binding.tvTitle.setOnClickListener {
            if (holder.binding.tvTitle.isChecked) {
                holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_white)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.black))

            } else {
                holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_black_white_border)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.white))
            }

        }
    }


    override fun getItemCount(): Int = list.size


    class FilterOfferViewHolder(val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root)


}

