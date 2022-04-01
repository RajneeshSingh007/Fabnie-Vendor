package com.fabnie.vendor.model.profile

data class ProfileResponseModel(
    val data: Data = Data(),
    val message: String = "",
    val success: Boolean = false
)