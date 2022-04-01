package com.fabnie.vendor.screen

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fabnie.vendor.R
import com.fabnie.vendor.model.login.Data
import com.fabnie.vendor.model.profile.UpdateProfileRequestModel
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.viewmodel.LoginViewModel
import com.fabnie.vendor.viewmodel.ProfileViewModel
import com.fabnie.vendor.viewmodel.UpdateProfileViewModel
import com.fabnie.vendor.widget.Header
import com.fabnie.vendor.widget.ShowProgressDialog
import java.io.File
import android.database.Cursor
import android.os.PatternMatcher
import android.util.Patterns
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import coil.compose.rememberImagePainter
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.utils.Constants
import com.fabnie.vendor.utils.Helpers
import com.fabnie.vendor.utils.ImagePathUtil
import com.fabnie.vendor.view.BackHandler


@Composable
    fun PersonalDataScreen(navController: NavHostController) {
        val context = LocalContext.current as Activity
        val profileViewModel: ProfileViewModel = hiltViewModel()
        val profileState = profileViewModel.profile.collectAsState().value
        val updateProfileViewModel: UpdateProfileViewModel = hiltViewModel()
        val updateProfileDetailState = updateProfileViewModel.updateProfile.collectAsState(Response.Empty()).value
        val isLoading = remember { mutableStateOf(false) }
        val isUpdating = remember { mutableStateOf(false) }
        val isEdit = remember { mutableStateOf(false)}
        //Fields
        val firstName = remember {mutableStateOf("") }
        val lastName = remember {mutableStateOf("") }
        val phone = remember {mutableStateOf("") }
        val email = remember {mutableStateOf("") }
        val area = remember {mutableStateOf("") }
        val city = remember {mutableStateOf("") }
        val address = remember {mutableStateOf("") }

    //Profile Image
        val imageUri = remember { mutableStateOf<Uri?>(null) }
        val imagePath = remember { mutableStateOf("") }
        val token = remember { mutableStateOf("") }

        LaunchedEffect(key1 = true) {
            val appPref = BaseApplication.appContext.appPref
            token.value = appPref.getString(Constants.ACCESS_TOKEN).toString()
            profileViewModel.getProfile(token = token.value)
        }

        when(profileState){
            is Response.Empty -> {
                isLoading.value = false
            }
            is Response.Loading -> {
                isLoading.value = true
            }
            is Response.Success -> {
                //Log.d("[ProfileScreen]", "Success ${profileState.data?.data}")
                val data = profileState.data?.data!!
                firstName.value = data.firstname
                lastName.value = data.lastname
                email.value = data.email
                phone.value = data.phone
                area.value = data.area
                city.value = data.city
                address.value = data.address
                if(data.photo != null && data.photo.isNotEmpty()) {
                    imageUri.value = Uri.parse("${Constants.USER_IMAGE_BASE_URL}${data.photo}")
                }
            }
            is Response.Error -> {
                //Log.d("[ProfileScreen]", "Error ${profileState.error.toString()}")
                isLoading.value = false
            }
        }

        when(updateProfileDetailState){
            is Response.Empty -> {
                isUpdating.value = false
            }
            is Response.Loading -> {
                isUpdating.value = true
            }
            is Response.Success -> {
                if(isUpdating.value){
                    isUpdating.value = false
                    isEdit.value = false
                    val data = updateProfileDetailState.data?.data!!
                    firstName.value = data.firstname
                    lastName.value = data.lastname
                    email.value = data.email
                    phone.value = data.phone
                    area.value = data.area
                    city.value = data.city
                    address.value = data.address
                    if(data.photo != null && data.photo.isNotEmpty()) {
                        imageUri.value = Uri.parse("${Constants.USER_IMAGE_BASE_URL}${data.photo}")
                    }
                    if(updateProfileDetailState.data.success){
                        profileViewModel.getProfile(token = token.value)
                        Helpers.showToast(context,"success", "Profile Updated Successfully")
                    }else{
                        Helpers.showToast(context,"error", updateProfileDetailState.data.message)
                    }
                }
                //Log.d("[UpdateProfileScreen]", "Success ${updateProfileDetailState.data?.data}")
            }
            is Response.Error -> {
                Helpers.showToast(context,"error", updateProfileDetailState.error.toString());
                //Log.d("[UpdateProfileScreen]", "Error ${updateProfileDetailState.error.toString()}")
                isUpdating.value = false
            }
        }

        val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),){uri ->
            imageUri.value = uri
            val getPath = ImagePathUtil.getFilePathByUri(context, uri as Uri).toString()
            if(getPath != null && getPath != "null"){
                imagePath.value = getPath.toString()
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Header(title = "Personal Data", showIcon = !isEdit.value){
                isEdit.value = !isEdit.value
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp))
            Box(modifier = Modifier.padding(horizontal = 10.dp)){
                Box(modifier = Modifier
                    .coloredShadow(
                        color = Color(0xFFfE8C05),
                        borderRadius = 20.dp, alpha = 0.3f, shadowRadius = 5.dp
                    )
                    .padding(vertical = 5.dp),
                    contentAlignment = Alignment.Center
                ){
                    Card(modifier =
                    Modifier.padding(horizontal = 5.dp),
                        elevation = 0.dp,
                        shape = RoundedCornerShape(CornerSize(20.dp)),
                        backgroundColor = Color(0xFFfff0dd)
                    ) {

                        Column {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Box(modifier =
                                Modifier
                                    .size(80.dp)
                                    .background(color = Color.White, shape = CircleShape)){
                                    if(imageUri.value != null && imageUri.value.toString().isNotEmpty()){
                                        Image(
                                            painter = rememberImagePainter(imageUri.value),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(CircleShape)
                                        )
                                    }else{
                                        Image(
                                            painter = painterResource(R.drawable.fabnie_logo),
                                            contentDescription = "",
                                            modifier = Modifier
                                                .size(80.dp)
                                                .clip(CircleShape)
                                        )
                                    }
                                }
                                Text(text = "Change Photo",
                                    modifier = Modifier
                                        .padding(start = 20.dp, top = 10.dp)
                                        .clickable {
                                            launcher.launch("image/*")
                                        },
                                    style = TextStyle(
                                        color= Color(color = 0XFFfe8c05),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily(fonts = GothamBold)
                                    )
                                )
                            }
                            Divider()
                            if(isEdit.value){
                                PersonalDetailEditItem(
                                    label = "First Name",
                                    value = firstName.value,
                                    onValueChange = { it->
                                        firstName.value = it.filter { it.isLetter() }
                                    },
                                )
                                Divider()
                                PersonalDetailEditItem(
                                    label = "Last Name",
                                    value = lastName.value,
                                    onValueChange = {it->
                                        lastName.value = it.filter { it.isLetter() }
                                    },
                                )
                                Divider()
                                PersonalDetailEditItem(
                                    label = "Email",
                                    value = email.value,
                                    onValueChange = {
                                        email.value = it
                                    },
                                    keyboardType = KeyboardType.Email
                                )
                                Divider()
                                PersonalDetailEditItem(
                                    label = "Phone",
                                    value = phone.value,
                                    onValueChange = { it->
                                        phone.value = it.filter { it.isDigit() }
                                    },
                                    keyboardType = KeyboardType.Number,
                                    maxLength = 10
                                )
                                Divider()
                                PersonalDetailEditItem(
                                    label = "Address",
                                    value = address.value,
                                    onValueChange = { it->
                                        address.value = it
                                            //.filter { it.isLetterOrDigit() || it.isWhitespace()}
                                    },
                                    keyboardType = KeyboardType.Text,
                                )
                                Divider()
                                PersonalDetailEditItem(
                                    label = "Area",
                                    value = area.value,
                                    onValueChange = { it->
                                        area.value = it
                                            //.filter { it.isLetter() || it.isWhitespace()}
                                    },
                                    keyboardType = KeyboardType.Text,
                                )
                                Divider()
                                PersonalDetailEditItem(
                                    label = "City",
                                    value = city.value,
                                    onValueChange = { it->
                                        city.value = it
                                            //.filter { it.isLetter()}
                                    },
                                    keyboardType = KeyboardType.Text,
                                )
                            }else {
                               ListItem(
                                   icon = painterResource(id = R.drawable.ic_name),
                                   label = "First Name",
                                   value = firstName.value
                               )
                                ListItem(
                                    icon = painterResource(id = R.drawable.ic_name),
                                    label = "Last Name",
                                    value = lastName.value
                                )
                                ListItem(
                                    icon = painterResource(id = R.drawable.ic_mail),
                                    label = "Email",
                                    value = email.value
                                )
                                ListItem(
                                    icon = painterResource(id = R.drawable.ic_phone),
                                    label = "Phone",
                                    value = phone.value
                                )
                                ListItem(
                                    icon = painterResource(id = R.drawable.ic_city),
                                    label = "Address",
                                    value = address.value
                                )
                                ListItem(
                                    icon = painterResource(id = R.drawable.ic_city),
                                    label = "Area",
                                    value = area.value
                                )
                                ListItem(
                                    icon = painterResource(id = R.drawable.ic_city),
                                    label = "City",
                                    value = city.value
                                )

                            }
                        }
                    }
                }
            }

            if(isEdit.value){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            if(firstName.value == ""){
                                Helpers.showToast(context, "error", "First Name empty")
                            }else if(firstName.value.length > 12){
                                Helpers.showToast(context, "error", "First Name can't accept more than 12 characters")
                            }else if(lastName.value == ""){
                                Helpers.showToast(context, "error", "Last Name empty")
                            }else if(email.value == ""){
                                Helpers.showToast(context, "error", "Email empty")
                            }else if(!Patterns.EMAIL_ADDRESS.matcher(email.value).matches()){
                                Helpers.showToast(context, "error", "Invalid Email")
                            }else if(phone.value == ""){
                                Helpers.showToast(context, "error", "Mobile Number empty")
                            }else if(!"[0-9]+".toRegex().matches(phone.value)){
                                Helpers.showToast(context, "error", "Invalid Mobile Number")
                            } else if(phone.value.length != 10){
                                Helpers.showToast(context,"error", "Please, Enter 10 Digit Mobile Number")
                            }
                            //else if(imagePath.value == ""){
                              //  Helpers.showToast(context, "error", "Please, Select Profile Image")
                            //}
                            else {
                                val data = UpdateProfileRequestModel(
                                    firstname = firstName.value,
                                    lastname = lastName.value,
                                    email = email.value,
                                    phone = phone.value,
                                    city = city.value,
                                    area = area.value,
                                    address = address.value,
                                )
                                if(imagePath.value.isNotEmpty()){
                                    data.photo = imagePath.value
                                }
                                updateProfileViewModel.updateProfile(token = token.value, data = data)
                            }
                        },
                        shape = CircleShape,
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp,
                            disabledElevation = 0.dp
                        ),
                    ) {
                        Text(
                            text = "Update",
                            style =
                            TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                fontFamily = FontFamily(fonts = GothamBold)
                            ),
                            modifier = Modifier.padding(start = 30.dp, end = 30.dp),
                        )
                    }
                }
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(100.dp))
            ShowProgressDialog(showDialog = isUpdating.value) {
            }
            BackHandler(enabled = isEdit.value,onBack = {
                isEdit.value = false
            })
        }
    }


    @Composable
    fun ListItem(icon: Painter, label: String, value: String) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon, contentDescription = null,
                modifier = Modifier.size(25.dp),
                tint = Color(0XFFfe8c05)
            )
            Column() {
                Text(text = label,
                    modifier = Modifier.padding(start = 10.dp),
                    style = TextStyle(
                        color= Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(fonts = GothamBold)
                    )
                )
                Text(text = value,
                    modifier = Modifier.padding(start = 10.dp),
                    style = TextStyle(
                        color= Color(color = 0xFF3a3a3a),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily(fonts = GothamBold)
                    )
                )
            }
        }
    }

@Composable
fun PersonalDetailEditItem(label: String, value: String, keyboardType: KeyboardType = KeyboardType.Text, onValueChange: (String)->Unit, maxLength:Int = 250){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            label={
                Text(text = label)
            },
            value = value,
            onValueChange = {
                if(it.length <= maxLength) {
                    onValueChange(it)
                }
            },
            keyboardOptions= KeyboardOptions(keyboardType = keyboardType),
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
