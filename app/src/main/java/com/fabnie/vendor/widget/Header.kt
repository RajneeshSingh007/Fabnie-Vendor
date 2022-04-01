package com.fabnie.vendor.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fabnie.vendor.R
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.ui.theme.GothamMedium

@Composable
fun Header(title: String, showIcon: Boolean = false, onClick: ()-> Unit = {}) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color(0xFFfe8c05), shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 20.dp, end = 20.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier
                .width(3.dp)
                .height(25.dp)
                .background(color = Color.White))
            Text(text = title,
                modifier = Modifier.padding(start = 20.dp),
                style = TextStyle(fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontFamily = FontFamily(fonts = GothamBold)
                )
            )
        }
        Box(modifier = Modifier
            .fillMaxWidth().height(50.dp)
            .background(
                color = Color(0xFFfe8c05), shape = RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp)),
                contentAlignment = Alignment.CenterEnd) {
            if(showIcon){
                IconButton(onClick = { onClick() }) {
                    Icon(painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = "edit",
                        modifier = Modifier.size(25.dp),
                        tint = Color.White
                    )
                }
            }
        }
    }
}