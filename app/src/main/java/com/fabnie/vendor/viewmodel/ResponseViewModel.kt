package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.ResponsesModel
import com.fabnie.vendor.model.addproduct.AddProductRequestModel
import com.fabnie.vendor.model.addproduct.AddProductResponseModel
import com.fabnie.vendor.model.product.ProductResponseModel
import com.fabnie.vendor.repository.AddProductRepository
import com.fabnie.vendor.repository.ProductRepository
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.repository.ResponseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ResponseViewModel
@Inject
constructor(private val responseRepository: ResponseRepository): ViewModel() {

    val response: StateFlow<Response<ResponsesModel>>
    get() = responseRepository.responseData

    fun deleteNotifications(id:Int, token: String) = viewModelScope.launch {
        responseRepository.deleteNotification(id, token = token)
    }

    fun orderStatusChange(id:Int, token: String, status:String,) = viewModelScope.launch {
        responseRepository.orderStatusChange(token = token,id = id,status = status)
    }


}