package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.model.LoginRequestModel
import com.fabnie.vendor.model.login.LoginResponseModel
import com.fabnie.vendor.network.ApiService
import com.fabnie.vendor.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class LoginRepository
@Inject
constructor(private val apiService: ApiService) {

    private val loginFlowData = MutableStateFlow<Response<LoginResponseModel>>(Response.Empty())
    val vendorData: StateFlow<Response<LoginResponseModel>>
        get() = loginFlowData

    suspend fun loginUser(data: LoginRequestModel) {
        val appPref = BaseApplication.appContext.appPref
        try {
            loginFlowData.emit(Response.Loading())
            val result = apiService.loginUser(body = data)
            Log.d("[Login]", "Success- ${result.body()}")
            if (result.body() != null) {
                val responseModel: LoginResponseModel = result.body() as LoginResponseModel
                if (responseModel.status) {
                    appPref.putString(Constants.VENDOR_ID, responseModel.data.id.toString())
                    appPref.putString(Constants.TOKEN_EXPIRY, responseModel.expires_in.toString())
                    appPref.putString(Constants.ACCESS_TOKEN, responseModel.access_token)
                    appPref.putString(Constants.IS_LOGGEDIN, "true")
                    loginFlowData.emit(Response.Success(data = responseModel))
                } else {
                    loginFlowData.emit(Response.Error(error = "Failed to verify OTP"))
                }
            } else {
                loginFlowData.emit(Response.Error(error = "Something wents wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Login]", "Exception- ${e.message.toString()}")
            loginFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }


}

