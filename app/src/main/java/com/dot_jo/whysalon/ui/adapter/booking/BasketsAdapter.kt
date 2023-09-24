package com.dot_jo.whysalon.ui.adapter.booking

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.R
import com.dot_jo.whysalon.data.response.CartsItemResponse
import com.dot_jo.whysalon.databinding.ItemBasketBinding
import com.dot_jo.whysalon.databinding.ItemBasketPackagesBinding
import com.dot_jo.whysalon.ui.adapter.packagee.PackagesServicesLineAdapter
import com.dot_jo.whysalon.ui.interfaces.BasketClickListener
import com.dot_jo.whysalon.util.ext.init
import com.dot_jo.whysalon.util.ext.roundTo
import com.dot_jo.whysalon.util.getDuration
import java.util.*


class BasketsAdapter(val listener: BasketClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var _binding: ItemBasketBinding? = null
    var _binding_expanded: ItemBasketPackagesBinding? = null

    // val differ = AsyncListDiffer(this, differCallback)
    lateinit var context: Context
    lateinit var adapter: PackagesServicesLineAdapter
    var ordersList = mutableListOf<CartsItemResponse>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    var expand = false

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): RecyclerView.ViewHolder {
        context = parent.context
        when (viewType) {

            NewsType.SHORTed.value -> {
                _binding = ItemBasketBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                expand = false
                return BasketViewHolder(_binding!!)

            }

            else -> {
                _binding_expanded = ItemBasketPackagesBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                expand = true
                return BasketViewHolder_Expaned(_binding_expanded!!)
            }
        }

    }


    override fun getItemViewType(position: Int): Int {
        return if (ordersList.get(position).packagee == null) {
            expand = false
            NewsType.SHORTed.value
        } else {
            expand = true
            NewsType.Expand.value
        }
    }

    fun removeItem(item: CartsItemResponse) {/*   ordersList.indexOfFirst { item.id == it.id }.takeIf { it > -1 }?.let { pos ->
               ordersList[pos] = item
        //       getItemViewType(pos)
   */
        item?.let {
            ordersList.remove(it)

        }

        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = ordersList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var currentItem = ordersList[position]


        if (expand) {
            var holder2 = holder as BasketViewHolder_Expaned


            holder2.binding.tvService.setText(currentItem.packagee?.name)
            currentItem.packagee?.servicesCount?.let {
                holder2.binding.tvServiceCount.setText(it.toString() + context.resources.getString(R.string.services_))

            }
            holder2.binding.tvTime.setText(
                currentItem.packagee?.duration?.toIntOrNull()?.let { getDuration(it, context) })
            holder2.binding.tvPrice.setText(
                currentItem.packagee?.price?.toDoubleOrNull()?.roundTo(2)
                    .toString() + context.resources.getText(
                    R.string.sr
                )
            )
            //   holder2.binding.tvServiceCount.setText(currentItem.`package`?.status)
            adapter = PackagesServicesLineAdapter()
            holder2.binding.rvServices.init(context, adapter, 2)
            currentItem.packagee?.services?.let {

                adapter.list = it
                adapter.notifyDataSetChanged()
            }
            holder2.binding.lytDelete.setOnClickListener {
                listener.onDeleteClickLisenter(currentItem)
            }
        } else {

            var holder2 = holder as BasketViewHolder
            holder2.binding.tvService.setText(currentItem.service?.name)
            holder2.binding.tvTime.setText(
                currentItem.service?.duration?.toIntOrNull()?.let { getDuration(it, context) })
            holder2.binding.tvPrice.setText(
                currentItem.service?.price?.toDoubleOrNull()?.roundTo(2)
                    .toString() + context.resources.getText(R.string.sr)
            )
            holder2.binding.tvDesc.setText(currentItem.service?.description)
            holder2.binding.lytDelete.setOnClickListener {
                listener.onDeleteClickLisenter(currentItem)
            }

        }


    }
}

class BasketViewHolder(val binding: ItemBasketBinding) : RecyclerView.ViewHolder(binding.root)

class BasketViewHolder_Expaned(val binding: ItemBasketPackagesBinding) :
    RecyclerView.ViewHolder(binding.root)

enum class NewsType(val value: Int) {
    Expand(1), SHORTed(2)
}


private val differCallback = object : DiffUtil.ItemCallback<CartsItemResponse>() {
    override fun areItemsTheSame(
        oldItem: CartsItemResponse, newItem: CartsItemResponse
    ): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(
        oldItem: CartsItemResponse, newItem: CartsItemResponse
    ): Boolean {
        return oldItem == newItem
    }

}

