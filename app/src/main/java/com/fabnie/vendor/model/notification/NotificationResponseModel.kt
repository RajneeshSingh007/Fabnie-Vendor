package com.fabnie.vendor.model.notification

data class NotificationResponseModel(
    val `data`: Data,
    val message: String,
    val success: Boolean
)