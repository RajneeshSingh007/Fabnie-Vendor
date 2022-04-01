package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.model.category.CategoryResponseModel
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class CategoryRepository
@Inject
constructor(private val apiService: ApiService) {

    private val categoriesFlowData =
        MutableStateFlow<Response<CategoryResponseModel>>(Response.Empty())
    val categoriesData: StateFlow<Response<CategoryResponseModel>>
        get() = categoriesFlowData

    suspend fun getCategories() {
        try {
            categoriesFlowData.emit(Response.Loading())
            val result = apiService.getCategories()
            Log.d("[Categories]", "Success- ${result.body()}")
            if (result.body() != null) {
                categoriesFlowData.emit(Response.Success(data = result.body()))
            } else {
                categoriesFlowData.emit(Response.Error(error = "Something went wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Categories]", "Exception- ${e.message.toString()}")
            categoriesFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }
}