package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.model.subcategory.SubCategoryResponseModel
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class SubCategoryRepository
@Inject
constructor(private val apiService: ApiService) {

    private val subCategoriesFlowData =
        MutableStateFlow<Response<SubCategoryResponseModel>>(Response.Empty())
    val subCategoriesData: StateFlow<Response<SubCategoryResponseModel>>
        get() = subCategoriesFlowData

    suspend fun getSubCategories(id: Int) {
        try {
            subCategoriesFlowData.emit(Response.Loading())
            val result = apiService.getSubCategories(id)
            Log.d("[SubCategories]", "Success- ${result.body()}")
            if (result.body() != null) {
                subCategoriesFlowData.emit(Response.Success(data = result.body()))
            } else {
                subCategoriesFlowData.emit(Response.Error(error = "Something went wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[SubCategories]", "Exception- ${e.message.toString()}")
            subCategoriesFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }
}