package com.fabnie.vendor.model.category

data class CategoryResponseModel(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)