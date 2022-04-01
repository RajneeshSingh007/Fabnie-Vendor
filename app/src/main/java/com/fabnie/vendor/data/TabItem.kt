package com.fabnie.vendor.data

import androidx.compose.ui.graphics.painter.Painter

enum class TabType {
    HOME,
    PRODUCT,
    PERSONAL,
    PROFILE
}

data class TabItem(val icon: Painter, val type: TabType)
