package com.fabnie.vendor.data

import androidx.compose.ui.graphics.painter.Painter

data class PaymentItem(
    val icon: Painter,
    val title: String,
    val showIcon: Boolean = true
)
