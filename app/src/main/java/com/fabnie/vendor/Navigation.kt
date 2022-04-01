package com.fabnie.vendor

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fabnie.vendor.screen.*
import com.fabnie.vendor.viewmodel.LoginViewModel

@Composable
fun Navigation(navHostController: NavHostController, onClick:() -> Unit) {
    NavHost(navController = navHostController, startDestination = Screen.OtpScreen.route) {
        composable(route = Screen.SignUpScreen.route){
            SignUpScreen(navController = navHostController)
        }
        composable(route = Screen.OtpVerifyScreen.route+"/{mobile}/{otp}",
            arguments = listOf(navArgument("mobile"){ defaultValue = ""},
                navArgument("otp"){ defaultValue = ""})
        ){
            OtpVerifyScreen(navController = navHostController, it.arguments?.getString("mobile"),it.arguments?.getString("otp")){
                onClick()
            }
        }
        composable(route = Screen.OtpScreen.route){
            OTPScreen(navController = navHostController)
        }
        composable(route = Screen.PersonalDataScreen.route){
            PersonalDataScreen(navController = navHostController)
        }
        composable(route = Screen.ProfileScreen.route){
            ProfileScreen(navController = navHostController)
        }
    }
}