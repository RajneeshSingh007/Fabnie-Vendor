package com.fabnie.vendor.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fabnie.vendor.BottomBarScreen
import com.fabnie.vendor.R
import com.fabnie.vendor.data.Dashboard
import com.fabnie.vendor.ui.theme.GothamMedium
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.widget.Header

@Composable
fun DashboardScreen(navHostController: NavHostController) {
    val items = listOf(
        Dashboard(icon = painterResource(id = R.drawable.ic_my_earnings), title = "My Earnings"),
        Dashboard(icon = painterResource(id = R.drawable.ic_bundle), title = "Manage Orders"),
        Dashboard(icon = painterResource(id = R.drawable.ic_manage_orders), title = "My Products"),
        Dashboard(icon = painterResource(id = R.drawable.ic_bundle), title = "Add a Product"),
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Header(title = "Dashboard", showIcon = false)
        Spacer(modifier = Modifier.height(10.dp))
        items.forEach {
            DashboardItem(item = it) {
                if (it.title == "My Earnings") {
                    navHostController.navigate(BottomBarScreen.Payments.route)
                } else if (it.title == "Manage Orders") {
                    navHostController.navigate(BottomBarScreen.Orders.route)
                } else if (it.title == "My Products") {
                    navHostController.navigate(BottomBarScreen.Products.route)
                } else if (it.title == "Add a Product") {
                    navHostController.navigate(BottomBarScreen.AddProduct.route)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun DashboardItem(item: Dashboard, onClick: () -> Unit) {
    Box(modifier = Modifier.padding(horizontal = 10.dp)) {
        Box(
            modifier = Modifier
                .coloredShadow(
                    color = Color(0xFFfE8C05),
                    borderRadius = 15.dp, alpha = 0.3f, shadowRadius = 4.dp
                ),
//            .padding(vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clickable {
                        onClick()
                    },
                backgroundColor = Color(0xfffff0dd),
                shape = RoundedCornerShape(15.dp),
                elevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 15.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .background(color = Color(0xFFfe8c05), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = item.icon, contentDescription = null,
                            modifier = Modifier.size(40.dp),
                            colorFilter = ColorFilter.tint(color = Color.White)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = item.title,
                        style = TextStyle(
                            fontFamily = FontFamily(fonts = GothamMedium),
                            fontSize = 18.sp
                        )
                    )
                }
            }
        }
    }

}