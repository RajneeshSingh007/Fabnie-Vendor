package com.fabnie.vendor.model.product

data class Data(val id: Int = 0, val user_id: String?, val product_id: String="",
                /*val size: String ="", val category_id: String?,*/ val quantity: String = "",/* val application: String = "", val standard: String = "", */val price: String?,/* val price_measure: String="",*/ val user: User?, val product: Product?)