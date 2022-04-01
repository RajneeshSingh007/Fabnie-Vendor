package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.model.bankdetail.BankDetailResponseModel
import com.fabnie.vendor.model.bankdetail.UpdateBankDetailRequestModel
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class BankDetailRepository
@Inject
constructor(private val apiService: ApiService) {

    //Get
    private val bankDetailFlowData =
        MutableStateFlow<Response<BankDetailResponseModel>>(Response.Empty())
    val bankDetailData: StateFlow<Response<BankDetailResponseModel>>
        get() = bankDetailFlowData

    //Update
    private val updateBankDetailFlowData =
        MutableStateFlow<Response<BankDetailResponseModel>>(Response.Empty())
    val updateBankDetailData: StateFlow<Response<BankDetailResponseModel>>
        get() = updateBankDetailFlowData

    suspend fun getBankDetail(token: String) {
        Log.d("AuthToken", "$token")
        try {
//            bankDetailFlowData.emit(Response.Loading())
            bankDetailFlowData.value = Response.Loading()
            val result = apiService.getBankDetail(authorization = "Bearer $token")
            Log.d("[BankDetail]", "Success- ${result.body()}")
            if (result.body() != null) {
                bankDetailFlowData.value = Response.Success(data = result.body())
//                bankDetailFlowData.emit(Response.Success(data = result.body()))
            }
        } catch (e: Exception) {
            Log.d("[BankDetail]", "Exception- ${e.message.toString()}")
            bankDetailFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }

    suspend fun updateBankDetail(token: String, data: UpdateBankDetailRequestModel) {
        Log.d("AuthToken", "$token")
        try {
            updateBankDetailFlowData.emit(Response.Loading())
            val result = apiService.updateBankDetail(authorization = "Bearer $token", body = data)
            Log.d("[UpdateBankDetail]", "Success- ${result.body()}")
            if (result.body() != null) {
                updateBankDetailFlowData.emit(Response.Success(data = result.body()))
            } else {
                updateBankDetailFlowData.emit(Response.Error(error = "Something went wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[UpdateBankDetail]", "Exception- ${e.message.toString()}")
            updateBankDetailFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }
}