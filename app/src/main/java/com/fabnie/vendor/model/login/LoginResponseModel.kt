package com.fabnie.vendor.model.login

data class LoginResponseModel(
    val access_token: String = "",
    val `data`: Data,
    val expires_in: Int = 0,
    val status: Boolean = false,
    val token_type: String = ""
)