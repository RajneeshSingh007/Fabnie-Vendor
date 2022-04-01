package com.fabnie.vendor.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.MeasurementResponseModel
import com.fabnie.vendor.model.product.ProductResponseModel
import com.fabnie.vendor.model.userproduct.UserProductResponseModel
import com.fabnie.vendor.repository.MeasurementRepository
import com.fabnie.vendor.repository.ProductRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeasurementViewModel
@Inject
constructor(private val measurementRepository: MeasurementRepository): ViewModel() {

    val measurements: StateFlow<Response<MeasurementResponseModel>>
    get() = measurementRepository.measurementData

    fun getMeasurements(token: String) = viewModelScope.launch {
        measurementRepository.getMeasurements(token = token)
    }
}