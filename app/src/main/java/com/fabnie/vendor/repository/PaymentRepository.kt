package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.model.PaymentResponseModel
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class PaymentRepository
@Inject
constructor(private val apiService: ApiService) {

    private val paymentFlowData =
        MutableStateFlow<Response<PaymentResponseModel>>(Response.Empty())

    val paymentData: StateFlow<Response<PaymentResponseModel>>
        get() = paymentFlowData

    suspend fun getPaymentDetails(token: String) {
        Log.d("AuthToken", "$token")
        try {
            paymentFlowData.emit(Response.Loading())
            val result = apiService.getPaymentDetails(authorization = "Bearer $token")
            Log.d("[Payment]", "${result}Success- ${result.body()}")
            if (result.body() != null) {
                paymentFlowData.emit(Response.Success(data = result.body()))
            } else {
                paymentFlowData.emit(Response.Error(error = "Something wents wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Payment]", "Exception- ${e.message.toString()}")
            paymentFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }

    suspend fun getPaymentDetailsByMonth(token: String, month: Int = 0) {
        Log.d("AuthToken", "$token")
        try {
            paymentFlowData.emit(Response.Loading())
            val result =
                apiService.getPaymentDetailsByMonth(authorization = "Bearer $token", month = month)
            Log.d("[Payment]", "${result}Success- ${result.body()}")
            if (result.body() != null) {
                paymentFlowData.emit(Response.Success(data = result.body()))
            } else {
                paymentFlowData.emit(Response.Error(error = "Something wents wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Payment]", "Exception- ${e.message.toString()}")
            paymentFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }

}