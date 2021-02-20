package com.oblessing.app.carlisting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.oblessing.app.R
import com.oblessing.app.carlisting.models.Car
import com.oblessing.app.carlisting.models.MSImage
import com.oblessing.app.core.utils.DataState
import com.oblessing.app.core.utils.ImageLoader
import com.oblessing.app.core.utils.doNothing
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.scopes.FragmentScoped
import kotlinx.android.synthetic.main.fragment_car_listing.*
import kotlinx.android.synthetic.main.item_image.view.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */

@AndroidEntryPoint
class CarListingFragment : Fragment() {
    private val TAG: String = "CarListingFragment"
    private val viewModel: CarListingViewModel by viewModels()

    @Inject
    lateinit var adapter: CarListingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_car_listing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list.adapter = adapter
        swiperefresh.setOnRefreshListener {
            viewModel.carsLiveData.refresh(true)
        }

         subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.carsLiveData.toLiveData.observe(viewLifecycleOwner, Observer {
            swiperefresh.isRefreshing = it is DataState.Loading

            it?.let {
                when (it) {
                    is DataState.Success -> {
                        adapter.updateCars(it.data)
                    }
                    is DataState.Error -> {
                        Toast.makeText(
                            this.context,
                            it.error.localizedMessage ?: getString(R.string.error_unknown),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> doNothing
                }

            } ?: {
                Toast.makeText(
                    this.context,
                    R.string.error_unknown,
                    Toast.LENGTH_LONG
                ).show()
            }()
        })

        viewModel.carsLiveData.refresh(forceReload = false)
    }
}

@FragmentScoped
class CarListingAdapter
@Inject
constructor(private val imageLoader: ImageLoader) :
    RecyclerView.Adapter<Holder>() {
    private val cars = mutableListOf<Car>()

    override fun getItemViewType(position: Int) =
        if (position != 0 && position % ADS_POSITION_DELTA == 0)
            AD_TYPE
        else CONTENT_TYPE

    fun updateCars(cars: List<Car> = emptyList()) {
        this.cars.clear()
        this.cars.addAll(cars)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        getViewType(parent.context, viewType, imageLoader)

    override fun getItemCount(): Int {
        var additionalContent = 0
        if (cars.size > 0 && ADS_POSITION_DELTA > 0 && cars.size > ADS_POSITION_DELTA) {
            additionalContent = cars.size / ADS_POSITION_DELTA
        }
        return cars.size + additionalContent
    }

    private fun getRealPosition(position: Int): Int {
        return position - position / ADS_POSITION_DELTA
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val car = cars[getRealPosition(position)]
        holder.updateUi(car)
    }

    companion object {
        const val CONTENT_TYPE = 0
        const val AD_TYPE = 1
        const val ADS_POSITION_DELTA = 4
    }
}

class CarImageAdapter(
    private val imageLoader: ImageLoader,
    private val callback: (MSImage) -> Unit
) :
    RecyclerView.Adapter<CarImageAdapter.Holder>() {
    private var images = mutableListOf<MSImage>()

    class Holder(itemView: View, private val imageLoader: ImageLoader) :
        RecyclerView.ViewHolder(itemView) {
        fun updateUi(image: MSImage) {
            imageLoader.loadImage(image.imageUrl, itemView.image_car)
        }
    }

    fun updateImages(images: List<MSImage>) {
        this.images.clear()
        this.images.addAll(images)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        val holder = Holder(
            itemView,
            imageLoader
        )

        itemView.setOnClickListener {
            val position = holder.adapterPosition
            callback(images[position])
        }

        return holder
    }

    override fun getItemCount() = images.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val carImage = images[position]
        holder.updateUi(carImage)
    }
}

