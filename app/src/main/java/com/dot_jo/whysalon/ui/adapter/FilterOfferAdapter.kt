package com.dot_jo.whysalon.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.CategoriesItem
import com.dot_jo.whysalon.databinding.ItemFilterBinding
import com.dot_jo.whysalon.ui.interfaces.FilterOffersByCategoryClickListener


class FilterOfferAdapter(var listener: FilterOffersByCategoryClickListener) : RecyclerView.Adapter<FilterOfferAdapter.FilterOfferViewHolder>() {
    lateinit var context: Context


    var list = mutableListOf<CategoriesItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
 var lastSelectedPostion=-1
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterOfferViewHolder {
        context = parent.context
        return FilterOfferViewHolder(
            ItemFilterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }


    override fun onBindViewHolder(holder: FilterOfferViewHolder, position: Int) {
        var currentItem = list[position]
        holder.binding.tvTitle.setText(currentItem.name)


        holder.binding.tvTitle.setOnClickListener {
            if (holder.binding.tvTitle.isChecked) {
                currentItem.checked = 1
                holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_white)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.black))
                selectOneItemOnly(currentItem , position)
                listener.onFilterOffersByCategory(currentItem)

            } else {
         //       lastSelectedPostion =-1
                currentItem.checked = 0
                holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_black_white_border)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.white))
                 listener.onFilterOffersByCategory(null)
            }
        }

        if(currentItem.checked==1){
            holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_white)
            holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.black))
        }else{
            holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_black_white_border)
            holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.white))

        }
    }
    fun updateItem(item: CategoriesItem) {
        list.indexOfFirst { item.id == it.id }
            .takeIf { it > -1 }?.let { pos ->
                list[pos] = item
                notifyItemChanged(pos)
            }
    }

    fun selectOneItemOnly(item: CategoriesItem, position: Int) {
        if (lastSelectedPostion != -1) {
            var lastDeafult = list[lastSelectedPostion]

            updateItem(
                item = CategoriesItem(
                    lastDeafult.description,
                    lastDeafult.id,
                    lastDeafult.image,
                     lastDeafult.name,
                    0,

                    )
            )
        }
        updateItem(item)
        lastSelectedPostion = position
    }
    override fun getItemCount(): Int = list.size


    class FilterOfferViewHolder(val binding: ItemFilterBinding) :
        RecyclerView.ViewHolder(binding.root)


}

