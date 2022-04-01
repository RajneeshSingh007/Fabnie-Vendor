package com.fabnie.vendor.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.BottomBarScreen
import com.fabnie.vendor.R
import com.fabnie.vendor.model.product.Data
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.ui.theme.GothamBook
import com.fabnie.vendor.ui.theme.GothamMedium
import com.fabnie.vendor.utils.Constants
import com.fabnie.vendor.utils.Utils
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.viewmodel.LoginViewModel
import com.fabnie.vendor.viewmodel.ProductViewModel
import com.fabnie.vendor.widget.Header
import kotlinx.coroutines.launch

@Composable
fun ProductListScreen(navController: NavHostController) {
    val productViewModel: ProductViewModel = hiltViewModel()
    val state = productViewModel.products.collectAsState().value
    val isLoading = remember { mutableStateOf(false) }
    val products = remember { mutableStateOf(listOf<Data>()) }
    val token = remember { mutableStateOf("") }
    LaunchedEffect(key1 = true) {
        val appPref = BaseApplication.appContext.appPref
        token.value = appPref.getString(Constants.ACCESS_TOKEN).toString()
        productViewModel.getProducts(token = token.value)
    }

    when (state) {
        is Response.Empty -> {
            isLoading.value = false
        }
        is Response.Loading -> {
            isLoading.value = true
        }
        is Response.Success -> {
            isLoading.value = false
            products.value = state.data?.data!!
            Log.d("[ProductListScreen]", "Success ${state.data?.data}")
        }
        is Response.Error -> {
            Log.d("[ProductListScreen]", "Error ${state.error}")
            isLoading.value = false
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        Header(title = "Product Listing", showIcon = false)
        Spacer(modifier = Modifier.height(15.dp))
        if (isLoading.value) {
            ShowLoading()
        } else {
            ShowProducts(products = products.value, navController = navController)
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}

@Composable
fun ShowLoading() {
    Spacer(modifier = Modifier.height(40.dp))
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ShowProducts(products: List<Data>, navController: NavHostController) {
    Box(modifier = Modifier.padding(horizontal = 10.dp)) {
        Box(
            modifier = Modifier
                .coloredShadow(
                    color = Color(0xFFfE8C05),
                    borderRadius = 20.dp, alpha = 0.3f, shadowRadius = 5.dp
                ),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                backgroundColor = Color(0xfffff0dd),
                shape = RoundedCornerShape(20.dp),
                elevation = 0.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    products.forEachIndexed { index, i ->
                        ProductListItem(item = i) {
                            navController.navigate("${BottomBarScreen.AddProduct.route}/${i.id}/${i.product_id}")
                        }
                        if (index != products.size - 1) {
                            Divider()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductListItem(item: Data, onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 15.dp),
        ) {
//            Text(
//                text = "${item.id}",
//                style = TextStyle(
//                    fontFamily = FontFamily(fonts = GothamBook),
//                    fontSize = 14.sp,
//                    color = Color(0xFF5c5c5c)
//                ),
//                modifier = Modifier.padding(start = 10.dp)
//            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = true, onClick = { /*TODO*/ }, colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFFfE8C05),
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Product: ",
                        style = TextStyle(
                            fontFamily = FontFamily(fonts = GothamMedium),
                            fontSize = 14.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(start = 10.dp)
                    )
                    Text(
                        text = "${item.product?.name}",
                        style = TextStyle(
                            fontFamily = FontFamily(fonts = GothamBold),
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column() {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Price: ",
                            style = TextStyle(
                                fontFamily = FontFamily(fonts = GothamMedium),
                                fontSize = 16.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(start = 35.dp)
                        )
                        Text(
                            text = "₹ ${item.price}",
                            style = TextStyle(
                                fontFamily = FontFamily(fonts = GothamBook),
                                fontSize = 14.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            //text = "Availability: ",
                            text = "Quantity: ",
                            style = TextStyle(
                                fontFamily = FontFamily(fonts = GothamMedium),
                                fontSize = 16.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(start = 35.dp)
                        )
                        Text(
                            text = "${item.quantity}",
                            //text = Utils.getProductStockInfo("${item.product?.size_qty}"),
                            style = TextStyle(
                                fontFamily = FontFamily(fonts = GothamBook),
                                fontSize = 14.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier.padding(start = 5.dp)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    val painter = rememberImagePainter(
                        data = "${Constants.PRODUCT_IMAGE_BASE_URL}${item.product?.thumbnail}",
                        builder = {
                            placeholder(R.drawable.fabnie_logo)
                        }
                    )
                    Image(
                        painter = painter, contentDescription = null,
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
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        onClick()
                    },
                    shape = CircleShape,
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                ) {
                    Text(
                        text = "Edit ",
                        style = TextStyle(
                            fontFamily = FontFamily(fonts = GothamBold),
                            fontSize = 18.sp
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow),
                        contentDescription = "right_arrow",
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(5.dp))
        }
    }
}