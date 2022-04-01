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
fun QuantityDropdown(disbaleSelection:Boolean = false,selectedData:String = "-1", items: List<String> = ArrayList<String>(0), selectedItemData:(name:String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }
    var selectedText by remember { mutableStateOf("") }

    if(selectedData  != "-1"){
        selectedText = selectedData
        selectedItemData(selectedData)
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
                    Text(text = "Select Stock")
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
                        selectedItemData(v)
                        scope.launch {
                            delay(150)
                            expanded = false
                            selectedText = v
                        }
                    }
                ) {
                    Text(v, modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
    }
}