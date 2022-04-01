package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.model.ResponsesModel
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

class ResponseRepository
@Inject
constructor(private val apiService: ApiService) {

    private val responseFlowData = MutableStateFlow<Response<ResponsesModel>>(Response.Empty())
    val responseData: StateFlow<Response<ResponsesModel>>
        get() = responseFlowData

    /**
     * delete notification
     */
    suspend fun deleteNotification(id: Int, token: String) {
        Log.d("AuthToken", "$token")
        try {
            responseFlowData.emit(Response.Loading())
            val result = apiService.deleteNotification(id, authorization = "Bearer $token")
            Log.d("[Response]", "Success- ${result.body()}")
            if (result.body() != null) {
                responseFlowData.emit(Response.Success(data = result.body()))
            } else {
                responseFlowData.emit(Response.Error(error = "Something wents wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Response]", "Exception- ${e.message.toString()}")
            responseFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }

    /**
     * order status change
     */
    suspend fun orderStatusChange(id: Int, status: String, token: String) {
        Log.d("AuthToken", "$token")
        try {
            responseFlowData.emit(Response.Loading())
            val idBody: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"), id.toString())
            val statusBody: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"), status.toString())
            val result = apiService.updateOrderStatus(
                authorization = "Bearer $token",
                order_Id = idBody,
                status = statusBody
            )
            Log.d("[Response]", "Success- ${result.body()}")
            if (result.body() != null) {
                responseFlowData.emit(Response.Success(data = result.body()))
            } else {
                responseFlowData.emit(Response.Error(error = "Something wents wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Response]", "Exception- ${e.message.toString()}")
            responseFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }
}