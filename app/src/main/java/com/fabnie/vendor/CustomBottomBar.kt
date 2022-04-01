package com.fabnie.vendor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fabnie.vendor.data.TabItem
import com.fabnie.vendor.data.TabType

@Composable
fun CustomBottomBar(navHostController: NavHostController) {
    val currentTabIndex = remember { mutableStateOf(0) }
    val tabs = listOf(
        TabItem(icon = painterResource(id = R.drawable.ic_home), type = TabType.HOME),
        TabItem(icon = painterResource(id = R.drawable.ic_bundle), type = TabType.PRODUCT),
        TabItem(icon = painterResource(id = R.drawable.ic_profile), type = TabType.PERSONAL),
        TabItem(icon = painterResource(id = R.drawable.ic_menu), type = TabType.PROFILE)
    )
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.White, shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        tabs.forEachIndexed { i, tab->
            IconButton(onClick = {
                when(tab.type){
                    TabType.HOME -> navHostController.navigate(BottomBarScreen.Dashboard.route)
                    TabType.PRODUCT -> navHostController.navigate(BottomBarScreen.Products.route)
                    TabType.PERSONAL -> navHostController.navigate(BottomBarScreen.PersonalData.route)
                    TabType.PROFILE -> navHostController.navigate(BottomBarScreen.Profile.route)
                }
                currentTabIndex.value = i
            }) {
                Icon(
                    painter = tab.icon,
                    contentDescription = null, modifier = Modifier.size(30.dp),
                    tint = if(currentTabIndex.value == i) Color(0xFFfE8C05) else Color(0xFFF3C994)
                )
            }
        }
    }
}
