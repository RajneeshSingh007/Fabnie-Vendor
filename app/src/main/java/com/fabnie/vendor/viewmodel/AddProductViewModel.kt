package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.addproduct.AddProductRequestModel
import com.fabnie.vendor.model.addproduct.AddProductResponseModel
import com.fabnie.vendor.model.product.ProductResponseModel
import com.fabnie.vendor.repository.AddProductRepository
import com.fabnie.vendor.repository.ProductRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddProductViewModel
@Inject
constructor(private val addProductRepository: AddProductRepository): ViewModel() {

    val products: StateFlow<Response<AddProductResponseModel>>
    get() = addProductRepository.productsData

    fun addProducts(token: String, addProductRequestModel: AddProductRequestModel) = viewModelScope.launch {
        addProductRepository.addProducts(token = token,addProductRequestModel)
    }
    fun updateProducts(token: String, addProductRequestModel: AddProductRequestModel) = viewModelScope.launch {
        addProductRepository.updateProducts(token = token,addProductRequestModel)
    }
}