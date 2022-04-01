package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.notification.NotificationResponseModel
import com.fabnie.vendor.model.notificationlist.NotificationList
import com.fabnie.vendor.repository.NotificationListRepository
import com.fabnie.vendor.repository.NotificationRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationListViewModel
@Inject
constructor(private val notificationRepository: NotificationListRepository): ViewModel() {

    val notifications: StateFlow<Response<NotificationList>>
    get() = notificationRepository.notificationData

    fun getNotificationsList(token: String="") = viewModelScope.launch {
        notificationRepository.getNotificationList(token = token)
    }

}