package com.example.whysalon.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.whysalon.R
import com.example.whysalon.data.response.TimeResponse
import com.example.whysalon.databinding.ItemFilterRatingBinding
import com.example.whysalon.ui.interfaces.RatingResaonsClickListener


class RateReasonsAdapter(
    var listener: RatingResaonsClickListener
) : RecyclerView.Adapter<RateReasonsAdapter.ReasonFilterViewHolder>() {


    var _binding: ItemFilterRatingBinding? = null
    var list = mutableListOf<TimeResponse>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context


var listSelected = arrayListOf<TimeResponse>()

override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReasonFilterViewHolder {
    context = parent.context
         _binding = ItemFilterRatingBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ReasonFilterViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: ReasonFilterViewHolder, position: Int) {

        var currentItem = list[position]
        holder.binding.tvTitle.setText(currentItem.time)
holder.binding.tvTitle.setOnClickListener {
    if(holder.binding.tvTitle.isChecked){
    }else{

    }
    holder.binding.tvTitle.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
            if ( isChecked) {
                holder.binding.tvTitle.setBackgroundResource( R.drawable.bg_btn_black_white_border)
                listSelected.add(currentItem)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.white))
            }
            else{
                listSelected.remove(currentItem)
                holder.binding.tvTitle.setBackgroundResource( R.drawable.bg_btn_white_black_border)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.black))             }
            listener.onRatingResaonsListener(listSelected)

        }
    })

}

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ReasonFilterViewHolder(val binding: ItemFilterRatingBinding) :
        RecyclerView.ViewHolder(binding.root)

}


