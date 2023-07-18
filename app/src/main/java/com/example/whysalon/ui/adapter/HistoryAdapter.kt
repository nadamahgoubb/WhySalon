package com.example.whysalon.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.whysalon.data.response.ServiceItem
import com.example.whysalon.databinding.ItemHistoryBinding
import com.example.whysalon.databinding.ItemRecomendationBinding
import com.example.whysalon.ui.interfaces.HistoryClickListener
import com.example.whysalon.util.ext.loadImage


class HistoryAdapter(
    var listener: HistoryClickListener
) : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {


    var _binding: ItemHistoryBinding? = null
    var list = mutableListOf<ServiceItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context




override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
    context = parent.context
         _binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
if(list.size>1) {

    val layoutParams: ViewGroup.LayoutParams = _binding!!.root.layoutParams
    layoutParams.width = (parent.width * 0.90).toInt()
    _binding!!.root.setLayoutParams(layoutParams)

}
        return HistoryViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        var currentItem = list[position]
        holder.binding.tvMoney.setText(currentItem.cost)
        holder.binding.tvBarberName.setText(currentItem.name)
        holder.binding.tvServicesType.setText(currentItem.service)
      //  holder.binding.ivService.loadImage(currentItem.img)
holder.binding.lytRebook.setOnClickListener {
listener.onHistoryClickListener()
}

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class HistoryViewHolder(val binding: ItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

}


