package com.fabnie.vendor.model.orders

import com.squareup.moshi.JsonClass

data class AddressData(val address_id:Int = 0,val user_id: Int = 0, val full_name: String="", val phone: String="", val pincode: String="", val address1: String="", val address2: String="", val city: String="", val state: String="", val id: Int=0)

data class Order (

    val address : AddressData,
    val totalQty : String,
    val address_id : Int,
    val created_at : String,
    val tax : Int,
    val shipping : String,
    val updated_at : String,
    val user_id : Int,
    val vendor_id : Int,
    val final_total : Int,
    val sub_total : Int,
    val id : Int,
    val payment_method : String,
    val txnid : String

)


data class Products (

//    val trending : Int,
//    val big : Int,
//    val featured : Int,
//    val bust_size : String,
//    val is_catalog : Int,
//    val ship : String,
//    val type : String,
//    val subcategory_id : Int,
//    val features : String,
    val price : Int,
    val details : String,
    val id : Int,
//    val whole_sell_discount : String,
//    val sku : String,
//    val stock : String,
//    val slug : String,
    val thumbnail : String,
//    val tags : String,
//    val catalog_id : Int,
//    val meta_description : String,
//    val license : String,
//    val measure : String,
//    val size : Array<String> = Array<String>(),
//    val user_id : Int,
//    val childcategory_id : String,
    val name : String,
//    val size_price : String,
//    val region : String,
//    val status : Int,
//    val youtube : String,
//    val color : String,
//    val link : String,
//    val created_at : String,
//    val best : Int,
//    val whole_sell_qty : String,
//    val meta_tag : String,
//    val hot : Int,
//    val colors : String,
//    val platform : String,
//    val is_discount : Int,
//    val pieces : Int,
//    val licence_type : String,
//    val file : String,
//    val category_id : Int,
//    val top : Int,
//    val updated_at : String,
//    val waist_size : String,
//    val views : Int,
//    val policy : String,
//    val latest : Int,
//    val affiliate_link : String,
//    val discount_date : String,
//    val is_meta : Int,
//    val product_condition : Int,
//    val photo : String,
//    val sale : Int,
//    val product_type : String,
//    val license_qty : String,
//    val size_qty : String,
//    val previous_price : String

)


data class OrderListData (

    val quantity : Int = 0,
    val measure : String = "",
    val size : String = "",
    val price : String = "",
    val vendor_id : Int= 0,
    val product_id : Int= 0,
    val sub_total : String = "",
    val id : Int= 0,
    val order_id : Int= 0,
    var status : String="Pending",
    val order : Order? =null,
    val products : Products? =null,
)

data class OrderResponseModel (
    var data: List<OrderListData> = ArrayList<OrderListData>(0),
    val success : Boolean = false,
    val message : String = "",
)

data class OrderDetailResponseModel(val success: Boolean = false,val message: String = "", var data: OrderListData = OrderListData(),val statusCode:Int = 0)
