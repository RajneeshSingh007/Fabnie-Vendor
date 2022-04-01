package com.fabnie.vendor.model.bankdetail

data class UpdateBankDetailRequestModel(
    val bank_name: String,
    val holder_name: String,
    val ifsc_code: String,
    val account_number: String
)
