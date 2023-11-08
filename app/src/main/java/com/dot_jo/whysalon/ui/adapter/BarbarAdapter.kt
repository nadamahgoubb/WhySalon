package com.dot_jo.whysalon.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.BarbarItem
import com.dot_jo.whysalon.databinding.ItemChooseBarberBinding
import com.dot_jo.whysalon.ui.interfaces.BarbarClickListener
import com.dot_jo.whysalon.util.ext.loadImage
import com.dot_jo.whysalon.util.ext.roundTo


class BarbarAdapter(
    var listener: BarbarClickListener
) : RecyclerView.Adapter<BarbarAdapter.BarbarViewHolder>() {


    var _binding: ItemChooseBarberBinding? = null
    var list = mutableListOf<BarbarItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarbarViewHolder {
        context = parent.context
        _binding =
            ItemChooseBarberBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BarbarViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: BarbarViewHolder, position: Int) {

        var currentItem = list[position]
        holder.binding.tvName.setText(currentItem.name)
        holder.binding.tvRating.setText(currentItem.avg_rates?.toDouble()?.roundTo(1).toString())
        holder.binding.imgWishItem.loadImage(currentItem.image , isCircular = true)

         holder.binding.root.setOnClickListener {

            if (currentItem.seleted== false) {
                currentItem.seleted= true
           selectOneItemOnly(currentItem, position)
                listener.onBarbarClickListener(currentItem)

            } else {
                currentItem.seleted= false
                updateItem(currentItem)
                lastSelectedPostion= -1
                listener.onBarbarClickListener(null)

            }
        }
 holder.binding.lytRoot.rootView.setOnClickListener {
            if (currentItem.seleted== false) {
                currentItem.seleted= true
           selectOneItemOnly(currentItem, position)
                listener.onBarbarClickListener(currentItem)

            } else {
                currentItem.seleted= false
                updateItem(currentItem)
                lastSelectedPostion= -1
                listener.onBarbarClickListener(null)

            }
        }


        if (currentItem.seleted == true) {
        holder.binding.lytRoot.setBackgroundResource(R.drawable.bg_btn_white_black_border)
            holder.binding.checkbox.isChecked = true      //    binding.item11.your_rectangle.setBackgroundResource(R.drawable.bg_btn_white_grey_border)
        } else {
            holder.binding.lytRoot.setBackgroundResource(R.drawable.bg_btn_white_grey_border)
            holder.binding.checkbox.isChecked = false

        }
    }

    fun updateItem(item: BarbarItem) {
        list.indexOfFirst { item.id == it.id }
            .takeIf { it > -1 }?.let { pos ->
                list[pos] = item
                notifyItemChanged(pos)
            }
    }

    var lastSelectedPostion = -1
    fun selectOneItemOnly(item: BarbarItem, position: Int) {
        if (lastSelectedPostion != -1) {
            var lastDeafult = list[lastSelectedPostion]

            updateItem(
                item = BarbarItem(
                    lastDeafult.email,
                    lastDeafult.id,
                    lastDeafult.image,
                    lastDeafult.name,
                    lastDeafult.number_rates,
                    lastDeafult.avg_rates,
                    false,

                    )
            )
        }
        updateItem(item)
        lastSelectedPostion = position
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class BarbarViewHolder(val binding: ItemChooseBarberBinding) :
        RecyclerView.ViewHolder(binding.root)

}


