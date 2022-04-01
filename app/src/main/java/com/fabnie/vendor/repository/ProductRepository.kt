package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.model.product.ProductResponseModel
import com.fabnie.vendor.model.userproduct.UserProductResponseModel
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class ProductRepository
@Inject
constructor(private val apiService: ApiService) {

    private val productsFlowData =
        MutableStateFlow<Response<ProductResponseModel>>(Response.Empty())
    private val userproductsFlowData =
        MutableStateFlow<Response<UserProductResponseModel>>(Response.Empty())

    val userProductsData: StateFlow<Response<UserProductResponseModel>>
        get() = userproductsFlowData

    val productsData: StateFlow<Response<ProductResponseModel>>
        get() = productsFlowData

    suspend fun getProducts(token: String) {
        Log.d("AuthToken", "$token")
        try {
            productsFlowData.emit(Response.Loading())
            val result = apiService.getProducts(authorization = "Bearer $token")
            Log.d("[Products]", "${result}Success- ${result.body()}")
            if (result.body() != null) {
                productsFlowData.emit(Response.Success(data = result.body()))
            } else {
                productsFlowData.emit(Response.Error(error = "Something wents wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Products]", "Exception- ${e.message.toString()}")
            productsFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }

    suspend fun getUserProducts(token: String) {
        Log.d("AuthToken", "$token")
        try {
            userproductsFlowData.emit(Response.Loading())
            val result = apiService.getUserProducts(authorization = "Bearer $token")
            Log.d("[Products]", "${result}Success- ${result.body()}")
            if (result.body() != null) {
                userproductsFlowData.emit(Response.Success(data = result.body()))
            } else {
                userproductsFlowData.emit(Response.Error(error = "Something wents wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Products]", "Exception- ${e.message.toString()}")
            userproductsFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }
}