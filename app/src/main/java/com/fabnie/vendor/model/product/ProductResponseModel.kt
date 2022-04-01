package com.fabnie.vendor.model.product

data class ProductResponseModel(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)