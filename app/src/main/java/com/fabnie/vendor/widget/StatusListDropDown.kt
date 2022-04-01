package com.fabnie.vendor.widget

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.ui.theme.Purple500
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun StatusListDropDown(
    values: List<String>,
    selectedIndex: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier,
    backgroundColor: Color = Purple500,
    shape: Shape = MaterialTheme.shapes.small.copy(bottomEnd =ZeroCornerSize, bottomStart = ZeroCornerSize)
) {
    SimpleExposedDropDownMenuImpl(
        values = values,
        selectedIndex = selectedIndex,
        onChange = onChange,
        modifier = modifier,
        backgroundColor = backgroundColor,
        shape = shape,
        decorator = { color, width, content ->
            content()
        }
    )
}

@Composable
private fun SimpleExposedDropDownMenuImpl(
    values: List<String>,
    selectedIndex: Int,
    onChange: (Int) -> Unit,
    modifier: Modifier,
    backgroundColor: Color,
    shape: Shape,
    decorator: @Composable (Color, Dp, @Composable () -> Unit) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var textfieldSize by remember { mutableStateOf(Size.Zero) }

    val rotation: Float by animateFloatAsState(if (expanded) 180f else 0f)

    val focusManager = LocalFocusManager.current

    val icon = if(expanded){
        Icons.Filled.KeyboardArrowUp
    }else {
        Icons.Filled.KeyboardArrowDown
    }

    val text = if(selectedIndex >= 0){
        values[selectedIndex]
    }else{
        "Select Status"
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier= Modifier
                .fillMaxWidth()
                .background(color = backgroundColor, shape = CircleShape)
                .onGloballyPositioned { textfieldSize = it.size.toSize() }
                .clip(shape)
                .clickable {
                    expanded = !expanded
                    focusManager.clearFocus()
                }
                .padding(horizontal = 8.dp,vertical = 4.dp)
        ) {
            Column(Modifier.padding(end = 12.dp).align(Alignment.Center)) {
                Text(
                    text = text,
                    style = TextStyle(
                        color= Color.White,
                        fontFamily = FontFamily(fonts = GothamBold),
                        fontSize = 16.sp
                    )
                )
            }
            Icon(
                imageVector = icon,
                contentDescription = "Change",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(top = 4.dp)
                    .rotate(rotation)
            )

        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.width(with(LocalDensity.current) { textfieldSize.width.toDp() })
        ) {
            values.forEachIndexed { i, v ->
                val scope = rememberCoroutineScope()
                DropdownMenuItem(
                    onClick = {
                        onChange(i)
                        scope.launch {
                            delay(150)
                            expanded = false
                        }
                    }
                ) {
                    Text(v, modifier = Modifier.padding(top = 10.dp))
                }
            }
        }
    }
}