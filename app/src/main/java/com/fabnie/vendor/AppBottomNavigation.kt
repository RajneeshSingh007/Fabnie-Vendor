package com.fabnie.vendor

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fabnie.vendor.screen.*
import com.fabnie.vendor.screen.OrderStatusScreen
import com.fabnie.vendor.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun AppBottomNavigation(navHostController: NavHostController,resetRoute:() -> Unit) {
    NavHost(
        navController = navHostController,
        startDestination = BottomBarScreen.Dashboard.route,
    ){
        composable(route = BottomBarScreen.Dashboard.route){
            DashboardScreen(navHostController = navHostController)
        }
        composable(route = BottomBarScreen.Products.route){
            ProductListScreen(navController = navHostController)
        }
        composable(route = BottomBarScreen.PersonalData.route){
            PersonalDataScreen(navController = navHostController)
        }

        composable(route = BottomBarScreen.Notifications.route){
            NotificationScreen(navController = navHostController)
        }

        composable(route = BottomBarScreen.OrderStatus.route+"/{order_id}",
            arguments = listOf(navArgument("order_id"){defaultValue = -1})){
            OrderStatusScreen(navController = navHostController,it.arguments?.getInt("order_id"))
        }

        composable(route = BottomBarScreen.Orders.route){
            OrderScreen(navHostController = navHostController)
        }

        composable(route = BottomBarScreen.Settings.route){
            SettingsScreen(navHostController = navHostController){
                resetRoute()
            }
        }
        composable(route = BottomBarScreen.Profile.route){
            ProfileScreen(navController = navHostController)
        }
        composable(route = BottomBarScreen.BankDetails.route){
            BankDetailsScreen(navHostController = navHostController)
        }
        composable(route = BottomBarScreen.Payments.route){
            MyEarningsScreen(navHostController = navHostController)
        }
        composable(route = BottomBarScreen.AddProduct.route+"/{id}/{product_id}",
            arguments = listOf(navArgument("id"){ defaultValue =-1},
                navArgument("product_id"){ defaultValue =-1})
        ){
            val parentEntry = remember {navHostController.getBackStackEntry(BottomBarScreen.Products.route)}
            val productVM = hiltViewModel<ProductViewModel>(parentEntry)
            AddProductScreen(navHostController = navHostController, productVM, it.arguments?.getInt("id"), it.arguments?.getInt("product_id"))
        }
        composable(route = BottomBarScreen.AddProduct.route){
            AddProductScreen(navHostController = navHostController, null, -1, -1)
        }
    }
}


