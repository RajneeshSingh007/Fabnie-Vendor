package com.fabnie.vendor.model.userproduct

data class UserProductResponseModel(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean
)