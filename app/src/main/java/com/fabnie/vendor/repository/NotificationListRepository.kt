package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.model.notificationlist.NotificationList
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class NotificationListRepository
@Inject
constructor(private val apiService: ApiService) {

    private val notificationFlowData =
        MutableStateFlow<Response<NotificationList>>(Response.Empty())
    val notificationData: StateFlow<Response<NotificationList>>
        get() = notificationFlowData

    suspend fun getNotificationList(token: String) {
        Log.d("AuthToken", "$token")
        try {
            notificationFlowData.emit(Response.Loading())
            val result = apiService.getNotificationList(authorization = "Bearer $token")
            Log.d("[Notification]", "Success- ${result.body()}")
            if (result.body() != null) {
                notificationFlowData.emit(Response.Success(data = result.body()))
            } else {
                notificationFlowData.emit(Response.Error(error = "Something went wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Notification]", "Exception- ${e.message.toString()}")
            notificationFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }
}