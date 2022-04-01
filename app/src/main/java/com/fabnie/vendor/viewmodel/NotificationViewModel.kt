package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.bankdetail.BankDetailResponseModel
import com.fabnie.vendor.model.notification.NotificationResponseModel
import com.fabnie.vendor.repository.BankDetailRepository
import com.fabnie.vendor.repository.NotificationRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel
@Inject
constructor(private val notificationRepository: NotificationRepository): ViewModel() {

    val notifications: StateFlow<Response<NotificationResponseModel>>
    get() = notificationRepository.notificationData

    fun getNotifications(token: String) = viewModelScope.launch {
        notificationRepository.getNotifications(token = token)
    }

    fun pushNotifications(token: String, int: Int = 1) = viewModelScope.launch {
        notificationRepository.updateNotification(token = token, int.toString())
    }
}