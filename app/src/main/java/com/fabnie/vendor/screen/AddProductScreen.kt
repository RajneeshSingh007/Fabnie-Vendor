package com.fabnie.vendor.screen

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.BottomBarScreen
import com.fabnie.vendor.model.addproduct.AddProductRequestModel
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.ui.theme.GothamMedium
import com.fabnie.vendor.utils.Constants
import com.fabnie.vendor.utils.Helpers
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.viewmodel.AddProductViewModel
import com.fabnie.vendor.viewmodel.MeasurementViewModel
import com.fabnie.vendor.viewmodel.ProductViewModel
import com.fabnie.vendor.widget.*
import kotlinx.coroutines.delay

@Composable
fun AddProductScreen(
    navHostController: NavHostController,
    productListVM: ProductViewModel?,
    id:Int? = -1,
    product_id: Int? = -1
) {
    //Log.e("[AddProductScreen]", "${id} :: ${product_id}")

    val context = LocalContext.current as Activity

    val productVMListState = productListVM?.products?.collectAsState()?.value

    //Log.e("productVMListState", "${productVMListState?.data?.data.toString()}")

    val productViewModel: ProductViewModel = hiltViewModel()
    val productListState = productViewModel.userProducts.collectAsState().value

    val addProductViewModel: AddProductViewModel = hiltViewModel()
    val state = addProductViewModel.products.collectAsState().value

    val measurementViewModel: MeasurementViewModel = hiltViewModel()
    val measurementState = measurementViewModel.measurements.collectAsState().value

    val title = remember { mutableStateOf("Add Product") }
    val name = remember { mutableStateOf("") }
    val description = remember { mutableStateOf("") }
    val imageUrl = remember { mutableStateOf("") }
    val token = remember { mutableStateOf("") }
    val isLoading = remember { mutableStateOf(false) }
    val standard = remember { mutableStateOf("") }
    val vendorID = remember { mutableStateOf("") }
    val clicked = remember { mutableStateOf(false) }
    val measureList = remember { mutableStateListOf<String>() }
    val itemList = remember { mutableStateMapOf<Int, Array<String>>() }
    val applicationList = remember { mutableStateMapOf<Int, String>() }
    val selectedProductId = remember { mutableStateOf(-1) }
    val pId = remember { mutableStateOf(-1) }
    val pIndex = remember { mutableStateOf(-1) }
    val productAmount = remember { mutableStateOf("") }
    val productQty = remember { mutableStateOf("") }

    LaunchedEffect(key1 = "AddProductScreen") {
        val appPref = BaseApplication.appContext.appPref
        token.value = appPref.getString(Constants.ACCESS_TOKEN).toString()
        vendorID.value = appPref.getString(Constants.VENDOR_ID).toString()
        measurementViewModel.getMeasurements(token = token.value)
        productViewModel.getUserProducts(token = token.value)
    }

    LaunchedEffect(key1 = productVMListState){
        if (id != null && id >= 0) {
            title.value = "Edit Product"
            //delay(200)
            //selectedProductId.value = id
            val response = productVMListState?.data
            if (response != null && response.data.isNotEmpty()) {
                val selectedProduct = response.data.filter { it.id == id }
                //Log.e("[AddProductScreen]", "${selectedProduct}")
                if (selectedProduct.isNotEmpty()) {
                    val firstPosProduct = selectedProduct[0]
                    //pId.value = firstPosProduct.id
                    productAmount.value = firstPosProduct.price.toString()

                    productQty.value = if(firstPosProduct.quantity.toString() == "0") "Out of Stock" else "In Stock"
//                    if (firstPosProduct.standard.isNotEmpty()) {
//                        standard.value = firstPosProduct.standard
//                    }

//                    if (firstPosProduct.price?.isNotEmpty() == true) {
//                        val priceSplit = firstPosProduct.price.split(",")
//                        //Log.e("priceSplit", "${priceSplit}")
//                        val quantitySplit = firstPosProduct.quantity.split(",")
//                        val priceMeasureSplit = firstPosProduct.price_measure.split(",")
//                        val sizeSplit = firstPosProduct.size.split(",")
//                        priceSplit.forEachIndexed { index, s ->
//                            val inc = index + 1
//                            itemList.put(
//                                inc,
//                                arrayOf(
//                                    sizeSplit[index],
//                                    quantitySplit[index],
//                                    s,
//                                    priceMeasureSplit[index]
//                                )
//                            )
//                        }
//                    } else {
//                        itemList.put(1, emptyArray())
//                    }

//                    if (firstPosProduct.application.isNotEmpty()) {
//                        val appSplit = firstPosProduct.application.split(",")
//                        appSplit.forEachIndexed { index, s ->
//                            val inc = index + 1
//                            applicationList.put(inc, s)
//                        }
//                    } else {
//                        applicationList.put(1, "")
//                    }
                } else {
//                    applicationList.put(1, "")
//                    itemList.put(1, emptyArray())
                }
            } else {
//                applicationList.put(1, "")
//                itemList.put(1, emptyArray())
            }
        } else {
//            applicationList.put(1, "")
//            itemList.put(1, emptyArray())
        }
    }

    when (productListState) {
        is Response.Success -> {
            if (pIndex.value == -1) {
                val response = productListState.data
                if (response != null && response.data.isNotEmpty()) {
                    val filter = response.data.filter { it.id == product_id }
                    if (filter.isNotEmpty()) {
                        pIndex.value = response.data.indexOf(filter[0])
                    }
                }
            }
        }
        else -> {

        }
    }


    //add or update product
    when (state) {
        is Response.Empty -> {
            isLoading.value = false
        }
        is Response.Loading -> {
            isLoading.value = true
        }
        is Response.Success -> {
            val response = state.data
            isLoading.value = false
            if (response != null && clicked.value) {
                clicked.value = false
                if (response.success) {
                    if (id != null) {
                        if (id == -1) {
                            selectedProductId.value = -1
                            productAmount.value = ""
                            productQty.value = ""
//                            standard.value = ""
//                            applicationList.clear()
//                            applicationList.put(1, "")
//                            itemList.clear()
//                            itemList.put(1, emptyArray())
                        }
                    }
                    navHostController.navigate(BottomBarScreen.Products.route)
                    Helpers.showToast(context, "success", response.message)
                } else {
                    Helpers.showToast(context, "error", response.message)
                }
                //Log.d("[AddProductScreen]", "Success ${response.message}")
            }
        }
        is Response.Error -> {
            //Log.d("[AddProductScreen]", "Error ${state.error}")
            if (clicked.value) {
                clicked.value = false
                Helpers.showToast(context, "error", "Something went wrong!")
            }
            isLoading.value = false
        }
    }

    //measurementState
//    when (measurementState) {
//        is Response.Success -> {
//            if (measureList.isEmpty()) {
//                val mmresponse = measurementState.data
//                if (mmresponse != null) {
//                    if (mmresponse.success && mmresponse.data.isNotEmpty()) {
//                        mmresponse.data.map { i -> measureList.add(i.name) }
//                    }
//                }
//            }
//        }
//        else -> {
//
//        }
//    }

    Column(modifier = Modifier.fillMaxSize()) {
        Header(title = title.value, showIcon = false)
        Spacer(modifier = Modifier.height(10.dp))
        SelectedProductInfo("${imageUrl.value}", "${name.value}", "${description.value}")
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Divider(
                modifier = Modifier.height(1.dp),
                color = Color(0xFFfff0dd)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .coloredShadow(
                        color = Color(0xFFfE8C05),
                        borderRadius = 0.dp, alpha = 0.3f, shadowRadius = 2.dp
                    )
                    .padding(vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                ) {
                    productListState.data?.let {
                        ProductDropdownMenu(
                            title.value == "Edit Product",
                            pIndex.value,
                            items = it.data,
                            selectedItemData = { id, pname, productImage, productDesc ->
                                selectedProductId.value = id
                                imageUrl.value = productImage
                                name.value = pname
                                description.value = productDesc
                            })
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .coloredShadow(
                        color = Color(0xFFfE8C05),
                        borderRadius = 0.dp, alpha = 0.3f, shadowRadius = 2.dp
                    )
                    .padding(vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                ) {
                    QuantityDropdown(
                        title.value == "Edit Stock",
                        productQty.value,
                        items = listOf("","In Stock", "Out of stock"),
                        selectedItemData = {
                            //if(it.isNotEmpty()){
                                productQty.value = it
                            //}
                        })
                }
            }
        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .coloredShadow(
//                        color = Color(0xFFfE8C05),
//                        borderRadius = 0.dp, alpha = 0.3f, shadowRadius = 2.dp
//                    )
//                    .padding(vertical = 5.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 5.dp)
//                ) {
//                    TextField(
//                        value = productQty.value,
//                        onValueChange = { it ->
//                            productQty.value = it.filter { it.isDigit() }
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        label = {
//                            Text(text = "Quantity")
//                        },
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                        colors = TextFieldDefaults.textFieldColors(
//                            backgroundColor = Color(0xFFFFFFFF),
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent,
//                            textColor = Color.Black
//                        ),
//                    )
//                }
//            }
//        }
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .coloredShadow(
                        color = Color(0xFFfE8C05),
                        borderRadius = 0.dp, alpha = 0.3f, shadowRadius = 2.dp
                    )
                    .padding(vertical = 5.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp)
                ) {
                    TextField(
                        value = productAmount.value,
                        onValueChange = { it ->
                            productAmount.value = it.filter { it.isDigit() }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Price")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFFFFFFFF),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            textColor = Color.Black
                        ),
                    )
                }
            }
        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp)
//        ) {
//            Box(
//                modifier = Modifier
//                    .coloredShadow(
//                        color = Color(0xFFfE8C05),
//                        borderRadius = 0.dp, alpha = 0.3f, shadowRadius = 2.dp
//                    )
//                    .padding(vertical = 5.dp),
//                contentAlignment = Alignment.Center
//            ) {
//                Box(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 5.dp)
//                ) {
//                    TextField(
//                        value = standard.value,
//                        onValueChange = {
//                            standard.value = it
//                        },
//                        modifier = Modifier.fillMaxWidth(),
//                        label = {
//                            Text(text = "Standard")
//                        },
//                        colors = TextFieldDefaults.textFieldColors(
//                            backgroundColor = Color(0xFFFFFFFF),
//                            focusedIndicatorColor = Color.Transparent,
//                            unfocusedIndicatorColor = Color.Transparent,
//                            textColor = Color.Black
//                        ),
//                    )
//                }
//            }
//        }
        //add application
//        applicationList.forEach { j ->
//            AddApplication(j.value, onChange = {
//                applicationList[j.key] = it
//            }, addCallback = {
//                if (applicationList.size < 6) {
//                    applicationList.put(applicationList.size + 1, "")
//                } else {
//                    Helpers.showToast(context, "error", "Max size exceeded")
//                }
//            }, j.key, removeCallback = {
//                applicationList.remove(it)
//            })
//        }
//        Spacer(modifier = Modifier.height(20.dp))
//        Text(
//            text = "Add Size & Price",
//            modifier = Modifier.padding(horizontal = 20.dp),
//            style = TextStyle(
//                fontSize = 15.sp,
//                fontFamily = FontFamily(fonts = GothamMedium),
//                letterSpacing = 2.sp
//            )
//        )
        //add item for product to add size, price, quantity, measure
//        itemList.forEach { j ->
//            AddItem(
//                measureList = measureList.toTypedArray(),
//                pId.value >= 0,
//                j.value,
//                context = context,
//                j.key,
//                result = {
//                    itemList[j.key] = it
//                },
//                addCallback = {
//                    if (itemList.size < 10) {
//                        itemList.put(itemList.size + 1, emptyArray())
//                    } else {
//                        Helpers.showToast(context, "error", "Max size exceeded")
//                    }
//                },
//                deleteCallback = {
//                    itemList.remove(it)
//                })
//        }
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    if (selectedProductId.value == -1) {
                        Helpers.showToast(context, "error", "Please, Select Product")
                    }else if (productQty.value.isEmpty()) {
                        Helpers.showToast(context, "error", "Please, Select Stock")
//                    } else if (standard.value == "") {
//                        Helpers.showToast(context, "error", "Please, Enter Standard")
//                    } else if (applicationList[1] == "") {
//                        Helpers.showToast(context, "error", "Please, Enter Application")
//                    } else if (itemList[1]?.isEmpty() == true) {
//                        Helpers.showToast(context, "error", "Please, Fill all details")
                    }else if (productAmount.value.isEmpty()) {
                        Helpers.showToast(context, "error", "Please, Enter Product Price")
                    } else {
//                        val applications = applicationList.filter { it.value.isNotEmpty() }
//                        val appList = ArrayList<String>()
//                        applications.forEach { t, u -> appList.add(u) }
//                        val sizeList = ArrayList<String>()
//                        val quantityList = ArrayList<String>()
//                        val priceMeasureList = ArrayList<String>()
//                        val priceList = ArrayList<String>()
//                        itemList.forEach { t, u ->
//                            if (u.isNotEmpty()) {
//                                sizeList.add(u[0])
//                                quantityList.add(u[1])
//                                priceList.add(u[2])
//                                priceMeasureList.add(u[3])
//                            }
//                        }
//                    Log.e("[AddProductScreen] applications", "${applications}")
//                    Log.e("[AddProductScreen] sizeList", "${sizeList}")
//                    Log.e("[AddProductScreen] quantityList", "${quantityList}")
//                    Log.e("[AddProductScreen] priceMeasureList", "${priceMeasureList}")
//                    Log.e("[AddProductScreen] priceList", "${priceList}")
//                        if (sizeList.isEmpty() || quantityList.isEmpty() || priceMeasureList.isEmpty() || priceList.isEmpty()) {
//                            Helpers.showToast(context, "error", "Please, Fill all details")
//                        } else {
                            val addProductRequestModel = AddProductRequestModel(
//                                id = pId.value.toInt(),
                                user_id = vendorID.value.toInt(),
                                product_id = selectedProductId.value,
//                                standard = standard.value,
//                                application = appList,
//                                size = sizeList,
//                                quantity = quantityList,
//                                price_measure = priceMeasureList,
//                                price = priceList,
                                quantity = if(productQty.value == "In Stock") "10000" else "0",
                                //productQty.value,
                                price = productAmount.value
                            )
                            clicked.value = true
                            //add product
                            if (id == -1) {
                                addProductViewModel.addProducts(token.value, addProductRequestModel)
                            } else {
                                if (id != null) {
                                    addProductRequestModel.id = id
                                }
                                if (product_id != null) {
                                    addProductRequestModel.product_id = product_id
                                }
                                addProductViewModel.updateProducts(
                                    token.value,
                                    addProductRequestModel
                                )
                            }
                       // }
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
                    text = "Submit",
                    style = TextStyle(
                        fontSize = 18.sp,
                        fontFamily = FontFamily(fonts = GothamBold),
                        letterSpacing = 2.sp
                    )
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
        ShowProgressDialog(showDialog = isLoading.value) {
        }
    }
}

@Composable
fun SelectedProductInfo(imageUrl: String = "", name: String = "", details: String = "") {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Box(
            modifier = Modifier
                .coloredShadow(
                    color = Color(0xFFfE8C05),
                    borderRadius = 10.dp, alpha = 0.2f, shadowRadius = 4.dp
                )
                .padding(vertical = 5.dp, horizontal = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(70.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberImagePainter(imageUrl), contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(RoundedCornerShape(15.dp)),
                )
            }
        }
        Spacer(modifier = Modifier.width(5.dp))
        Column(modifier = Modifier.weight(8f), verticalArrangement = Arrangement.Center) {
            Text(
                text = name,
                modifier = Modifier.padding(end = 10.dp),
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 20.sp,
                    fontFamily = FontFamily(fonts = GothamBold)
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = details,
                modifier = Modifier.padding(end = 10.dp),
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp,
                    fontFamily = FontFamily(fonts = GothamMedium)
                )
            )
        }

    }
}

@Composable
fun AddItem(
    measureList: Array<String>,
    editMode: Boolean,
    data: Array<String>,
    context: Activity,
    index: Int,
    result: (Array<String>) -> Unit,
    addCallback: () -> Unit,
    deleteCallback: (key: Int) -> Unit
) {
    val size = remember { mutableStateOf("") }
    val quantity = remember { mutableStateOf("") }
    val price = remember { mutableStateOf("") }
    val selectedItemPriceMeasure = remember { mutableStateOf(-1) }
    val iconType = if (index == 1) {
        Icons.Filled.Add
    } else {
        Icons.Filled.Close
    }
    //Log.e("data", "${data.toString()}")
    if (editMode && data.isNotEmpty()) {
        size.value = data[0]
        quantity.value = data[1]
        price.value = data[2]
        selectedItemPriceMeasure.value = measureList.indexOf(data[3])
    }
    Spacer(modifier = Modifier.height(20.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), contentAlignment = Alignment.Center
    ) {
        Card(modifier = Modifier.fillMaxWidth(), elevation = 3.dp) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp, horizontal = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
//                    Text(text = "Item ${index+1} ", style = TextStyle(fontSize = 15.sp, fontWeight= FontWeight.Bold, fontFamily = FontFamily(fonts = GothamBook), letterSpacing = 1.sp))
                    Box(modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .clickable {
                            if (index == 1) {
                                addCallback()
                            } else {
                                deleteCallback(index)
                            }
                        }, contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconType,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .coloredShadow(
                            color = Color(0xFFfE8C05),
                            borderRadius = 0.dp,
                            alpha = 0.3f,
                            shadowRadius = 2.dp
                        )
                        .padding(vertical = 5.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Spacer(modifier = Modifier.weight(0.05f))
                        Box(modifier = Modifier.weight(0.4f)) {
                            TextField(
                                value = size.value,
                                onValueChange = {
                                    size.value = it
                                }, modifier = Modifier.fillMaxWidth(),
                                label = {
                                    Text(text = "Size")
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color(0xFFFFFFFF),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    textColor = Color.Black
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.05f))
                        Box(modifier = Modifier.weight(0.4f)) {
                            TextField(
                                value = quantity.value,
                                onValueChange = {
                                    quantity.value = it
                                }, modifier = Modifier.fillMaxWidth(),
                                label = {
                                    Text(text = "Quantity")
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color(0xFFFFFFFF),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    textColor = Color.Black
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.05f))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .coloredShadow(
                            color = Color(0xFFfE8C05),
                            borderRadius = 0.dp,
                            alpha = 0.3f,
                            shadowRadius = 2.dp
                        )
                        .padding(vertical = 5.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Spacer(modifier = Modifier.weight(0.05f))
                        Box(modifier = Modifier.weight(0.4f)) {
                            TextField(
                                value = price.value,
                                onValueChange = {
                                    price.value = it
                                }, modifier = Modifier.fillMaxWidth(),
                                label = {
                                    Text(text = "Price")
                                },
                                colors = TextFieldDefaults.textFieldColors(
                                    backgroundColor = Color(0xFFFFFFFF),
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    textColor = Color.Black
                                ),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.05f))
                        Box(modifier = Modifier.weight(0.4f)) {
                            SimpleExposedDropDownMenu(
                                measureList.toList(),
                                selectedItemPriceMeasure.value,
                                onChange = {
                                    selectedItemPriceMeasure.value = it
                                },
                                label = {
                                    Text(text = "Measure")
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        Spacer(modifier = Modifier.weight(0.05f))
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(
                        onClick = {
                            when {
                                size.value.isEmpty() -> {
                                    Helpers.showToast(context, "error", "Please, Enter Size")
                                }
                                quantity.value.isEmpty() -> {
                                    Helpers.showToast(context, "error", "Please, Enter Quantity")
                                }
                                !"[0-9]+".toRegex().matches(quantity.value) -> {
                                    Helpers.showToast(context, "error", "Please, Invalid Quantity")
                                }
                                price.value.isEmpty() -> {
                                    Helpers.showToast(context, "error", "Please, Enter Price")
                                }
                                !"[0-9]+".toRegex().matches(price.value) -> {
                                    Helpers.showToast(context, "error", "Please, Invalid Price")
                                }
                                selectedItemPriceMeasure.value == -1 -> {
                                    Helpers.showToast(context, "error", "Please, Select Measure")
                                }
                                else -> {
                                    Helpers.showToast(context, "success", "Item Added Successfully")
                                    result(
                                        arrayOf(
                                            size.value,
                                            quantity.value,
                                            price.value,
                                            measureList[selectedItemPriceMeasure.value]
                                        )
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth(0.8f)
                            .clip(shape = RoundedCornerShape(8.dp)),
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),
                        contentPadding = PaddingValues(horizontal = 25.dp),
                    ) {
                        Text(
                            text = "Add",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = FontFamily(fonts = GothamMedium),
                                letterSpacing = 2.sp
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun AddApplication(
    value: String = "",
    onChange: (value: String) -> Unit,
    addCallback: () -> Unit,
    size: Int = 0,
    removeCallback: (key: Int) -> Unit
) {

    val iconType = if (size == 1) {
        Icons.Filled.Add
    } else {
        Icons.Filled.Close
    }
    Spacer(modifier = Modifier.height(20.dp))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp), contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .coloredShadow(
                    color = Color(0xFFfE8C05),
                    borderRadius = 0.dp, alpha = 0.3f, shadowRadius = 2.dp
                )
                .padding(vertical = 5.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp), horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(modifier = Modifier.weight(0.85f), contentAlignment = Alignment.Center) {
                    TextField(
                        value = value,
                        onValueChange = {
                            onChange(it)
                        }, modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Application")
                        },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color(0xFFFFFFFF),
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            textColor = Color.Black
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                    )
                }
                Spacer(modifier = Modifier.weight(0.05f))
                Box(modifier = Modifier
                    .weight(0.1f)
                    .padding(top = 16.dp, end = 10.dp)
                    .clickable {
                        if (size == 1) {
                            addCallback()
                        } else {
                            removeCallback(size)
                        }
                    }, contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = iconType,
                        contentDescription = "Delete",
                        tint = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}