package com.dot_jo.whysalon.ui.adapter.booking

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
 import androidx.recyclerview.widget.RecyclerView
import com.dot_jo.whysalon.data.response.BookingsItem
import com.dot_jo.whysalon.databinding.ItemHistoryListBinding
import com.dot_jo.whysalon.databinding.ItemHistoryTitleBinding
 import com.dot_jo.whysalon.ui.interfaces.HistoryClickListener
import com.dot_jo.whysalon.util.ext.initNested


sealed class HistoryItem {
    data class HeaderItem(val title: Int) : HistoryItem()
    data class historyItem(val item: BookingsItem) : HistoryItem()
}

class HistoryWithYearAdapter(
    var listener: HistoryClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var titleYear: String? = null
    lateinit var adapter: HistoryAdapter

    var _bindingTitle: ItemHistoryTitleBinding? = null
    var _bindingBody: ItemHistoryListBinding? = null
    var list = mutableListOf<HistoryItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    var mapp = mutableMapOf <Int, ArrayList<BookingsItem>>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context
    var title = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
         when (viewType) {

            Type.title.value -> {
                _bindingTitle = ItemHistoryTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                title = true
                return HistoryTileViewHolder(_bindingTitle!!)

            }

            else -> {
                _bindingBody = ItemHistoryListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                adapter= HistoryAdapter( listener)
                title = false
                return HistoryBodyViewHolder(_bindingBody!!)
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        var currentItem = list[position]


        if (title) {

            var holder2 = holder as HistoryTileViewHolder
titleYear = (currentItem as HistoryItem.HeaderItem).title.toString()
currentItem as HistoryItem
            holder2.binding.tvYear.setText( titleYear)

        } else {

            var holder2 = holder as HistoryBodyViewHolder
            holder2.binding.rvHistoryYearBfore.initNested(context, adapter )
            mapp?.get(titleYear?.toInt())?.let {
                adapter.list = it
                adapter.notifyDataSetChanged()
            }
            }


    }
    override fun getItemViewType(position: Int): Int = when (list[position]) {
        is HistoryItem.HeaderItem -> Type.title.value
        is HistoryItem.historyItem -> Type.body.value
    }


    enum class Type(val value: Int) {
        title(1), body(2)
    }

    override fun getItemCount(): Int {
        return (list.size)
    }

    class HistoryBodyViewHolder(val binding: ItemHistoryListBinding) :
        RecyclerView.ViewHolder(binding.root)

    class HistoryTileViewHolder(val binding: ItemHistoryTitleBinding) :
        RecyclerView.ViewHolder(binding.root)


}


