package com.fabnie.vendor.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fabnie.vendor.utils.coloredShadow

@Composable
fun CustomTextField(
    placeholder: String,
    value: String,
    onValueChange:(String) -> Unit,
    icon: Painter,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxLength:Int = 200
) {
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 18.dp, end = 18.dp), contentAlignment = Alignment.CenterStart) {
        Box(
            modifier = Modifier
                .coloredShadow(color = Color(0xFFfE8C05), borderRadius = 20.dp, alpha = 0.5f, shadowRadius = 10.dp)
        ) {
            TextField(
                keyboardOptions = KeyboardOptions(keyboardType =keyboardType),
                value = value,
                onValueChange = {
                    if(it.length <= maxLength) {
                        onValueChange(it)
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFfff0dd),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    textColor = Color.Black
                ),
                placeholder = {
                    Text(text = placeholder)
                }, modifier = Modifier
                    .clip(shape = RoundedCornerShape(70.dp))
                    .background(color = Color(0xFFfff0dd))
                    .fillMaxWidth()
                    .padding(start = 70.dp)
            )
        }
        Box(modifier = Modifier
            .coloredShadow(color = Color(0xFFfE8C05), borderRadius = 5.dp, alpha = 0.2f)
            .clip(shape = CircleShape)
            .size(70.dp),
            contentAlignment = Alignment.Center
        ){
            Box(modifier = Modifier.size(70.dp).background(color = Color(0xFFfff0dd), shape = CircleShape), contentAlignment = Alignment.Center){
                Icon(painter = icon, contentDescription = null, tint = Color.Black)
            }
        }
    }
}