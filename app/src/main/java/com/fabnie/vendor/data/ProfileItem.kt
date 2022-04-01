package com.fabnie.vendor.data

import androidx.compose.ui.graphics.painter.Painter

enum class ProfileType {
    DASHBOARD,
    ORDERS,
    PAYMENTS,
    NOTIFICATIONS,
    BANK_DETAILS,
    SETTINGS
}

data class ProfileItem(val icon: Painter, val title: ProfileType)
