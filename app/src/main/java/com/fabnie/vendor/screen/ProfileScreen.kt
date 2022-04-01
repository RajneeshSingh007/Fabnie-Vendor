package com.fabnie.vendor.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults.buttonColors
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.BottomBarScreen
import com.fabnie.vendor.R
import com.fabnie.vendor.data.ProfileItem
import com.fabnie.vendor.data.ProfileType
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.utils.Constants
import com.fabnie.vendor.utils.Utils
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.viewmodel.ProfileViewModel
import com.fabnie.vendor.widget.Header

@Composable
fun ProfileScreen(navController: NavHostController) {
    val profileViewModel: ProfileViewModel = hiltViewModel()
    val profileState = profileViewModel.profile.collectAsState().value
    val token = remember { mutableStateOf("") }
    val firstName = remember { mutableStateOf("") }

    var imageUrl: Painter = painterResource(R.drawable.fabnie_logo)

    LaunchedEffect(key1 = true) {
        val appPref = BaseApplication.appContext.appPref
        token.value = appPref.getString(Constants.ACCESS_TOKEN).toString()
        profileViewModel.getProfile(token = token.value)
    }

    when (profileState) {
        is Response.Success -> {
            val data = profileState.data?.data!!
            firstName.value = data.firstname
            imageUrl = rememberImagePainter("${Constants.USER_IMAGE_BASE_URL}${data.photo}")
        }
        else -> firstName.value = ""
    }

    val items = listOf(
        ProfileItem(icon = painterResource(id = R.drawable.ic_name), title = ProfileType.DASHBOARD),
        ProfileItem(
            icon = painterResource(id = R.drawable.ic_purchase_order),
            title = ProfileType.ORDERS
        ),
        ProfileItem(
            icon = painterResource(id = R.drawable.ic_payment),
            title = ProfileType.PAYMENTS
        ),
        ProfileItem(
            icon = painterResource(id = R.drawable.ic_notification),
            title = ProfileType.NOTIFICATIONS
        ),
        ProfileItem(
            icon = painterResource(id = R.drawable.ic_bank_details),
            title = ProfileType.BANK_DETAILS
        ),
        ProfileItem(
            icon = painterResource(id = R.drawable.ic_settings),
            title = ProfileType.SETTINGS
        )
    )
    Column(modifier = Modifier.fillMaxSize()) {
        Header(title = "Profile", showIcon = false)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
        Box(modifier = Modifier.padding(horizontal = 10.dp)) {
            Box(
                modifier = Modifier
                    .coloredShadow(
                        color = Color(0xFFfE8C05),
                        borderRadius = 20.dp, alpha = 0.3f, shadowRadius = 5.dp
                    )
                    .padding(vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier =
                    Modifier.padding(horizontal = 5.dp),
                    shape = RoundedCornerShape(CornerSize(20.dp)),
                    backgroundColor = Color(0xFFfff0dd),
                    elevation = 0.dp
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
//                        horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier =
                                Modifier
                                    .size(80.dp)
                                    .background(color = Color.White, shape = CircleShape)
                            ) {
                                Image(
                                    painter = imageUrl, contentDescription = null,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(80.dp)
                                )
                            }
                            Text(
                                text = "${firstName.value}",
                                modifier = Modifier.padding(start = 20.dp),
                                style = TextStyle(
                                    color = Color(color = 0XFFfe8c05),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily(fonts = GothamBold),
                                )
                            )
                        }
                        Divider()
                        items.forEachIndexed { index, i ->
                            ListItem(item = i) { profileType ->
                                when (profileType) {
                                    ProfileType.DASHBOARD -> navController.navigate(BottomBarScreen.Dashboard.route)
                                    ProfileType.ORDERS -> navController.navigate(BottomBarScreen.Orders.route)
                                    ProfileType.PAYMENTS -> navController.navigate(BottomBarScreen.Payments.route)
                                    ProfileType.NOTIFICATIONS -> navController.navigate(
                                        BottomBarScreen.Notifications.route
                                    )
                                    ProfileType.BANK_DETAILS -> navController.navigate(
                                        BottomBarScreen.BankDetails.route
                                    )
                                    ProfileType.SETTINGS -> navController.navigate(BottomBarScreen.Settings.route)
                                }
                            }
                            if (index != items.size - 1) {
                                Divider()
                            }
                        }

                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}

@Composable
fun ListItem(item: ProfileItem, onClick: (profileType: ProfileType) -> Unit) {
    Button(
        onClick = { onClick(item.title) },
        colors = buttonColors(backgroundColor = Color.Transparent),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
        ) {
            Icon(
                painter = item.icon, contentDescription = null,
                modifier = Modifier.size(25.dp),
                tint = Color(0XFFfe8c05)
            )
            Text(
                text = Utils.getProfileTitle(item.title),
                modifier = Modifier.padding(start = 15.dp),
                style = TextStyle(
                    color = Color(color = 0xFF3a3a3a),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(fonts = GothamBold),
                )
            )
        }
    }
}


