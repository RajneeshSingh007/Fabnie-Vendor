package com.fabnie.vendor.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.product.ProductResponseModel
import com.fabnie.vendor.model.userproduct.UserProductResponseModel
import com.fabnie.vendor.repository.ProductRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel
@Inject
constructor(private val productRepository: ProductRepository): ViewModel() {

    val products: StateFlow<Response<ProductResponseModel>>
    get() = productRepository.productsData

    val userProducts: StateFlow<Response<UserProductResponseModel>>
    get() = productRepository.userProductsData

    fun getProducts(token: String) = viewModelScope.launch {
        productRepository.getProducts(token = token)
    }

    fun getUserProducts(token: String) = viewModelScope.launch {
        productRepository.getUserProducts(token = token)
    }
}