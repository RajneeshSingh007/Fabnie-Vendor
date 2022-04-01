package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.model.notification.NotificationResponseModel
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

class NotificationRepository
@Inject
constructor(private val apiService: ApiService) {

    //Get
    private val notificationFlowData =
        MutableStateFlow<Response<NotificationResponseModel>>(Response.Empty())
    val notificationData: StateFlow<Response<NotificationResponseModel>>
        get() = notificationFlowData

    suspend fun getNotifications(token: String) {
        Log.d("AuthToken", "$token")
        try {
            notificationFlowData.emit(Response.Loading())
            val result = apiService.getNotifications(authorization = "Bearer $token")
            Log.d("[Profile]", "Success- ${result.body()}")
            if (result.body() != null) {
                notificationFlowData.emit(Response.Success(data = result.body()))
            } else {
                notificationFlowData.emit(Response.Error(error = "Something went wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Profile]", "Exception- ${e.message.toString()}")
            notificationFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }

    suspend fun updateNotification(token: String, pushEnable: String = "") {
        Log.d("AuthToken", "$token")
        try {
            val pushNotification: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"), pushEnable)
            notificationFlowData.emit(Response.Loading())
            val result =
                apiService.pushNotifications(authorization = "Bearer $token", pushNotification)
            Log.d("[Profile]", "Success- ${result.body()}")
            if (result.body() != null) {
                notificationFlowData.emit(Response.Success(data = result.body()))
            } else {
                notificationFlowData.emit(Response.Error(error = "Something went wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Profile]", "Exception- ${e.message.toString()}")
            notificationFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }
}