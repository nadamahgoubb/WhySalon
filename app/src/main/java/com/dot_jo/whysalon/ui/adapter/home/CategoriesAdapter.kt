package com.dot_jo.whysalon.ui.adapter.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.data.response.CategoriesItem

import com.dot_jo.whysalon.databinding.ItemServiceBinding
import com.dot_jo.whysalon.ui.interfaces.HomeClickListener


class CategoriesAdapter(
    var listener: HomeClickListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {


    var _binding: ItemServiceBinding? = null
    var list = mutableListOf<CategoriesItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        context = parent.context
        _binding = ItemServiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoriesViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {

        var currentItem = list[position]
        holder.binding.tvName.setText(currentItem.name)
        //  holder.binding.ivService.loadImage(currentItem.img)
        holder.binding.root.setOnClickListener {
            listener.onCategoryClickListener(currentItem)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class CategoriesViewHolder(val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root)

}


