package com.fabnie.vendor

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.fabnie.vendor.ui.theme.FabineUserTheme
import com.fabnie.vendor.utils.Constants
import com.fabnie.vendor.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appPref = BaseApplication.appContext.appPref
        lifecycleScope.launch{
            val value = appPref.getString(Constants.IS_LOGGEDIN)
            var isLoggedIn = false
            if (value == null || value == ""){

            }else{
                isLoggedIn = value.toBoolean()
            }
            setContent {
                FabineUserTheme(darkTheme = false) {
                    MainScreen(isLoggedIn)
                }
            }
        }

        val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this@MainActivity,permissions,1500)
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(isLoggedIn:Boolean = false) {
    val navHostController = rememberNavController()
    val isLoggedIn = remember { mutableStateOf(isLoggedIn) }
    Scaffold(
        bottomBar = {
            if(isLoggedIn.value){
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
                        )
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp)
                        ),
                    elevation = 0.dp,
                    backgroundColor = Color.White,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    CustomBottomBar(navHostController = navHostController)
                }
            }
        },
    ) {
        Surface(color = MaterialTheme.colors.background,
                    modifier = Modifier.verticalScroll(state = ScrollState(0))) {
            if(isLoggedIn.value){
                AppBottomNavigation(navHostController = navHostController){
                    isLoggedIn.value = false
                }
            }else {
                Navigation(navHostController = navHostController){
                    isLoggedIn.value = true
                }
            }
        }
    }
}