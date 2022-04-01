package com.fabnie.vendor.data

import androidx.compose.ui.graphics.painter.Painter

enum class Availability {
    IN_STOCK,
    OUT_OF_STOCK
}

data class Product(
    val id: String,
    val name: String,
    val price: String,
    val availability:Availability,
    val image: Painter

)
