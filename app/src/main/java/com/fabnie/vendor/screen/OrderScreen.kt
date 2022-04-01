package com.fabnie.vendor.screen

import android.app.Activity
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.BottomBarScreen
import com.fabnie.vendor.R
import com.fabnie.vendor.data.Order
import com.fabnie.vendor.data.OrderStatus
import com.fabnie.vendor.model.orders.OrderListData
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.ui.theme.GothamBook
import com.fabnie.vendor.ui.theme.GothamMedium
import com.fabnie.vendor.utils.Constants
import com.fabnie.vendor.utils.Helpers
import com.fabnie.vendor.utils.Utils
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.viewmodel.OrdersViewModel
import com.fabnie.vendor.viewmodel.ProfileViewModel
import com.fabnie.vendor.viewmodel.ResponseViewModel
import com.fabnie.vendor.widget.Header
import com.fabnie.vendor.widget.ShowProgressDialog
import com.fabnie.vendor.widget.SimpleExposedDropDownMenu
import com.fabnie.vendor.widget.StatusListDropDown
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun OrderScreen(navHostController: NavHostController) {
    val context = LocalContext.current as Activity
    val isLoading = remember { mutableStateOf(false) }
    val ordersViewModel: OrdersViewModel = hiltViewModel()
    val state = ordersViewModel.orders.collectAsState().value
    val statusList = arrayOf("Processing","Shipped", "Delivered","Declined")
    val dropdownState = remember { mutableStateMapOf<Int, Int>() }
    val responseViewModel: ResponseViewModel = hiltViewModel()
    val responseState = responseViewModel.response.collectAsState().value
    val statusChangeLoading = remember { mutableStateOf(false) }
    val token = remember { mutableStateOf("") }
    val apiProcess = remember { mutableStateOf(-1) }

    LaunchedEffect(key1 = "OrderStatusScreen") {
        val appPref = BaseApplication.appContext.appPref
        token.value = appPref.getString(Constants.ACCESS_TOKEN).toString()
        ordersViewModel.getAllOrders(token = token.value)
    }

    when(state){
        is Response.Empty -> {
            isLoading.value = true
        }
        is Response.Loading -> {
            isLoading.value = true
        }
        is Response.Success -> {
            isLoading.value = false
        }
        is Response.Error -> {
            isLoading.value = false
        }
    }

    when(responseState){
        is Response.Empty -> {
            statusChangeLoading.value = false
        }
        is Response.Loading -> {
            statusChangeLoading.value = true
        }
        is Response.Success -> {
            statusChangeLoading.value = false
            if(apiProcess.value == 0) {
                responseState.data?.let { Helpers.showToast(context, "success", it.message) }
                apiProcess.value = -1
            }
        }
        is Response.Error -> {
            statusChangeLoading.value = false
            if(apiProcess.value == 0) {
                responseState.data?.let { Helpers.showToast(context, "error", it.message) }
                apiProcess.value = -1
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Header(title = "Order Status", showIcon = false)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
        if (isLoading.value) {
            ShowLoading()
        } else {
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
                        elevation = 0.dp,
                        shape = RoundedCornerShape(CornerSize(20.dp)),
                        backgroundColor = Color(0xFFfff0dd)
                    ) {

                        Column(modifier = Modifier.fillMaxWidth()) {
                            state.data?.data?.forEachIndexed { index, item ->
                                OrderListItem(dropdownState,statusList, item = item,onStatusClick = {
                                    it, pos ->
                                    dropdownState[it.id] = pos
                                    apiProcess.value = 0
                                    responseViewModel.orderStatusChange(it.id,token.value,statusList[pos].toLowerCase())
                                },onViewOrderClick = {
                                    navHostController.navigate("${BottomBarScreen.OrderStatus.route}/${it.id}")
                                })
                                if (index != state.data?.data?.size - 1) {
                                    Divider()
                                }
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
    ShowProgressDialog(showDialog = statusChangeLoading.value) {
    }
}

@Composable
fun OrderListItem(dropdownMap:MutableMap<Int, Int>, statusList:Array<String>, item: OrderListData, onStatusClick:(item: OrderListData, index:Int) -> Unit, onViewOrderClick: (item: OrderListData) -> Unit) {
    var selectedDropDownIndex =  if(dropdownMap.isNotEmpty()){
        dropdownMap[item.id]
    }else{
        -1
    }
    if(selectedDropDownIndex == null){
        selectedDropDownIndex =  -1
    }

    val status = if(selectedDropDownIndex == -1){
        item.status
    }else{
        statusList[selectedDropDownIndex]
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onViewOrderClick(item)
            }
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ID: ${item.id}",
                style = TextStyle(
                    fontFamily = FontFamily(fonts = GothamBook),
                    fontSize = 18.sp,
                    color = Color(0xFF5C5C5C)
                ),
            )
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .border(1.dp, color = Color(0xFFFE8C05), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Qty: ${item.quantity}",
                    style = TextStyle(
                        fontFamily = FontFamily(fonts = GothamMedium),
                        fontSize = 14.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberImagePainter("${Constants.PRODUCT_IMAGE_BASE_URL}/${item.products?.thumbnail}"), contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 1.dp,
                            color = Color(0xFFfe8c05),
                            shape = RoundedCornerShape(8.dp)
                        ),
                )
            }
            Column(
                modifier = Modifier.weight(8f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "${item.products?.name}",
                    style = TextStyle(
                        fontFamily = FontFamily(fonts = GothamBold),
                        fontSize = 16.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(start = 10.dp)
                )
                Spacer(modifier = Modifier.height(5.dp))
                Text(
                    text = "₹${item.order?.final_total?.toDouble()}",
                    style = TextStyle(
                        fontFamily = FontFamily(fonts = GothamMedium),
                        fontSize = 14.sp,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(start = 10.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier
                .weight(0.5f)
                .align(alignment = Alignment.CenterVertically)) {
                Box(modifier = Modifier.padding(top = 4.dp)) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(
                                color = Utils.getOrderColor(status.toLowerCase()),
                                shape = CircleShape
                            )
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = status.capitalize(),
                    style = TextStyle(
                        color = Color(color = 0xFF3a3a3a),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W300,
                        fontFamily = FontFamily(fonts = GothamBold)
                    )
                )
            }
            if(status.toLowerCase() != "delivered"){
                Box(modifier = Modifier.weight(0.5f)) {
                    StatusListDropDown(
                        values = statusList.toList(),
                        selectedIndex = selectedDropDownIndex,
                        onChange = {
                            onStatusClick(item, it)
                        },
                        modifier = Modifier.padding(horizontal = 0.dp, vertical = 0.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}


