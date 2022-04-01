package com.fabnie.vendor.model.category

data class Data(
    val id: Int,
    val image: String,
    val is_featured: Int,
    val name: String,
    val photo: String,
    val show_in_home: Int,
    val slug: String,
    val status: Int
)