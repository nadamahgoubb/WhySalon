package com.dot_jo.whysalon.ui.adapter.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.ServicesInCatgories
import com.dot_jo.whysalon.databinding.ItemNewServiceBinding
import com.dot_jo.whysalon.ui.interfaces.HomeSericeListener
import com.dot_jo.whysalon.util.getDuration
import kotlin.math.roundToInt


class NewServiceAdapter(
    var listener: HomeSericeListener
) : RecyclerView.Adapter<NewServiceAdapter.NewServiceViewHolder>() {


    var _binding: ItemNewServiceBinding? = null
    var list = mutableListOf<ServicesInCatgories>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context




override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewServiceViewHolder {
    context = parent.context
         _binding = ItemNewServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return NewServiceViewHolder(_binding!!)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: NewServiceViewHolder, position: Int) {

         var currentItem = list[position]
        holder.binding.tvName.setText(currentItem.serviceName)
       // holder.binding.tvDesc.setText(currentItem.)
         holder.binding.tvDesc.setText(
          currentItem?.price + " " + context.resources.getString(
                R.string.sr
            )
        )
         holder.binding.tvTime.setText(currentItem.serviceDuration?.roundToInt()
            ?.let { getDuration(it, context) })


         holder.binding.cardAdd.setOnClickListener {
            listener.onBookNowClickListener(position, currentItem)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class NewServiceViewHolder(val binding: ItemNewServiceBinding) :
        RecyclerView.ViewHolder(binding.root)

}


