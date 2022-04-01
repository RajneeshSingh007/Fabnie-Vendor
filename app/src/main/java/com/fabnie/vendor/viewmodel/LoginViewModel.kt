package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.LoginRequestModel
import com.fabnie.vendor.model.login.LoginResponseModel
import com.fabnie.vendor.repository.LoginRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository
    ): ViewModel() {

    val vendor: StateFlow<Response<LoginResponseModel>>
    get() = loginRepository.vendorData

    fun loginUser(data: LoginRequestModel) = viewModelScope.launch {
        loginRepository.loginUser(data = data);
    }
}