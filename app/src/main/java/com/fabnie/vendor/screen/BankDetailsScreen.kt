package com.fabnie.vendor.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.R
import com.fabnie.vendor.model.bankdetail.UpdateBankDetailRequestModel
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.ui.theme.GothamMedium
import com.fabnie.vendor.utils.Constants
import com.fabnie.vendor.utils.Helpers
import com.fabnie.vendor.utils.Utils
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.viewmodel.BankDetailViewModel
import com.fabnie.vendor.viewmodel.UpdateBankDetailViewModel
import com.fabnie.vendor.widget.Header
import com.fabnie.vendor.widget.ShowProgressDialog

@Composable
fun BankDetailsScreen(navHostController: NavHostController) {
    val context = LocalContext.current as Activity
    val bankDetailViewModel: BankDetailViewModel = hiltViewModel()
    val updateBankDetailViewModel: UpdateBankDetailViewModel = hiltViewModel()
    val bankDetailState = bankDetailViewModel.bankDetail.collectAsState(Response.Loading()).value
    val updateBankDetailState =
        updateBankDetailViewModel.updateBankDetail.collectAsState(Response.Empty()).value
    val isEdit = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val isUpdating = remember { mutableStateOf(false) }

    //Field Values
    val bankName = remember { mutableStateOf("") }
    val holderName = remember { mutableStateOf("") }
    val ifscCode = remember { mutableStateOf("") }
    val accountNumber = remember { mutableStateOf("") }
    val token = remember { mutableStateOf("") }

    LaunchedEffect(key1 = "BankDetailsScreen") {
        val appPref = BaseApplication.appContext.appPref
        token.value = appPref.getString(Constants.ACCESS_TOKEN).toString()
        bankDetailViewModel.getBankDetail(token = token.value)
    }

    when (bankDetailState) {
        is Response.Empty -> {
            isLoading.value = false
        }
        is Response.Loading -> {
            isLoading.value = true
        }
        is Response.Success -> {
            if(isLoading.value) {
                isLoading.value = false
                //Log.d("[BankDetailScreen]", "Success ${bankDetailState.data?.data}")
                val data = bankDetailState.data?.data!!
                bankName.value = data.bank_name
                holderName.value = data.holder_name
                ifscCode.value = data.ifsc_code
                accountNumber.value = data.account_number
            }
        }
        is Response.Error -> {
            //Log.d("[BankDetailScreen]", "Error ${bankDetailState.error.toString()}")
            isLoading.value = false
        }
    }

    when (updateBankDetailState) {
        is Response.Empty -> {
            isUpdating.value = false
        }
        is Response.Loading -> {
            isUpdating.value = true
        }
        is Response.Success -> {
            if (isUpdating.value) {
                isUpdating.value = false
                isEdit.value = false
                //Log.d("[UpdateBankDetailScreen]", "Success ${updateBankDetailState.data?.data}")
                val data = updateBankDetailState.data?.data!!
                bankName.value = data.bank_name
                holderName.value = data.holder_name
                ifscCode.value = data.ifsc_code
                accountNumber.value = data.account_number
                if (updateBankDetailState.data.success) {
                    Helpers.showToast(context, "success", updateBankDetailState.data.message)
                } else {
                    Helpers.showToast(context, "error", updateBankDetailState.data.message)
                }
            }
        }
        is Response.Error -> {
            //Log.d("[UpdateBankDetailScreen]", "Error ${updateBankDetailState.error.toString()}")
            isLoading.value = false
            Helpers.showToast(context, "error", "Something went wrong!")
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Header(title = "Bank Details", showIcon = !isEdit.value) {
            isEdit.value = !isEdit.value
        }
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
                        Spacer(modifier = Modifier.height(15.dp))
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_bank_details),
                                contentDescription = null,
                                modifier = Modifier.size(50.dp),
                                tint = Color(0XFFfe8c05)
                            )
                        }
                        Spacer(modifier = Modifier.height(15.dp))
                        Divider()
                        if (isEdit.value) {
                            BankDetailsEditItem(label = "Bank Name", value = bankName.value) {
                                    it->
                                bankName.value = it.filter { it.isWhitespace() || it.isLetter()}
                            }
                            Divider()
                            BankDetailsEditItem(label = "Holder Name", value = holderName.value) { it->
                                holderName.value = it.filter { it.isWhitespace() || it.isLetter() }
                            }
                            Divider()
                            BankDetailsEditItem(label = "IFSC Code", value = ifscCode.value) { it ->
                                if(it.length <= 11) {
                                    ifscCode.value = it.filter { it.isLetterOrDigit() }
                                }
                            }
                            Divider()
                            BankDetailsEditItem(
                                label = "Account Number",
                                keyboardType = KeyboardType.Number,
                                value = accountNumber.value
                            ) {
                                it ->
                                if(it.length <= 20) {
                                    accountNumber.value = it.filter { it.isDigit() }
                                }
                            }
                        } else {
                            BankDetailsItem(label = "Bank Name", value = bankName.value)
                            Divider()
                            BankDetailsItem(label = "Holder Name", value = holderName.value)
                            Divider()
                            BankDetailsItem(label = "IFSC Code", value = ifscCode.value)
                            Divider()
                            BankDetailsItem(label = "Account Number", value = accountNumber.value)
                        }

                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
        if (isEdit.value) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        if (bankName.value == "") {
                            Helpers.showToast(context, "error", "Bank Name empty")
                        } else if (holderName.value == "") {
                            Helpers.showToast(context, "error", "Holder Name empty")
                        } else if (ifscCode.value == "") {
                            Helpers.showToast(context, "error", "IFSC Code empty")
                        } else if (ifscCode.value.length != 11) {
                            Helpers.showToast(context, "error", "Please, Enter 11 Digit IFSC Code")
                        } else if (ifscCode.value.isDigitsOnly() || Utils.isLettersOnly(ifscCode.value)) {
                            Helpers.showToast(context, "error", "Please, Enter valid IFSC with letters and numbers format")
                        } else if (accountNumber.value == "") {
                            Helpers.showToast(context, "error", "Account Number empty")
                        } else if (!"[0-9]+".toRegex().matches(accountNumber.value)) {
                            Helpers.showToast(context, "error", "Invalid Account Number")
                        } else {
                            val data = UpdateBankDetailRequestModel(
                                bank_name = bankName.value,
                                holder_name = holderName.value,
                                account_number = accountNumber.value,
                                ifsc_code = ifscCode.value
                            )
                            updateBankDetailViewModel.updateBankDetail(
                                token = token.value,
                                data = data
                            )
                        }

                    },
                    modifier = Modifier
                        .height(50.dp)
                        .clip(shape = RoundedCornerShape(8.dp)),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp,
                        disabledElevation = 0.dp
                    ),
                    contentPadding = PaddingValues(horizontal = 20.dp)
                ) {
                    Text(text = "Update")
                }
            }
            Spacer(modifier = Modifier.height(100.dp))
            ShowProgressDialog(showDialog = isUpdating.value) {
            }
            BackHandler(enabled = isEdit.value, onBack = {
                isEdit.value = false
            })
        }
    }
}

@Composable
fun BankDetailsItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label, style = TextStyle(
                color = Color(0xFF3A3A3A),
                fontSize = 20.sp,
                fontFamily = FontFamily(fonts = GothamMedium),
            )
        )
        Text(
            text = value,
            style = TextStyle(
                color = Color.Black,
                fontSize = 20.sp,
                fontFamily = FontFamily(fonts = GothamMedium),

                ),
            modifier = Modifier
                .padding(start = 20.dp),
        )
    }
}

@Composable
fun BankDetailsEditItem(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            label = {
                Text(text = label)
            },
            value = value,
            onValueChange = {
                onValueChange(it)
            },
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFfff0dd),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = Color.Black
            ),
            placeholder = {
                Text(text = label)
            }, modifier = Modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color(0xFFfff0dd))
                .fillMaxWidth()
                .border(1.dp, color = Color(0xFFfE8C05), shape = RoundedCornerShape(8.dp))
        )
    }
}