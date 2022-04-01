package com.fabnie.vendor

enum class TabType {
    DASHBOARD,
    PRODUCTS,
    PERSONAL_DATA,
    PROFILE,
    PAYMENTS,
    ORDERS,
    ORDERSTATUS,
    SETTINGS,
    BANK_DETAILS,
    NOTIFICATIONS,
    ADD_PRODUCT
}

sealed class BottomBarScreen(
    val route: String,
    val id: TabType,
    ) {
    object Dashboard: BottomBarScreen(route = "dashboard", id = TabType.DASHBOARD)
    object Products: BottomBarScreen(route = "products", id = TabType.PRODUCTS)
    object PersonalData: BottomBarScreen(route = "personal_data", id = TabType.PERSONAL_DATA)
    object Profile: BottomBarScreen(route = "profile", id = TabType.PROFILE)
    object Payments: BottomBarScreen(route = "my_earnings", id = TabType.PAYMENTS)
    object BankDetails: BottomBarScreen(route = "bank_details", id = TabType.BANK_DETAILS)
    object Notifications: BottomBarScreen(route = "notification", id = TabType.NOTIFICATIONS)
    object OrderStatus: BottomBarScreen(route = "OrderStatus", id = TabType.ORDERSTATUS)
    object Orders: BottomBarScreen(route = "orders", id = TabType.ORDERS)
    object Settings: BottomBarScreen(route = "settings", id = TabType.SETTINGS)
    object AddProduct: BottomBarScreen(route = "add_product", id = TabType.ADD_PRODUCT)
}
