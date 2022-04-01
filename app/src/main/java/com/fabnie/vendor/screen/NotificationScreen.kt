package com.fabnie.vendor.screen

import android.app.Activity
import android.util.Log
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.fabnie.vendor.BaseApplication
import com.fabnie.vendor.R
import com.fabnie.vendor.data.NotificationItem
import com.fabnie.vendor.data.OrderState
import com.fabnie.vendor.model.login.Data
import com.fabnie.vendor.repository.Response
import com.fabnie.vendor.ui.theme.GothamBold
import com.fabnie.vendor.ui.theme.GothamBook
import com.fabnie.vendor.ui.theme.GothamMedium
import com.fabnie.vendor.utils.Constants
import com.fabnie.vendor.utils.Helpers
import com.fabnie.vendor.utils.Utils
import com.fabnie.vendor.utils.coloredShadow
import com.fabnie.vendor.viewmodel.LoginViewModel
import com.fabnie.vendor.viewmodel.NotificationListViewModel
import com.fabnie.vendor.viewmodel.NotificationViewModel
import com.fabnie.vendor.viewmodel.ResponseViewModel
import com.fabnie.vendor.widget.Header
import com.fabnie.vendor.widget.ShowProgressDialog
import java.util.*

    @Composable
    fun NotificationScreen(navController: NavHostController) {
        val context = LocalContext.current as Activity
        val notificationViewModel: NotificationListViewModel = hiltViewModel()
        val notificationState = notificationViewModel.notifications.collectAsState().value
        val isLoading = remember { mutableStateOf(false) }
        val responseViewModel: ResponseViewModel = hiltViewModel()
        val responseState = responseViewModel.response.collectAsState().value
        val deleteLoading = remember { mutableStateOf(false) }
        val token = remember { mutableStateOf("") }
        val notificationList = remember { mutableStateListOf<com.fabnie.vendor.model.notificationlist.Data>() }
        val activePos = remember { mutableStateOf(-1)}

        LaunchedEffect(key1 = "NotificationScreen") {
            val appPref = BaseApplication.appContext.appPref
            token.value = appPref.getString(Constants.ACCESS_TOKEN).toString()
            notificationViewModel.getNotificationsList(token = token.value)
        }

        when(responseState){
            is Response.Empty -> {
                deleteLoading.value = false
            }
            is Response.Loading -> {
                deleteLoading.value = true
            }
            is Response.Success -> {
                deleteLoading.value = false
                if(activePos.value != -1){
                    notificationList.removeAt(activePos.value)
                    Helpers.showToast(context, "success", "Notification deleted successfully")
                    activePos.value = -1
                }
            }
            is Response.Error -> {
                deleteLoading.value = false
                if(activePos.value != -1){
                    Helpers.showToast(context, "error", "Failed to delete notification")
                    activePos.value = -1
                }
            }
        }

        when(notificationState){
            is Response.Empty -> {
                isLoading.value = false
            }
            is Response.Loading -> {
                isLoading.value = true
            }
            is Response.Success -> {
                isLoading.value = false
                notificationState.data?.data?.let {
                    if(notificationList.isEmpty()) {
                        notificationList.clear()
                        notificationList.addAll(it)
                    }
                }
            }
            is Response.Error -> {
                isLoading.value = false
            }
        }

        Column(modifier = Modifier.fillMaxSize()) {
            Header(title = "Notifications", showIcon = false)
            Spacer(modifier = Modifier.height(10.dp))
            if(isLoading.value){
                ShowLoading()
            }else if(notificationList.isNotEmpty()){
                notificationList.forEachIndexed{index,it ->
                    ListItem(it, index, deleteCallback = { pos->
                        activePos.value = pos
                        val data = notificationList[pos]
                        responseViewModel.deleteNotifications(data.id.toInt(),token = token.value)
                    })
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = { /*TODO*/ },
                    shape = CircleShape,
                ) {
                    Text(text = "Load More", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp),
                        modifier = Modifier
                        .padding(start = 30.dp, end = 30.dp),)
                }
            }
            Box(modifier = Modifier.height(80.dp))
        }
        ShowProgressDialog(showDialog = deleteLoading.value) {
        }
    }

@Composable
fun ListItem(item: com.fabnie.vendor.model.notificationlist.Data, pos:Int, deleteCallback:(pos:Int)->Unit) {
    Spacer(modifier = Modifier.height(6.dp))
    Card(modifier =
    Modifier.padding(start = 20.dp, end = 20.dp, top = 15.dp),
        elevation = 1.dp,
        shape = RoundedCornerShape(CornerSize(15.dp)),
        backgroundColor = Color(0xFFfff0dd)
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 15.dp, top = 10.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp, top = 10.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ){
                    Image(
                        painter = painterResource(id = R.drawable.ic_clock), contentDescription = null,
                    )
                    Text(text = Utils.formatDate(item.created_at),
                        modifier = Modifier.padding(start = 10.dp),
                        style = TextStyle(
                            color= Color.Black,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily(fonts = GothamMedium)
                        )
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(modifier = Modifier.padding(bottom = 8.dp)) {
                        Text(text = item.subject,
                            modifier = Modifier.padding(start = 10.dp, bottom = 10.dp),
                            style = TextStyle(
                                color= Color.Black,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily(fonts = GothamBold)
                            )
                        )
                        Text(text = item.message,
                            modifier = Modifier.padding(start = 10.dp),
                            style = TextStyle(
                                color= Color(color = 0xFF494949),
                                fontSize = 14.sp,
                                fontFamily = FontFamily(fonts = GothamBook),
                                textAlign = TextAlign.Left
                            )
                        )
                    }
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ){
                        IconButton(onClick = {
                            deleteCallback(pos)
                        }) {
                            Icon(painter = painterResource(id = R.drawable.ic_delete_),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(45.dp)
                                    .padding(end = 20.dp),
                                tint = Color(0xFFFF9800)
                            )
                        }
                    }
                }
            }
        }
    }
}
