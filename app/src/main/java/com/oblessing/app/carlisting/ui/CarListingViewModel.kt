package com.oblessing.app.carlisting.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oblessing.app.carlisting.models.Car
import com.oblessing.app.carlisting.repository.CarListingRepository
import com.oblessing.app.core.utils.DataState
import com.oblessing.app.core.utils.RefreshableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarListingViewModel
@Inject
constructor(private val repository: CarListingRepository) : ViewModel() {
    val carsLiveData: RefreshableLiveData<DataState<List<Car>>> =
        RefreshableLiveData(refreshCallback = { forceReload, receiver ->
            viewModelScope.launch {
                repository.cars(forceReload).onEach {
                    receiver.value = it
                }.launchIn(viewModelScope)
            }
        })
}