package com.fabnie.vendor.screen

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.data.Settings
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.ui.theme.GothamMedium
import com.fabnie.vendor.utils.Constants
import com.fabnie.vendor.utils.Helpers
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.viewmodel.NotificationViewModel
import com.fabnie.vendor.widget.Header
import com.fabnie.vendor.widget.ShowProgressDialog
import kotlinx.coroutines.*

@Composable
fun SettingsScreen(navHostController: NavHostController, resetRoute: () -> Unit) {
    val context = LocalContext.current as Activity
    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val state = notificationViewModel.notifications.collectAsState().value
    val showDialog = remember { mutableStateOf(false) }

    val notificationState = remember { mutableStateOf(true) }
    val token = remember { mutableStateOf("") }
    val isNotificationChecked = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = "SettingsScreen") {
        val appPref = BaseApplication.appContext.appPref
        token.value = appPref.getString(Constants.ACCESS_TOKEN).toString()
        notificationViewModel.getNotifications(token.value)
    }

    when (state) {
        is Response.Empty -> {
        }
        is Response.Loading -> {
        }
        is Response.Success -> {
            val response = state.data
            if (response != null && response.success) {
                notificationState.value = response.data.push_notification == 1
                if (isNotificationChecked.value) {
                    isNotificationChecked.value = false
                    val type = if (response.success) {
                        "success"
                    } else {
                        "error"
                    }
                    Helpers.showToast(context, type, response.message)
                }
            }
        }
        is Response.Error -> {
        }
    }

    val items = listOf(
        Settings(title = "App Push Notification", showSwitch = true),
        Settings(title = "Log Out")
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Header(title = "Settings", showIcon = false)
        Spacer(modifier = Modifier.height(20.dp))
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
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 5.dp),
                    shape = RoundedCornerShape(CornerSize(20.dp)),
                    backgroundColor = Color(0xFFfff0dd),
                    elevation = 0.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        items.forEachIndexed { index, i ->
                            Button(
                                onClick = {
                                    when (index) {
                                        1 -> {
                                            isNotificationChecked.value = false
                                            showDialog.value = true
                                        }
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                                elevation = ButtonDefaults.elevation(
                                    defaultElevation = 0.dp,
                                    pressedElevation = 0.dp,
                                    disabledElevation = 0.dp
                                )
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 20.dp, vertical = 25.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = i.title, style = TextStyle(
                                            color = Color.Black,
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily(fonts = GothamMedium),
                                        )
                                    )
                                    if (i.showSwitch) {
                                        Switch(
                                            checked = notificationState.value,
                                            onCheckedChange = {
                                                val reverseBool = !notificationState.value
                                                notificationState.value = reverseBool
                                                var stateNotify: Int = 0
                                                if (reverseBool) {
                                                    stateNotify = 1
                                                } else {
                                                    stateNotify = 0
                                                }
                                                isNotificationChecked.value = true
                                                notificationViewModel.pushNotifications(
                                                    token.value,
                                                    stateNotify
                                                )
                                            })
                                    }
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

    }
    ShowProgressDialog(showDialog = isNotificationChecked.value) {
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            title = {
                Text(text = "Logout")
            },
            text = {
                Text("Are you sure you want to logout?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                        GlobalScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                            logout(navHostController) {
                                resetRoute()
                            }
                        }
                    }) {
                    Text("Ok")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog.value = false
                    }) {
                    Text("Cancel")
                }
            }
        )
    }
}


suspend fun logout(navController: NavHostController, navigate: () -> Unit) {
    val appPref = BaseApplication.appContext.appPref
    withContext(Dispatchers.Main) {
        appPref.putString(Constants.VENDOR_ID, "")
        appPref.putString(Constants.TOKEN_EXPIRY, "")
        appPref.putString(Constants.ACCESS_TOKEN, "")
        appPref.putString(Constants.IS_LOGGEDIN, "")
        navigate()
    }
}