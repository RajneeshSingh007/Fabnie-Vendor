package com.fabnie.vendor.model

data class Otp(val statusCode:Int = 200, var loading:Boolean = false, val success: Boolean = false, val message: String = "", val otp: String = "") {}