package com.fabnie.vendor.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fabnie.vendor.R
import com.fabnie.vendor.data.CategoryFilter
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.ui.theme.GothamMedium

@Composable
fun ProductScreen(navHostController: NavHostController) {
    val searchText = remember { mutableStateOf("") }
    val activeFilterIndex = remember { mutableStateOf(1) }
    val filters = listOf(
        CategoryFilter(title = "Rolling Sheet"),
        CategoryFilter(title = "Fabrication")

    )

    val items = (1..3).toList()

    Box(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    color = Color(0xFFfe8c05),
                    shape = RoundedCornerShape(bottomStart = 25.dp, bottomEnd = 25.dp)
                )
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                        .border(2.dp, color = Color.White, shape = RoundedCornerShape(20.dp))
                        .height(50.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, color = Color(0xFFfe8c05), shape = RoundedCornerShape(20.dp))
                        .height(42.dp)
                        .background(color = Color(0xFFfe8c05))
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
                        .height(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    TextField(
                        value = searchText.value,
                        onValueChange = { it ->
                            searchText.value = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(
                                shape = RoundedCornerShape(
                                    bottomStart = 20.dp,
                                    bottomEnd = 20.dp
                                )
                            ),
                        trailingIcon = {
                            Image(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = "search",
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFFfe8c05),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            textColor = Color.White,
                            placeholderColor = Color.White,
                            cursorColor = Color.White
                        ),
                        placeholder = {
                            Text(text = "Search")
                        },
                    )
                }

            }
            LazyRow(

                modifier = Modifier.padding(top = 30.dp, bottom = 20.dp)
            ) {
                items((1..10).toList()) {
                    ProductCard()
                }
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .fillMaxWidth()
                    .clip(shape = CircleShape)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color(0xFFFFF0DE))
                ) {
                    filters.forEachIndexed { index, categoryFilter ->
                        val bgColor =
                            if (activeFilterIndex.value == index) Color(0xFFfe8c05) else Color(
                                0xFFFFF0DE
                            )
                        val textColor =
                            if (activeFilterIndex.value == index) Color(0xFFFFFFFF) else Color(
                                0xFFFE8C05
                            )
                        Button(
                            onClick = {
                                activeFilterIndex.value = index
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .clip(shape = CircleShape),
                            colors = ButtonDefaults.buttonColors(backgroundColor = bgColor),
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                disabledElevation = 0.dp
                            ),
                        ) {
                            Text(
                                text = categoryFilter.title, style = TextStyle(
                                    color = textColor,
                                    fontFamily = FontFamily(fonts = GothamBold),
                                    fontSize = 17.sp
                                )
                            )
                        }
                    }
                }
            }
            items.forEach { item ->
                ProductListItem(navHostController = navHostController)
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color(0xFFF9F9F9))
                    .padding(vertical = 20.dp)
            ) {
                Column {
                    Text(
                        text = "BUILDING\nSTRONGER\nLIVES",
                        modifier = Modifier.padding(start = 20.dp),
                        style = TextStyle(
                            fontSize = 58.sp,
                            color = Color(0xFFFFD9AB),
                            fontFamily = FontFamily(fonts = GothamBold),
                            fontWeight = FontWeight.W600
                        )
                    )
                    Text(
                        text = "POWERED BY,\nFABINE CO. INDIA",
                        modifier = Modifier.padding(start = 20.dp, top = 10.dp),
                        style = TextStyle(
                            fontSize = 12.sp,
                            color = Color(0xFFE1E1E1),
                            fontWeight = FontWeight.W400,
                            fontFamily = FontFamily(fonts = GothamBold)
                        )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }
    }
}

@Composable
fun ProductCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp), shape = RoundedCornerShape(20.dp)
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp.dp
        Image(
            painter = painterResource(id = R.drawable.sample_image), contentDescription = null,
            modifier = Modifier
                .width(screenWidth - 30.dp)
                .height(180.dp),
            alignment = Alignment.CenterEnd,
        )

    }
}

@Composable
fun ProductListItem(navHostController: NavHostController) {
    Button(
        onClick = {
//        navHostController.navigate(BottomBarScreen.ProductDetail.route)
        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        elevation = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 10.dp),
            shape = RoundedCornerShape(20.dp),
            backgroundColor = Color(0xFFfff0dd),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sample_product),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    )
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .weight(7f)
                        .height(120.dp),
                    verticalArrangement = Arrangement.SpaceAround
                ) {

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Steel Iron Rod",
                            style = TextStyle(
                                color = Color(0xff707070),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                fontFamily = FontFamily(fonts = GothamBold)
                            )
                        )
                        Text(
                            text = "8,10,12,16,20,25,32 (In Mm)",
                            style = TextStyle(
                                color = Color(0xff9E9E9E),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 10.sp
                            ),
                            fontFamily = FontFamily(fonts = GothamMedium)
                        )
                        Text(
                            text = "Mild Steel",
                            style = TextStyle(
                                color = Color(0xff9E9E9E),
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp
                            ),
                            fontFamily = FontFamily(fonts = GothamMedium)
                        )
                    }
                    Text(
                        text = "₹44,750 / Metric Ton",
                        style = TextStyle(
                            color = Color(0xff707070),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        fontFamily = FontFamily(fonts = GothamBold)
                    )
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(120.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_tag),
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 8.dp, top = 10.dp),
                        alignment = Alignment.CenterEnd
                    )
                }
            }
        }
    }
}


