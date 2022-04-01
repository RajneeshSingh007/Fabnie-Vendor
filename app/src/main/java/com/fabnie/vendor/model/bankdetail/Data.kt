package com.fabnie.vendor.model.bankdetail

data class Data(
    val account_number: String,
    val bank_name: String,
    val holder_name: String,
    val id: Int,
    val ifsc_code: String
)