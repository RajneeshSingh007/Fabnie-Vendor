package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.category.CategoryResponseModel
import com.fabnie.vendor.model.profile.ProfileResponseModel
import com.fabnie.vendor.model.profile.UpdateProfileRequestModel
import com.fabnie.vendor.repository.CategoryRepository
import com.fabnie.vendor.repository.ProfileRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateProfileViewModel
@Inject
constructor(private val profileRepository: ProfileRepository): ViewModel() {

    val updateProfile: StateFlow<Response<ProfileResponseModel>>
    get() = profileRepository.updateProfileData

    fun updateProfile(token: String, data: UpdateProfileRequestModel) = viewModelScope.launch {
        profileRepository.updateProfile(token = token, data = data);
    }
}