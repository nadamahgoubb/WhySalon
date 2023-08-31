package com.dot_jo.whysalon.ui.fragment.allPackages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dot_jo.whysalon.data.response.ImageItem
import com.dot_jo.whysalon.databinding.ItemWalkthrougthBinding
import com.dot_jo.whysalon.util.ext.loadImage

class SectionsHomePagerAdapter(
     private val viewPager2: ViewPager2,
    var urls: ArrayList<ImageItem>
) : RecyclerView.Adapter<SectionsHomePagerAdapter.WishListViewHolder>() {
    var _binding: ItemWalkthrougthBinding? = null

      override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): WishListViewHolder {
        _binding = ItemWalkthrougthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishListViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: WishListViewHolder, position: Int) {
        var currentItem = urls[position]


        holder.binding.imgSlider.loadImage( currentItem.name)

        if (position == urls.size) {
            viewPager2.post(runnable)
        }
    }

    private val runnable = Runnable {
        urls.clear()
        urls.addAll(urls)
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = urls.size

    class WishListViewHolder(val binding: ItemWalkthrougthBinding) : RecyclerView.ViewHolder(binding.root)

}


