package com.fabnie.vendor.model

data class MeasurementResponseModel(
    val `data`: List<Data>,
    val message: String,
    val success: Boolean) {
}

data class Data(
    val id: Int=-1,
    val name: String="",
)