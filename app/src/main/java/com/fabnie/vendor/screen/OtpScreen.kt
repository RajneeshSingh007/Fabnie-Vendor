package com.fabnie.vendor.screen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fabnie.vendor.R
import com.fabnie.vendor.Screen
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.ui.theme.GothamBook
import com.fabnie.vendor.ui.theme.GothamMedium
import com.fabnie.vendor.utils.Helpers
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.viewmodel.LoginViewModel
import com.fabnie.vendor.viewmodel.OtpVM
import com.fabnie.vendor.widget.CustomTextField
import com.fabnie.vendor.widget.ShowProgressDialog
import kotlinx.coroutines.launch

@Composable
fun OTPScreen(navController: NavHostController) {
    val viewModel: OtpVM = hiltViewModel()
    val context = navController.context as Activity
    val otp = remember { mutableStateOf("") }
    val state = viewModel.vendor.collectAsState(initial = Response.Loading()).value
    val coroutineScope = rememberCoroutineScope()
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = state, block = {
        coroutineScope.launch {
            when (state) {
                is Response.Empty -> {
                    isLoading.value = false
                }
                is Response.Loading -> {
                    isLoading.value = true
                }
                is Response.Success -> {
                    isLoading.value = false
                    if(state.data != null){
                        Helpers.showToast(
                            context = context,
                            "success",
                            "OTP sent successfully"
                        )
                        navController.navigate("${Screen.OtpVerifyScreen.route}/${otp.value}/${state.data.otp}")
                    }
                }
                is Response.Error -> {
                    Helpers.showToast(
                        context = context,
                        "error",
                        state.error.toString()
                    )
                    Log.d("RES", "ERROR-${state.error.toString()}")
                }
            }
        }
    })

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Image(
            painter = painterResource(id = R.drawable.fabnie_logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterHorizontally)
        )

        Text(
            text = "Login to your account",
            style = TextStyle(
                color = Color(0xff707070),
                fontSize = 16.sp
            ),
            fontFamily = FontFamily(fonts = GothamMedium)
        )
        Spacer(modifier = Modifier.height(50.dp))
        CustomTextField(
            placeholder = "Enter your mobile number",
            value = otp.value,
            onValueChange = { it -> otp.value = it.filter { it.isDigit() } },
            icon = painterResource(id = R.drawable.ic_phone),
            keyboardType = KeyboardType.Number,
            maxLength = 10
        )
        Spacer(modifier = Modifier.height(50.dp))
        Text(modifier = Modifier.padding(horizontal = 40.dp),
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black,
                        fontFamily = FontFamily(fonts = GothamBook)
                    )
                ) {
                    append("Enter mobile number to get OTP")
                }
//                withStyle(
//                    style = SpanStyle(
//                        color = Color.Black,
//                        fontFamily = FontFamily(fonts = GothamBook)
//                    )
//                ) {
//                    append(" sent to your Mobile Number")
//                }
            }
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Box(
                modifier = Modifier
                    .coloredShadow(color = Color(0xFFfE8C05), borderRadius = 5.dp, alpha = 0.2f)
                    .clip(shape = RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier.background(color = Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            if (otp.value == "") {
                                Helpers.showToast(context, "error", "Please, Enter Mobile Number")
                            } else if (!"[0-9]+".toRegex().matches(otp.value)) {
                                Helpers.showToast(
                                    context,
                                    "error",
                                    "Please, Enter Valid Mobile Number"
                                )
                            } else if (otp.value.length != 10) {
                                Helpers.showToast(
                                    context,
                                    "error",
                                    "Please, Enter 10 Digit Mobile Number"
                                )
                            } else {
                                viewModel.sendOtp(otp.value)
                            }
                        }, modifier = Modifier
                            .height(50.dp)
                            .clip(shape = RoundedCornerShape(25.dp)),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        Text(
                            text = "Get OTP ",
                            style = TextStyle(
                                fontFamily = FontFamily(fonts = GothamBold),
                                fontSize = 18.sp,
                                letterSpacing = 2.sp
                            )
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow),
                            contentDescription = "right_arrow",
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

        }

        ShowProgressDialog(showDialog = isLoading.value) {
            isLoading.value = false
        }
    }

}
