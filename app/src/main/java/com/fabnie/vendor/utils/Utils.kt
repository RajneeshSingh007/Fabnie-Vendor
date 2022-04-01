package com.fabnie.vendor.utils

import androidx.compose.ui.graphics.Color
import com.fabnie.vendor.data.Availability
import com.fabnie.vendor.data.OrderState
import com.fabnie.vendor.data.OrderStatus
import com.fabnie.vendor.data.ProfileType
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun getFormattedDate(calendar: Calendar): String{
        val monthStr:String = when(calendar.get((Calendar.MONTH))){
            0 -> "Jan"
            1 -> "Feb"
            2 -> "Mar"
            3 -> "Apr"
            4 -> "May"
            5 -> "Jun"
            6 -> "Jul"
            7 -> "Aug"
            8 -> "Sep"
            9 -> "Oct"
            10 -> "Nov"
            11 -> "Dec"
            else -> "Unknown"
        }
        val year:Int = when(calendar.get(Calendar.YEAR)){
            2021 -> 21
            2022 -> 22
            2023 -> 23
            2024 -> 24
            2025 -> 25
            2026 -> 26
            2027 -> 27
            2028 -> 28
            2029 -> 29
            2030 -> 30
            2031 -> 31
            2032 -> 32
            2033 -> 33
            else -> calendar.get(Calendar.YEAR)
        }
        val date = "${calendar.get(Calendar.DATE)} $monthStr $year";
        return  date;
    }

    fun getTime(calendar: Calendar): String {
        return "${calendar.get(Calendar.HOUR_OF_DAY)}: ${calendar.get(Calendar.MINUTE)}"
    }

    fun getOrderStatus(orderStatus: OrderStatus): String {
        return when(orderStatus){
            OrderStatus.PROCESSING -> "Processing"
            OrderStatus.SHIPPED -> "Shipped"
            OrderStatus.IN_TRANSIT -> "In Transit"
            OrderStatus.DELIVERED -> "Delivered"
            OrderStatus.DEFAULT -> "Default"
        }
    }

    fun getProfileTitle(profileType: ProfileType): String {
        return when(profileType){
            ProfileType.DASHBOARD -> "Dashboard"
            ProfileType.ORDERS -> "Orders"
            ProfileType.PAYMENTS -> "Payments"
            ProfileType.NOTIFICATIONS -> "Notifications"
            ProfileType.BANK_DETAILS -> "Bank Details"
            ProfileType.SETTINGS -> "Settings"
        }
    }

    fun getProductStatus(availability: Availability): String {
        return  when(availability){
            Availability.IN_STOCK -> "In Stock"
            Availability.OUT_OF_STOCK -> "Out of Stock"
        }
    }

    fun getOrderState(orderState: OrderState): String{
        return when(orderState){
            OrderState.DELIVERED -> "Order Delivered!"
            OrderState.PLACED -> "Order Placed!"
            OrderState.NEW_PRODUCT -> "New Product Listed!"
        }
    }

    fun getProductStockInfo(qty: String):String {
        var value = "In Stock"
        if(qty == "" || qty == "0"){
            value = "Out of Stock"
        }
        return value
    }

    fun getDigits(number:String, prevData:String) : String{
        if("[0-9]+".toRegex().matches(number)){
            return number.take(1)
        }
        return ""
    }

    fun formatDate(date:String) : String{
        val sourceSplit = date.split(" ")
        val dateSplit = sourceSplit[0].split("-")
        val timeSplit = sourceSplit[1].split(":")
        val calendar = Calendar.getInstance()
        calendar.set(dateSplit[0].toInt(),dateSplit[1].toInt()-1,dateSplit[2].toInt(), timeSplit[0].toInt(),timeSplit[1].toInt(),timeSplit[2].toInt())
        return SimpleDateFormat("dd MMM yy HH:mm").format(calendar.time)
    }

    fun getOrderColor(status:String): Color {
        return when(status.toLowerCase()){
            "shipped" -> Color(0xFF03A9F4)
            "delivered" -> Color(0xFF4CAF50)
            "declined" -> Color.Red
            else -> Color(0xFFfe8c05)
        }
    }

    fun getMonth(month: Int): String {
        return when (month) {
            0 -> "January"
            1 -> "February"
            2 -> "March"
            3 -> "April"
            4 -> "May"
            5 -> "June"
            6 -> "July"
            7 -> "August"
            8 -> "September"
            9 -> "October"
            10 -> "November"
            11 -> "December"
            else -> "Unknown"
        }
    }

    fun isLettersOnly(value:String): Boolean {
        val len = value.length
        for (i in 0 until len) {
            if (!value[i].isLetter()) {
                return false
            }
        }
        return true
    }


}