package com.oblessing.app.carlisting.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.oblessing.app.R
import com.oblessing.app.carlisting.models.Car
import com.oblessing.app.carlisting.models.MSImage
import com.oblessing.app.core.utils.ImageLoader
import com.oblessing.app.core.utils.defaultCurrencySymbol
import com.oblessing.app.core.utils.hide
import com.oblessing.app.core.utils.show
import kotlinx.android.synthetic.main.item_car.view.*
import kotlinx.android.synthetic.main.item_image.view.*

fun getViewType(context: Context, type: Int, imageLoader: ImageLoader): Holder {
    return if (type == CarListingAdapter.AD_TYPE) {
        AdsHolder(
            LayoutInflater.from(context).inflate(R.layout.item_image, null, false),
            imageLoader
        )
    } else {
        ContentHolder(
            LayoutInflater.from(context).inflate(R.layout.item_car, null, false),
            imageLoader
        )
    }
}

class AdsHolder(itemView: View, private val imageLoader: ImageLoader) :
    Holder(itemView) {
    override fun onImageSelected(image: MSImage?) {
        image?.let {
            imageLoader.loadImageRes(R.drawable.advert, itemView.image_car)
        }
    }

    override fun updateUi(car: Car?) {
        car?.let {
            imageLoader.loadImageRes(R.drawable.advert, itemView.image_car)
        }
    }
}

class ContentHolder(itemView: View, private val imageLoader: ImageLoader) :
    Holder(itemView) {
    private val adapter = CarImageAdapter(imageLoader, ::onImageSelected)

    init {
        itemView.apply {
            recycler_images.adapter = adapter
            recycler_images.layoutManager =
                LinearLayoutManager(itemView.context, RecyclerView.HORIZONTAL, false)
        }
    }

    override fun onImageSelected(image: MSImage?) {
        image?.let {
            imageLoader.loadImage(it.imageUrl, itemView.image_main)
        }
    }

    override fun updateUi(car: Car?) {
        car?.let {
            itemView.apply {
                text_title.text = car.title
                text_fuel.text = car.fuel
                text_type.text = ""
                text_price.text = String.format("%s %,.2f", defaultCurrencySymbol, car.price)

            }

            if (car.pictures.isNotEmpty()) {
                imageLoader.loadImage(car.pictures[0].imageUrl, itemView.image_main)
                adapter.updateImages(car.pictures)
                itemView.recycler_images.show()
            } else {
                itemView.recycler_images.hide()
            }
        }

    }
}

abstract class Holder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    abstract fun onImageSelected(image: MSImage?);

    abstract fun updateUi(car: Car?)
}