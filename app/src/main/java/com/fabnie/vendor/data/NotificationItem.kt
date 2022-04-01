package com.fabnie.vendor.data

import androidx.compose.ui.graphics.painter.Painter
import java.util.*

enum class OrderState {
    DELIVERED,
    PLACED,
    NEW_PRODUCT
}

data class NotificationItem(
    val productImage: Painter,
    val title: String,
    val status: OrderState,
    val time: Calendar,
    )
