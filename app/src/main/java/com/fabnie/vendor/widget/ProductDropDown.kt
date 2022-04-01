package com.fabnie.vendor.widget

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.fabnie.vendor.model.category.Data
import com.fabnie.vendor.utils.Constants
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun ProductDropdownMenu(disbaleSelection:Boolean = false,selectedPos:Int = -1, items: List<com.fabnie.vendor.model.userproduct.Data> = ArrayList<com.fabnie.vendor.model.userproduct.Data>(0), selectedItemData:(id:Int, name:String, productImage:String, productDesc:String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var selectedText by remember { mutableStateOf("") }

    if(selectedPos  >= 0){
        val selectedItem = items[selectedPos]
        selectedText = selectedItem.name
        selectedItemData(selectedItem.id!!, selectedItem.name!!,"${Constants.PRODUCT_IMAGE_BASE_URL}/${selectedItem.thumbnail}",selectedItem.details)
    }

    val labelColor = if (expanded) MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high) else MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)
    val trailingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity)
    val rotation: Float by animateFloatAsState(if (expanded) 180f else 0f)
    val focusManager = LocalFocusManager.current

    val icon = if(expanded){
        Icons.Filled.KeyboardArrowUp
    }else {
        Icons.Filled.KeyboardArrowDown
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .fillMaxWidth()
                .background(color = Color.White, shape = MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize))
                .onGloballyPositioned { textfieldSize = it.size.toSize() }
                .clip(MaterialTheme.shapes.small.copy(bottomEnd = ZeroCornerSize, bottomStart = ZeroCornerSize))
                .clickable {

                    if(!disbaleSelection) {
                        expanded = !expanded
                        focusManager.clearFocus()
                    }

                }
                .padding(start = 16.dp, end = 12.dp, top = 7.dp, bottom = 10.dp)
        ) {
            Column(Modifier.padding(end = 32.dp)) {
                ProvideTextStyle(value = MaterialTheme.typography.caption.copy(color = labelColor)) {
                    Text(text = "Select Product")
                }
                Text(
                    text = selectedText,
                    modifier = Modifier.padding(top = 1.dp)
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = "Change",
                tint = trailingIconColor,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 4.dp)
                    .rotate(rotation)
            )

        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            items.forEachIndexed { i, v ->
                val scope = rememberCoroutineScope()
                DropdownMenuItem(
                    onClick = {
                        selectedItemData(
                            v.id!!,
                            v.name!!,
                            "${Constants.PRODUCT_IMAGE_BASE_URL}/${v.thumbnail}",
                            v.details
                        )
                        scope.launch {
                            delay(150)
                            expanded = false
                            selectedText = v.name
                        }
                    }
                ) {
                    Text(v.name!!, modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
    }
}