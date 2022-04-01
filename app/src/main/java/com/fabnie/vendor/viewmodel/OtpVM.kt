package com.fabnie.vendor.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.LoginRequestModel
import com.fabnie.vendor.model.Otp
import com.fabnie.vendor.model.login.LoginResponseModel
import com.fabnie.vendor.repository.LoginRepository
import com.fabnie.vendor.repository.OtpRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpVM @Inject constructor(
    private val otpRepository: OtpRepository
): ViewModel() {

    val vendor: StateFlow<Response<Otp>>
        get() = otpRepository.otpData

    fun sendOtp(mobileNumber:String) = viewModelScope.launch {
        otpRepository.sendOtp(mobileNumber)
    }
}