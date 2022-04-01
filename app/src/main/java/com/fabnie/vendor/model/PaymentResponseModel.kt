package com.fabnie.vendor.model

data class PaymentResponseModel(val success: Boolean = false, val message: String = "", val data: PaymentData = PaymentData())

data class PaymentData(val current_month_data: Int = 0, val current_week_data: Int = 0)
