package com.dot_jo.whysalon.ui.adapter.packagee

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.data.response.ServicesItem
import com.dot_jo.whysalon.databinding.ItemPackageServicesItemBinding


class PackagesServicesLineAdapter(
 ) : RecyclerView.Adapter<PackagesServicesLineAdapter.PackagesServicesLineViewHolder>() {


    var _binding: ItemPackageServicesItemBinding? = null
    var list = mutableListOf<ServicesItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context




override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackagesServicesLineViewHolder {
    context = parent.context
         _binding = ItemPackageServicesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PackagesServicesLineViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: PackagesServicesLineViewHolder, position: Int) {

        var currentItem:ServicesItem = list[position]


                holder.binding.tvDesc.setText(currentItem.name)


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class PackagesServicesLineViewHolder(val binding: ItemPackageServicesItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}


