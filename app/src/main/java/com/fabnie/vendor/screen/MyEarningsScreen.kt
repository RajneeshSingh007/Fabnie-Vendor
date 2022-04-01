package com.fabnie.vendor.screen

import android.icu.util.Calendar
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.dt.composedatepicker.ComposeCalendar
import com.dt.composedatepicker.SelectDateListener
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.R
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.ui.theme.GothamMedium
import com.fabnie.vendor.ui.theme.Purple500
import com.fabnie.vendor.utils.Constants
import com.fabnie.vendor.utils.Utils
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.viewmodel.PaymentViewModel
import com.fabnie.vendor.widget.Header
import java.util.*

@Composable
fun MyEarningsScreen(navHostController: NavHostController) {
    val paymentViewModel: PaymentViewModel = hiltViewModel()
    val state = paymentViewModel.payment.collectAsState().value
    val token = remember { mutableStateOf("") }
    val monthlyAmt = remember { mutableStateOf(0) }
    val weeklyAmt = remember { mutableStateOf(0) }
    val showCalendar = remember { mutableStateOf(false) }
    val selectedMonth = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = "MyEarningsScreen"){
        val appPref = BaseApplication.appContext.appPref
        token.value = appPref.getString(Constants.ACCESS_TOKEN).toString()
        paymentViewModel.getPaymentDetails(token.value)
        val currentDate = Calendar.getInstance().time
        selectedMonth.value = currentDate.month
    }

    when(state){
        is Response.Empty -> {
        }
        is Response.Loading -> {
        }
        is Response.Success -> {
            val data = state.data?.data
            if(data != null) {
                monthlyAmt.value = data.current_month_data
                weeklyAmt.value = data.current_week_data
            }
        }
        is Response.Error -> {
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Header(title = "My Earnings", showIcon = false)
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
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.weight(9f),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = { showCalendar.value = true },
                                    modifier = Modifier.clip(shape = CircleShape),
                                    contentPadding = PaddingValues(4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(35.dp)
                                            .background(color = Color.White, shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_calender),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = Color(0XFFfe8c05)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = Utils.getMonth(selectedMonth.value), style = TextStyle(
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily(fonts = GothamMedium),
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                            }
//                            Box(modifier = Modifier.weight(1f)) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_arrow),
//                                    contentDescription = "right_arrow",
//                                    modifier = Modifier.size(15.dp),
//                                    tint = Color.Black
//                                )
//                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .padding(3.dp)
                                .shadow(elevation = 2.dp, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "₹ ${monthlyAmt.value}", style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily(fonts = GothamBold),
                                ),
                                modifier = Modifier
                                    .background(color = Color(0xFFfff0dd), shape = CircleShape)
                                    .padding(horizontal = 40.dp, vertical = 10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Divider(modifier = Modifier.height(2.dp))
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 30.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier.weight(9f),
                                contentAlignment = Alignment.Center
                            ) {
                                Button(
                                    onClick = { },
                                    modifier = Modifier.clip(shape = CircleShape),
                                    contentPadding = PaddingValues(4.dp),
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(35.dp)
                                            .background(color = Color.White, shape = CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_weekly_earnings),
                                            contentDescription = null,
                                            modifier = Modifier.size(20.dp),
                                            tint = Color(0XFFfe8c05)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = "Weekly Earnings", style = TextStyle(
                                            fontSize = 20.sp,
                                            fontFamily = FontFamily(fonts = GothamMedium),
                                        )
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                }
                            }
//                            Box(modifier = Modifier.weight(1f)) {
//                                Icon(
//                                    painter = painterResource(id = R.drawable.ic_arrow),
//                                    contentDescription = "right_arrow",
//                                    modifier = Modifier.size(15.dp),
//                                    tint = Color.Black
//                                )
//                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .padding(3.dp)
                                .shadow(elevation = 2.dp, shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "₹ ${weeklyAmt.value}", style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 24.sp,
                                    fontFamily = FontFamily(fonts = GothamBold),
                                ),
                                modifier = Modifier
                                    .background(color = Color(0xFFfff0dd), shape = CircleShape)
                                    .padding(horizontal = 40.dp, vertical = 10.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(20.dp))
//                        Row(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(horizontal = 60.dp),
//                            horizontalArrangement = Arrangement.SpaceEvenly
//                        ) {
//
//                            Bar(weight1 = 6, weight2 = 4)
//                            Bar(weight1 = 2, weight2 = 8)
//                            Bar(weight1 = 3, weight2 = 7)
//                            Bar(weight1 = 1, weight2 = 9)
//                            Bar(weight1 = 2, weight2 = 8)
//                            Bar(weight1 = 5, weight2 = 5)
//                            Bar(weight1 = 2, weight2 = 8)
//                        }
                        //Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }

    }

    if(showCalendar.value) {
        Dialog(
            onDismissRequest = { },
            DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
        ) {
            ComposeCalendar(
                maxDate = Calendar.getInstance().time,
                locale = Locale("en"),
                title = "Select Month",
                showOnlyMonth = true,
                themeColor = Purple500,
                listener = object : SelectDateListener {
                    override fun onDateSelected(date: Date) {
                        Log.e("Picked", "${date.month}")
                        selectedMonth.value = date.month
                        paymentViewModel.getPaymentDetailsByMonth(token.value,date.month)
                        showCalendar.value = false
                    }

                    override fun onCanceled() {
                        showCalendar.value = false
                    }
                })
        }
    }

}

@Composable
fun Bar(weight1: Int, weight2: Int) {
    Column(
        modifier = Modifier
            .width(10.dp)
            .height(120.dp)
            .border(width = 1.dp, color = Color(0XFFfe8c05))
    ) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(75.dp)
                .weight(weight1.toFloat())
//            .background(color = Color.White)
        )
        Box(
            modifier = Modifier
                .width(10.dp)
                .weight(weight2.toFloat())
                .background(color = Color(0XFFfe8c05))
        )
    }
}