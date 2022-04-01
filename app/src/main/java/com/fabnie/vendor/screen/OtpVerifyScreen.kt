package com.fabnie.vendor.screen

import android.app.Activity
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fabnie.vendor.R
import com.fabnie.vendor.Screen
import com.fabnie.vendor.model.LoginRequestModel
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.ui.theme.GothamBook
import com.fabnie.vendor.utils.Helpers
import com.fabnie.vendor.utils.Utils
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.viewmodel.LoginViewModel
import com.fabnie.vendor.viewmodel.OtpVM
import com.fabnie.vendor.widget.OtpInput
import com.fabnie.vendor.widget.ShowProgressDialog
import kotlinx.coroutines.launch

@Composable
fun OtpVerifyScreen(navController: NavHostController, mobile: String? = "", otpNumber: String? = "", onClick: () -> Unit) {
    val viewModel: LoginViewModel = hiltViewModel()
    val otp1 = remember { mutableStateOf("") }
    val otp2 = remember { mutableStateOf("") }
    val otp3 = remember { mutableStateOf("") }
    val otp4 = remember { mutableStateOf("") }
    val focusRequesters: List<FocusRequester> = remember { (0 until 4).map { FocusRequester() } }
    val state = viewModel.vendor.collectAsState(initial = Response.Loading()).value
    val isLoading = remember { mutableStateOf(false) }
    val context = navController.context as Activity
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    val finalOTP = remember { mutableStateOf("") }
    val otpviewModel: OtpVM = hiltViewModel()
    val otpState = otpviewModel.vendor.collectAsState(initial = Response.Loading()).value

    LaunchedEffect(key1 = "OtpVerifyScreen"){
        finalOTP.value = otpNumber.toString()
    }

    val backCallback = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.popBackStack()
            }
        }
    }

    LaunchedEffect(key1 = otpState, block = {
        coroutineScope.launch {
            when (otpState) {
                is Response.Empty -> {
                    isLoading.value = false
                }
                is Response.Loading -> {
                    isLoading.value = true
                }
                is Response.Success -> {
                    isLoading.value = false
                    if(otpState.data != null){
                        Log.e("finalOTP", otpState.data.otp)
                        finalOTP.value = otpState.data.otp
                    }
                }
                is Response.Error -> {
                    Helpers.showToast(
                        context = context,
                        "error",
                        otpState.error.toString()
                    )
                    Log.d("RES", "ERROR-${otpState.error.toString()}")
                }
            }
        }
    })

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
                    Helpers.showToast(
                        context = context,
                        "success",
                        "LoggedIn Successfully"
                    )
                    onClick()
                }
                is Response.Error -> {
                    isLoading.value = false
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

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {

        IconButton(onClick = {
            navController.popBackStack()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "right_arrow",
                modifier = Modifier.size(15.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .size(125.dp)
                .coloredShadow(
                    color = Color(0xFFfE8C05),
                    borderRadius = 65.dp, alpha = 0.3f, shadowRadius = 10.dp
                )
                .clip(shape = RoundedCornerShape(65.dp)),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(color = Color(0xFFfff0dd), shape = RoundedCornerShape(65.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_otp),
                    contentDescription = "otp",
                    modifier = Modifier
                        .size(80.dp)
                        .padding(end = 12.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = "OTP Verification",
            style = TextStyle(
                color = Color.Black,
                fontSize = 22.sp,
                fontFamily = FontFamily(fonts = GothamBold)
            )
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 40.dp)
                .padding(top = 20.dp),
            text = "We have sent OTP on your number",
            style = TextStyle(
                color = Color(0XFF6a6a6a),
                fontSize = 16.sp,
                fontFamily = FontFamily(fonts = GothamBook)
            )
        )
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .padding(horizontal = 48.dp, vertical = 20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OtpInput(
                value = otp1.value, onValueChange = {
                    otp1.value = Utils.getDigits(it, otp1.value)
                }, focusRequester = focusRequesters[0],
                nextFocusRequester = focusRequesters[1]
            )

            Spacer(modifier = Modifier.width(10.dp))

            OtpInput(
                value = otp2.value, onValueChange = {
                    otp2.value = Utils.getDigits(it, otp2.value)
                }, focusRequester = focusRequesters[1],
                nextFocusRequester = focusRequesters[2]
            )
            Spacer(modifier = Modifier.width(10.dp))
            OtpInput(
                value = otp3.value, onValueChange = {
                    otp3.value = Utils.getDigits(it, otp3.value)
                }, focusRequester = focusRequesters[2],
                nextFocusRequester = focusRequesters[3]
            )
            Spacer(modifier = Modifier.width(10.dp))
            OtpInput(
                value = otp4.value, onValueChange = {
                    otp4.value = Utils.getDigits(it, otp4.value)
                }, focusRequester = focusRequesters[3],
                nextFocusRequester = null,
                imeAction = ImeAction.Done
            )
        }
        Row(
            modifier = Modifier.padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Didn't receive the OTP?",
                style = TextStyle(
                    color = Color(0xff333333),
                    fontFamily = FontFamily(fonts = GothamBook)
                )
            )
            Button(
                onClick = {
                    Helpers.showToast(
                        context = context,
                        "success",
                        "OTP has resent successfully"
                    )
                    focusRequesters[0].requestFocus()
                    otp1.value = ""
                    otp2.value = ""
                    otp3.value = ""
                    otp4.value = ""
                    if (mobile != null) {
                        otpviewModel.sendOtp(mobileNumber = mobile)
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp
                ),
            ) {
                Text(
                    text = "RESEND OTP",
                    style = TextStyle(
                        color = Color(0xFFfe8c05),
                        fontFamily = FontFamily(fonts = GothamBold)
                    )
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    val otp = "${otp1.value}${otp2.value}${otp3.value}${otp4.value}"
                    if (otp == "") {
                        Helpers.showToast(
                            context = context,
                            "error",
                            "Please,Enter OTP Number"
                        )
                    }else if(finalOTP.value.isNotEmpty() && finalOTP.value.trim() != otp.trim()){
                        Helpers.showToast(
                            context = context,
                            "error",
                            "Failed to match OTP Number"
                        )
                    } else {
                        viewModel.loginUser(data = LoginRequestModel(mobile!!, otp))
                    }
                },
                modifier = Modifier
                    .height(50.dp)
                    .clip(shape = RoundedCornerShape(25.dp)),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp
                ),
                contentPadding = PaddingValues(horizontal = 25.dp),
            ) {
                Text(
                    text = "Verify & Proceed ",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(fonts = GothamBold),
                        letterSpacing = 2.sp
                    )
                )
            }
        }

        ShowProgressDialog(showDialog = isLoading.value) {
            isLoading.value = false
        }
    }

}
