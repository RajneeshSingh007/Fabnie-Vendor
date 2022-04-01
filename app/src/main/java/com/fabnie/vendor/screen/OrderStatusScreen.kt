package com.fabnie.vendor.viewmodel


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.R
import com.fabnie.vendor.model.orders.OrderListData
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.screen.ShowLoading
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.ui.theme.GothamBook
import com.fabnie.vendor.ui.theme.GothamMedium
import com.fabnie.vendor.utils.Constants
import com.fabnie.vendor.utils.Utils
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.widget.Header
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun OrderStatusScreen(navController: NavHostController, order_id:Int? = -1) {
    val isLoading = remember { mutableStateOf(false) }
    val ordersViewModel: OrdersViewModel = hiltViewModel()
    val state = ordersViewModel.orderDetails.collectAsState().value

    LaunchedEffect(key1 = "OrderStatusScreen") {
        val appPref = BaseApplication.appContext.appPref
        ordersViewModel.getOrderDetails(token = appPref.getString(Constants.ACCESS_TOKEN).toString(), orderID= order_id!!.toInt())
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


    Column(modifier = Modifier.fillMaxSize()) {
        Header(title = "Order Detail", showIcon = false)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        )
        if (isLoading.value) {
            ShowLoading()
        }else{
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
                        shape = RoundedCornerShape(CornerSize(10.dp)),
                        backgroundColor = Color(0xFFfff0dd)
                    ) {
                        Column(modifier = Modifier.fillMaxSize()){
                            if(state.data?.data?.order != null && state.data?.data?.products != null ) {
                                ListItem(state.data?.data)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun Processing(status:String = "pending") {
    Box(
        modifier = Modifier
            .width(40.dp)
            .height(40.dp)
            .background(color = Utils.getOrderColor(status), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_loading_sign), contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(color = Color.White)
        )
    }
}

@Composable
fun Completed(status:String = "pending") {
    Box(
        modifier = Modifier
            .width(30.dp)
            .height(30.dp)
            .background(color = Utils.getOrderColor(status), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_check), contentDescription = null,
            modifier = Modifier.size(15.dp),
            colorFilter = ColorFilter.tint(color = Color.White)
        )
    }
}

@Composable
fun Default() {
    Box(
        modifier = Modifier
            .width(30.dp)
            .height(30.dp)
            .background(color = Color(0xFF5f5f5f), shape = CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(10.dp)
                .height(10.dp)
                .background(color = Color.White, shape = CircleShape),
        )
    }
}

@Composable
fun ShowProcessingStatus() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
    ) {
        Processing()
        Box(
            modifier = Modifier
                .weight(2f)
                .height(1.dp)
                .background(color = Color.Gray)
        )
        Default()
        Box(
            modifier = Modifier
                .weight(2f)
                .height(1.dp)
                .background(color = Color.Gray)
        )
        Default()
//        Box(
//            modifier = Modifier
//                .width(50.dp)
//                .height(1.dp)
//                .background(color = Color.Gray)
//        )
//        Default()
    }
}

@Composable
fun ShowInTransitStatus() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
    ) {
        Completed()
        Box(
            modifier = Modifier
                .weight(2f)
                .height(1.dp)
                .background(color = Color.Gray)
        )
        Completed()
        Box(
            modifier = Modifier
                .weight(2f)
                .height(1.dp)
                .background(color = Color.Gray)
        )
        Processing()
//        Box(
//            modifier = Modifier
//                .width(50.dp)
//                .height(1.dp)
//                .background(color = Color.Gray)
//        )
//        Default()
    }
}

@Composable
fun ShowShippedStatus() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp)
    ) {
        Completed()
        Box(
            modifier = Modifier
                .weight(2f)
                .height(1.dp)
                .background(color = Color.Gray)
        )
        Processing("shipped")
        Box(
            modifier = Modifier
                .weight(2f)
                .height(1.dp)
                .background(color = Color.Gray)
        )
        Default()
//        Box(
//            modifier = Modifier
//                .width(50.dp)
//                .height(1.dp)
//                .background(color = Color.Gray)
//        )
//        Default()
    }
}

@Composable
fun ShowCompletedStatus() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
    ) {
        Completed()
        Box(
            modifier = Modifier
                .weight(2f)
                .height(1.dp)
                .background(color = Color.Gray)
        )
        Completed("shipped")
        Box(
            modifier = Modifier
                .weight(2f)
                .height(1.dp)
                .background(color = Color.Gray)
        )
        Completed("delivered")
//        Box(
//            modifier = Modifier
//                .width(50.dp)
//                .height(1.dp)
//                .background(color = Color.Gray)
//        )
//        Completed()
    }
}
@Composable
fun PriceInfo(heading:String="", info:String=""){
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        Text(
            text = "${heading}:",
            style = TextStyle(
                color = Color(0xff707070),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(fonts = GothamMedium),
            ),
            modifier = Modifier.weight(0.4f)
        )
        Text(
            text = info.capitalize(),
            style = TextStyle(
                color = Color.Black,
                fontSize = 15.sp,
                fontFamily = FontFamily(fonts = GothamMedium),
            ),
            modifier = Modifier.weight(0.6f)
        )
    }
}

