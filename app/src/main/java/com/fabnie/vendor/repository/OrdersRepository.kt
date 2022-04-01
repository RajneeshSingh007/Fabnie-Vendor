package com.fabnie.vendor.repository


import android.util.Log
import com.fabnie.vendor.model.orders.OrderDetailResponseModel
import com.fabnie.vendor.model.orders.OrderResponseModel
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class OrdersRepository
@Inject
constructor(private val apiService: ApiService) {

    //Get
    private val ordersFlowData = MutableStateFlow<Response<OrderResponseModel>>(Response.Empty())
    val ordersData: StateFlow<Response<OrderResponseModel>>
        get() = ordersFlowData

    private val ordersDetailsFlowData =
        MutableStateFlow<Response<OrderDetailResponseModel>>(Response.Empty())
    val ordersDetailsData: StateFlow<Response<OrderDetailResponseModel>>
        get() = ordersDetailsFlowData

    suspend fun getAllOrders(token: String) {
        Log.d("AuthToken", "$token")
        try {
            ordersFlowData.emit(Response.Loading())
            val result = apiService.getOrders(authorization = "Bearer $token")
            Log.d("[Profile]", "Success- ${result.body()}")
            if (result.body() != null) {
                ordersFlowData.emit(Response.Success(data = result.body()))
            } else {
                ordersFlowData.emit(Response.Error(error = "Something went wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Profile]", "Exception- ${e.message.toString()}")
            ordersFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }

    suspend fun getOrdersDetails(token: String, orderId: String = "") {
        Log.d("AuthToken", "$token")
        try {
            ordersDetailsFlowData.emit(Response.Loading())
            val result =
                apiService.getOrderDetails(authorization = "Bearer $token", id = orderId.toInt())
            Log.d("[Profile]", "Success- ${result.body()}")
            if (result.body() != null) {
                ordersDetailsFlowData.emit(Response.Success(data = result.body()))
            } else {
                ordersDetailsFlowData.emit(Response.Error(error = "Something went wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Profile]", "Exception- ${e.message.toString()}")
            ordersDetailsFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }
}