package com.fabnie.vendor.model.subcategory

data class SubCategoryResponseModel(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)