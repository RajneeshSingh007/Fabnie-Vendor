package com.fabnie.vendor.model.addproduct

data class AddProductRequestModel(
    var id: Int = -1,
    val user_id: Int = -1,
    var product_id: Int = -1,
    val standard: String= "",
    val price:String = "",
    val quantity:String = "",
    //val price: List<String> = ArrayList<String>(0),
    val price_measure: List<String> = ArrayList<String>(0),
    val size: List<String> = ArrayList<String>(0),
    //val quantity: List<String> = ArrayList<String>(0),
    val application:List<String> = ArrayList<String>(0),
)
