package com.fabnie.vendor.model

data class LoginRequestModel(val phone: String, val otp: String, val is_vendor:Int = 1)
