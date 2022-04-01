package com.fabnie.vendor.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.fabnie.vendor.R
import com.fabnie.vendor.Screen
import com.fabnie.vendor.widget.CustomTextField

@Composable
fun SignUpScreen(navController: NavHostController) {
    val name = remember { mutableStateOf("") }
    val mobile = remember { mutableStateOf("") }
    val city = remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "right_arrow",
                modifier = Modifier.size(15.dp)
            )
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.fabnie_logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            modifier = Modifier.padding(horizontal = 40.dp),
            text = "Sign Up",
            style = TextStyle(
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
        Text(
            modifier = Modifier.padding(horizontal = 40.dp),
            text = "For your account",
            style = TextStyle(
                color = Color(0XFF6a6a6a),
                fontSize = 16.sp
            )
        )
        CustomTextField(
            placeholder = "Name",
            value = name.value,
            onValueChange = { name.value = it },
            icon = painterResource(id = R.drawable.ic_name)
        )
        CustomTextField(
            placeholder = "Mobile Number",
            value = mobile.value,
            onValueChange = { mobile.value = it },
            icon = painterResource(id = R.drawable.ic_phone)
        )
        CustomTextField(
            placeholder = "City",
            value = city.value,
            onValueChange = { city.value = it },
            icon = painterResource(id = R.drawable.ic_city)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Button(
                onClick = {
                    navController.navigate(Screen.OtpScreen.route)
                },
                shape = CircleShape,
            ) {
                Text(
                    text = "Sign Up ",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp)
                )
            }
        }
    }

}
