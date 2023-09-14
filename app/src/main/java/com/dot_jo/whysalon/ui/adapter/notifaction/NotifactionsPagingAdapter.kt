package com.dot_jo.whysalon.ui.adapter

 import android.annotation.SuppressLint
 import android.content.Context
 import android.view.LayoutInflater
 import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
 import com.dot_jo.whysalon.data.response.NotificationItem
 import com.dot_jo.whysalon.databinding.ItemNotifactionListBinding
 import com.dot_jo.whysalon.databinding.ItemNotifactionTitleBinding
 import com.dot_jo.whysalon.ui.adapter.NotifactionAdapter
 import com.dot_jo.whysalon.util.ext.init

sealed class NotifactionItem {
    data class HeaderItem(val title: String) : NotifactionItem()
    data class BodyItem(val item: NotificationItem) : NotifactionItem()
}

class NotifactionsPagingAdapter(
  ) : PagingDataAdapter<NotificationItem,  RecyclerView.ViewHolder>(ORDER_DIFF_CALLBACK) {

    private var titleYear: String? = null
    lateinit var adapter: NotifactionAdapter

    var _bindingTitle: ItemNotifactionTitleBinding? = null
    var _bindingBody: ItemNotifactionListBinding? = null
    var list = mutableListOf<NotifactionItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    var mapp = mutableMapOf<String, ArrayList<NotifactionItem>>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    lateinit var context: Context
    var title = true
    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        var currentItem =list.get(position)



        if (title) {

            var holder2 = holder as NotifactionTileViewHolder
            titleYear = (currentItem as NotifactionItem.HeaderItem).title
            if(titleYear.toString() ==((list.get(0) as NotifactionItem.HeaderItem).title) ) {
                holder2.binding.v1.isVisible = false
            }
            else {
                holder.binding.v1.isVisible = true
            }
            holder2.binding.tvDate.setText(titleYear)

        } else {
            var holder2 = holder as NotifactionBodyViewHolder

             holder2.binding.rvOrdersPrev.init(context, adapter , 2)
            mapp?.get(titleYear)?.let {
                adapter.list = it as MutableList<NotificationItem>
                adapter.notifyDataSetChanged()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        context = parent.context
        when (viewType) {

            Type.title.value -> {
                _bindingTitle = ItemNotifactionTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                title = true
                return NotifactionTileViewHolder(_bindingTitle!!)

            }

            else -> {
                _bindingBody = ItemNotifactionListBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
              adapter = NotifactionAdapter()
                title = false
                return NotifactionBodyViewHolder(_bindingBody!!)
            }
        }



    }

    override fun getItemViewType(position: Int): Int = when (list[position]) {
        is NotifactionItem.HeaderItem -> Type.title.value
        is NotifactionItem.BodyItem -> Type.body.value
    }


    enum class Type(val value: Int) {
        title(1), body(2)
    }

    override fun getItemCount(): Int {
        return (list.size)
    }

    class NotifactionBodyViewHolder(val binding: ItemNotifactionListBinding) :
        RecyclerView.ViewHolder(binding.root)

    class NotifactionTileViewHolder(val binding: ItemNotifactionTitleBinding) :
        RecyclerView.ViewHolder(binding.root)




    companion object {
        private val ORDER_DIFF_CALLBACK = object : DiffUtil.ItemCallback<NotificationItem>() {
            override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean =
                oldItem == newItem


        }
    }
}


