package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.PaymentResponseModel
import com.fabnie.vendor.model.category.CategoryResponseModel
import com.fabnie.vendor.model.profile.ProfileResponseModel
import com.fabnie.vendor.repository.CategoryRepository
import com.fabnie.vendor.repository.PaymentRepository
import com.fabnie.vendor.repository.ProfileRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel
@Inject
constructor(private val paymentRepository: PaymentRepository): ViewModel() {

    val payment: StateFlow<Response<PaymentResponseModel>>
    get() = paymentRepository.paymentData

    fun getPaymentDetails(token: String) = viewModelScope.launch {
        paymentRepository.getPaymentDetails(token = token)
    }

    fun getPaymentDetailsByMonth(token: String, month:Int=0) = viewModelScope.launch {
        paymentRepository.getPaymentDetailsByMonth(token = token, month = month)
    }
}