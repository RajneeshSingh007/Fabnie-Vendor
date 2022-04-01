package com.fabnie.vendor.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusOrder
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.utils.coloredShadow


@Composable
fun OtpInput(value: String, onValueChange: (String)->Unit,
             focusRequester: FocusRequester,
             nextFocusRequester: FocusRequester? = null,
             imeAction: ImeAction = androidx.compose.ui.text.input.ImeAction.Next){
    val focusManager = LocalFocusManager.current
    Box(modifier = Modifier.width(60.dp)
        .height(60.dp)
        .coloredShadow(color = Color(0xFFfE8C05), borderRadius = 15.dp, alpha = 0.5f, shadowRadius = 8.dp )
        .clip(RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ){
        TextField(
            value = value,
            onValueChange = {
                if(it.isEmpty() || it.length <= 1 && it.isDigitsOnly()){
                    onValueChange(it)
                    if(it.isNotEmpty()){
                        nextFocusRequester?.requestFocus()
                    }
                }
            },

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFfff0dd),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                textColor = Color.Black
            ),
            modifier = Modifier
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = Color(0xFFfff0dd))
                .size(60.dp)
                .focusRequester(focusRequester)
                .focusOrder(focusRequester)
                .onKeyEvent {
                    if (it.nativeKeyEvent.keyCode == 67) {
                        if (value.isEmpty()) {
                            focusManager.moveFocus(FocusDirection.Left)
                        }
                    }
                    true
                },
            textStyle = TextStyle(textAlign = TextAlign.Center, fontSize = 24.sp, fontFamily = FontFamily(fonts = GothamBold))
        )
    }
}