package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.model.addproduct.AddProductRequestModel
import com.fabnie.vendor.model.addproduct.AddProductResponseModel
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AddProductRepository
@Inject
constructor(private val apiService: ApiService) {

    private val productsFlowData =
        MutableStateFlow<Response<AddProductResponseModel>>(Response.Empty())
    val productsData: StateFlow<Response<AddProductResponseModel>>
        get() = productsFlowData

    suspend fun addProducts(token: String, addProductRequestModel: AddProductRequestModel) {
        Log.d("AuthToken", "$token")
        try {
            productsFlowData.emit(Response.Loading())
            val result =
                apiService.addProduct(authorization = "Bearer $token", addProductRequestModel)
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

    suspend fun updateProducts(token: String, addProductRequestModel: AddProductRequestModel) {
        Log.d("[Products]", "${addProductRequestModel}")
        try {
            productsFlowData.emit(Response.Loading())
            val result =
                apiService.updateProduct(authorization = "Bearer $token", addProductRequestModel)
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
}