@Composable
fun ListItem(item: OrderListData) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(start = 15.dp, top = 16.dp,end = 15.dp)
    ) {
        Text(
            text = "ID: ${item.id}",
            style = TextStyle(
                color = Color(0xFFfE8C05),
                fontSize = 18.sp,
                fontFamily = FontFamily(fonts = GothamBook),
            )
        )
        Text(
            text = "Qty: ${item.quantity}",
            style = TextStyle(
                color = Color(color = 0xFF3a3a3a),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(fonts = GothamBook),
            )
        )
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, end = 4.dp, top = 10.dp)
    ) {
        Text(
            text = Utils.formatDate(item.order!!.created_at),
            modifier = Modifier.padding(start = 20.dp),
            style = TextStyle(
                color = Color(color = 0xFF3a3a3a),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = Utils.formatDate(item.order.updated_at),
            modifier = Modifier.padding(end = 20.dp),
            style = TextStyle(
                color = Color(color = 0xFF3a3a3a),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, bottom = 5.dp)
    ) {
        Text(text = "Processing",
            modifier = Modifier.padding(start = 10.dp),
            style = TextStyle(
                color= Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(text = "Shipped",
            modifier = Modifier.padding(end = 10.dp),
            style = TextStyle(
                color= Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
//        Text(text = "In Transit",
//            modifier = Modifier.padding(start = 10.dp),
//            style = TextStyle(
//                color= Color.Gray,
//                fontSize = 12.sp,
//                fontWeight = FontWeight.Bold
//            )
//        )
        Text(text = "Delivered",
            modifier = Modifier.padding(end = 10.dp),
            style = TextStyle(
                color= Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
    Spacer(modifier = Modifier.height(10.dp))
    when (item.status) {
        "shipped" -> ShowShippedStatus()
        "delivered" -> ShowCompletedStatus()
        else -> ShowProcessingStatus()
    }
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = "Product",
        modifier = Modifier.padding(start = 15.dp),
        style = TextStyle(
            color = Color(0xFFfE8C05),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(fonts = GothamBold),
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 15.dp, top = 5.dp)
    ) {
        Box(
            modifier = Modifier
                .width(106.dp)
                .height(106.dp)
                .clip(RoundedCornerShape(8.dp))
        ) {
            Image(
                painter = rememberImagePainter("${Constants.PRODUCT_IMAGE_BASE_URL}${item.products?.thumbnail}"),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(106.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = Color(0xFFfe8c05),
                        shape = RoundedCornerShape(8.dp)
                    ),
            )
        }
        Column(modifier = Modifier
            .padding(horizontal = 10.dp)
            .weight(7f),
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "${item.products?.name?.capitalize()}",
                    style = TextStyle(
                        color = Color(0xff707070),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(fonts = GothamBold)
                    )
                )
                Text(
                    text = "${item.products?.details?.capitalize()}",
                    style = TextStyle(
                        color = Color(0xff9E9E9E),
                        fontSize = 15.sp,
                    )
                )
//                Text(
//                    text = "Size: ${item.size}",
//                    style = TextStyle(
//                        color = Color(0xff9E9E9E),
//                        fontWeight = FontWeight.SemiBold,
//                        fontSize = 15.sp
//                    ),
//                    modifier = Modifier.padding(vertical = 8.dp),
//                    fontFamily = FontFamily(fonts = GothamMedium)
//                )
            }
            Text(
                text = "₹${item.price}",
                style = TextStyle(
                    color = Color(0xff707070),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                modifier = Modifier.padding(vertical = 8.dp),
                fontFamily = FontFamily(fonts = GothamMedium)
            )
        }
    }
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = "Address",
        modifier = Modifier.padding(start = 15.dp),
        style = TextStyle(
            color = Color(0xFFfE8C05),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(fonts = GothamBold),
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 15.dp)) {
        Text(
            text = "${item.order!!.address.full_name}",
            modifier = Modifier.padding(top = 5.dp),
            style = TextStyle(
                color = Color(0xff707070),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Text(
            text = "${item.order!!.address.address1}, ${item.order!!.address.address2}",
            modifier = Modifier.padding(top = 5.dp),
            style = TextStyle(
                color = Color(color = 0xFF3a3a3a),
                fontSize = 16.sp,
                fontWeight = FontWeight.W300
            )
        )
        Text(
            text = "${item.order!!.address.city}, ${item.order!!.address.state} ${item.order!!.address.pincode}",
            modifier = Modifier.padding(top = 5.dp),
            style = TextStyle(
                color = Color(color = 0xFF3a3a3a),
                fontSize = 16.sp,
                fontWeight = FontWeight.W300
            )
        )
    }
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = "Amount",
        modifier = Modifier.padding(start = 15.dp),
        style = TextStyle(
            color = Color(0xFFfE8C05),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily(fonts = GothamBold),
        )
    )
    Spacer(modifier = Modifier.height(10.dp))
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 15.dp)
    ) {
        PriceInfo("Payment Mode","${item.order?.payment_method}")
        //PriceInfo("Sub Total","₹${item.order!!.sub_total.toDouble()}")
        //PriceInfo("Delivery","₹${item.order!!.tax.toDouble()}")
        PriceInfo("Total","₹${item.order?.final_total?.toDouble()}")
    }
    Spacer(modifier = Modifier.height(24.dp))
}
