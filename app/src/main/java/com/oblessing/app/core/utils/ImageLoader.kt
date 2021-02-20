package com.oblessing.app.core.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.oblessing.app.R
import com.squareup.picasso.Picasso
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageLoader
@Inject
constructor(private val picasso: Picasso) {
    fun loadImage(url: String, imageView: ImageView) {
        picasso
            .load(url)
            .placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_broken_image_24)
            .into(imageView)
    }

    fun loadImageRes(@DrawableRes resId: Int, imageView: ImageView) {
        picasso.load(resId).placeholder(R.drawable.ic_baseline_image_24)
            .error(R.drawable.ic_baseline_broken_image_24).into(imageView)
    }
}
