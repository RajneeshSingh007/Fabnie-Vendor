package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.model.LoginRequestModel
import com.fabnie.vendor.model.Otp
import com.fabnie.vendor.model.login.LoginResponseModel
import com.fabnie.vendor.network.ApiService
import com.fabnie.vendor.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

class OtpRepository
@Inject
constructor(private val apiService: ApiService) {

    private val otpFlowData = MutableStateFlow<Response<Otp>>(Response.Empty())
    val otpData: StateFlow<Response<Otp>>
        get() = otpFlowData

    suspend fun sendOtp(mobileNumber:String) {
        try {
            otpFlowData.emit(Response.Loading())
            val mobile: RequestBody = RequestBody.create(MediaType.parse("text/plain"), mobileNumber)
            val result = apiService.sendOtp(mobile)
            if (result.body() != null) {
                val responseModel: Otp = result.body() as Otp
                if (responseModel.success) {
                    Log.d("[OTP]", responseModel.otp)
                    otpFlowData.emit(Response.Success(data = responseModel))
                } else {
                    otpFlowData.emit(Response.Error(error = responseModel.message))
                }
            } else {
                otpFlowData.emit(Response.Error(error = "Something went wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[OTP]", "Exception- ${e.message.toString()}")
            otpFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }


}