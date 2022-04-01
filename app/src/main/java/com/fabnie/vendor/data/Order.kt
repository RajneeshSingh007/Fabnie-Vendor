package com.fabnie.vendor.data

import androidx.compose.ui.graphics.painter.Painter
import java.util.*

enum class OrderStatus {
    PROCESSING,
    SHIPPED,
    IN_TRANSIT,
    DELIVERED,
    DEFAULT
}

data class Order(
    val productImage: Painter,
    val orderId: String,
    val quantity: Int,
    val orderStatus: OrderStatus,
    val productName: String,
    val productPrice: Double,
    )
