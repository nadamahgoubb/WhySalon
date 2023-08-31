package com.dot_jo.whysalon.util.ext

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.dot_jo.whysalon.R


fun View.show(show: Boolean = true) {
    if (show) visible() else gone()
}


fun View.refreshView() {
    invalidate()
    requestLayout()
}

fun ImageView.loadGif(
    data: Any?,
    disablePlaceholder: Boolean = false,
    progressBar: ProgressBar? = null,
    placeHolderImage: Int? = null
) {

    progressBar?.visible()
    val options = RequestOptions().error(R.drawable.loading_image).apply {
        if (!disablePlaceholder) placeholder(placeHolderImage ?: R.drawable.loading_image)
        diskCacheStrategy(DiskCacheStrategy.NONE)
    }
    Glide.with(this).asGif().load(data).apply(options)
        .addListener(object : RequestListener<GifDrawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable>?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.gone()
                //    e.showLogMessage("glide")
                return false
            }

            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                progressBar?.gone()
                return false
            }

        }).into(this)
}

fun ImageView.loadImage(
    data: Any?,
    disableCache: Boolean = true,
    disablePlaceholder: Boolean = false,
    isCircular: Boolean = false,
    progressBar: ProgressBar? = null,
    errorImage: Int = R.drawable.ic_fail,
    placeHolderOnFsImage: Int = R.drawable.loading_image

) {

    progressBar?.visible()
    val options = RequestOptions().error(errorImage).apply {
        if (!disablePlaceholder) placeholder(placeHolderOnFsImage)
        if (isCircular) circleCrop()
        diskCacheStrategy(if (disableCache) DiskCacheStrategy.NONE else DiskCacheStrategy.AUTOMATIC)
    }
    Glide.with(this).load(data).apply(options).addListener(object : RequestListener<Drawable> {
        override fun onLoadFailed(
            e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean
        ): Boolean {
            progressBar?.gone()
            //    e.showLogMessage("glide")
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            progressBar?.gone()
            return false
        }

    }).into(this)

}

fun RecyclerView.init(
    context: Context?,
    adapter: RecyclerView.Adapter<*>?,
    type: Int,
    column: Int = 2 // 1 :hor  2:lin 3:grid

) {

    var layoutManager: RecyclerView.LayoutManager? = null
    when (type) {
        1 -> {
            layoutManager = LinearLayoutManager(
                context, LinearLayoutManager.HORIZONTAL, false
            )
        }

        2 -> layoutManager = LinearLayoutManager(context)
        else -> {
            layoutManager = GridLayoutManager(context, column)


        }
    }

    this.layoutManager = layoutManager
    this.setHasFixedSize(false)
    this.adapter = adapter
}

fun RecyclerView.initNested(
    context: Context?,
    adapter: RecyclerView.Adapter<*>?,

) {

    var layoutManager: RecyclerView.LayoutManager? = null
    layoutManager = LinearLayoutManager(
        context, LinearLayoutManager.HORIZONTAL,
        false
    )
    this.layoutManager = layoutManager
    this.setHasFixedSize(false)
    this.adapter = adapter
}

@RequiresApi(Build.VERSION_CODES.M)
fun View.makeForeGroundClickable() {
    val outValue = TypedValue()
    context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
    foreground = ContextCompat.getDrawable(context, outValue.resourceId)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}







