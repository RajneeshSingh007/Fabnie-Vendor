package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.bankdetail.BankDetailResponseModel
import com.fabnie.vendor.model.bankdetail.UpdateBankDetailRequestModel
import com.fabnie.vendor.model.category.CategoryResponseModel
import com.fabnie.vendor.model.profile.ProfileResponseModel
import com.fabnie.vendor.repository.BankDetailRepository
import com.fabnie.vendor.repository.CategoryRepository
import com.fabnie.vendor.repository.ProfileRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateBankDetailViewModel
@Inject
constructor(private val bankDetailRepository: BankDetailRepository): ViewModel() {

    val updateBankDetail: StateFlow<Response<BankDetailResponseModel>>
    get() = bankDetailRepository.updateBankDetailData

    fun updateBankDetail(token: String, data: UpdateBankDetailRequestModel) = viewModelScope.launch {
        bankDetailRepository.updateBankDetail(token = token, data = data);
    }
}