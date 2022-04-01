package com.fabnie.vendor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabnie.vendor.model.category.CategoryResponseModel
import com.fabnie.vendor.model.profile.ProfileResponseModel
import com.fabnie.vendor.repository.CategoryRepository
import com.fabnie.vendor.repository.ProfileRepository
import com.fabnie.vendor.repository.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(private val profileRepository: ProfileRepository): ViewModel() {


    val profile: StateFlow<Response<ProfileResponseModel>>
        get() = profileRepository.profileData

    fun getProfile(token: String) = viewModelScope.launch {
        profileRepository.getProfile(token = token);
    }
}