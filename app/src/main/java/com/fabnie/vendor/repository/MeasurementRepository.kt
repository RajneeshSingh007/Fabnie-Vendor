package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.model.MeasurementResponseModel
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


class MeasurementRepository
@Inject
constructor(private val apiService: ApiService) {

    private val measurementFlowData =
        MutableStateFlow<Response<MeasurementResponseModel>>(Response.Empty())

    val measurementData: StateFlow<Response<MeasurementResponseModel>>
        get() = measurementFlowData

    suspend fun getMeasurements(token: String) {
        Log.d("AuthToken", "$token")
        try {
            measurementFlowData.emit(Response.Loading())
            val result = apiService.getMeasurements(authorization = "Bearer $token")
            Log.d("[Products]", "${result}Success- ${result.body()}")
            if (result.body() != null) {
                measurementFlowData.emit(Response.Success(data = result.body()))
            } else {
                measurementFlowData.emit(Response.Error(error = "Something wents wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Products]", "Exception- ${e.message.toString()}")
            measurementFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }
}