package com.dot_jo.whysalon.ui.adapter.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.CategoriesAndServices
import com.dot_jo.whysalon.databinding.ItemFilterBinding
import com.dot_jo.whysalon.ui.interfaces.FilterHomeByServiceClickListener
import com.dot_jo.whysalon.ui.interfaces.FilterOffersByCategoryClickListener


class FilterServiceHomeAdapter(var listener: FilterHomeByServiceClickListener) : RecyclerView.Adapter<FilterServiceHomeAdapter.FilterOfferViewHolder>() {
    lateinit var context: Context


    var list = mutableListOf<CategoriesAndServices>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
 var lastSelectedPostion=0
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
        holder.binding.tvTitle.setText(currentItem.categoryName)


        holder.binding.tvTitle.setOnClickListener {
            if (holder.binding.tvTitle.isChecked) {
                currentItem.checked = 1
                holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_black_white_border)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.white))
                selectOneItemOnly(currentItem , position)
                listener.onFilterOffersByCategory(position ,currentItem)

            } else {
         //       lastSelectedPostion =-1
                currentItem.checked = 0

                holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_white)
                holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.black))
                listener.onFilterOffersByCategory(0 , null)
            }
        }

        if(currentItem.checked==1){

            holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_black_white_border)
            holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.white))     }else{

            holder.binding.tvTitle.setBackgroundResource(R.drawable.bg_btn_white)
            holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.black))
        }
    }
    fun updateItem(item: CategoriesAndServices) {
        list.indexOfFirst { item.categoryId == it.categoryId }
            .takeIf { it > -1 }?.let { pos ->
                list[pos] = item
                notifyItemChanged(pos)
            }
    }

    fun selectOneItemOnly(item: CategoriesAndServices, position: Int) {
        if (lastSelectedPostion != -1) {
            var lastDeafult = list[lastSelectedPostion]

            updateItem(
                item = CategoriesAndServices(
                    lastDeafult.categoryId,
                    lastDeafult.categoryName,
                    lastDeafult.categoryDescription,
                    lastDeafult.categoryImage,
                     lastDeafult.services,
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

