package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.bankdetail.BankDetailResponseModel
import com.fabnie.vendor.repository.BankDetailRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BankDetailViewModel
@Inject
constructor(private val bankDetailRepository: BankDetailRepository): ViewModel() {

    val bankDetail: StateFlow<Response<BankDetailResponseModel>>
        get() = bankDetailRepository.bankDetailData

    fun getBankDetail(token: String) = viewModelScope.launch {
        bankDetailRepository.getBankDetail(token = token);
    }
}