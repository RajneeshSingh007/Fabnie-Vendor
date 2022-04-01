package com.fabnie.vendor.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.bankdetail.BankDetailResponseModel
import com.fabnie.vendor.model.notification.NotificationResponseModel
import com.fabnie.vendor.model.orders.OrderDetailResponseModel
import com.fabnie.vendor.model.orders.OrderResponseModel
import com.fabnie.vendor.repository.BankDetailRepository
import com.fabnie.vendor.repository.NotificationRepository
import com.fabnie.vendor.repository.OrdersRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel
@Inject
constructor(private val ordersRepository: OrdersRepository): ViewModel() {

    val orders: StateFlow<Response<OrderResponseModel>>
    get() = ordersRepository.ordersData

    val orderDetails: StateFlow<Response<OrderDetailResponseModel>>
    get() = ordersRepository.ordersDetailsData

    fun getAllOrders(token: String) = viewModelScope.launch {
        ordersRepository.getAllOrders(token = token)
    }

    fun getOrderDetails(token: String, orderID: Int = 1) = viewModelScope.launch {
        ordersRepository.getOrdersDetails(token = token, orderID.toString())
    }
}