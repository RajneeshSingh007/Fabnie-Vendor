package com.fabnie.vendor

sealed class Screen(val route: String) {
    object OtpScreen: Screen(route = "otp_screen")
    object PaymentScreen: Screen(route = "payment_screen")
    object PersonalDataScreen: Screen(route = "personal_data_screen")
    object ProfileScreen: Screen(route = "profile_screen")
    object SignUpScreen: Screen(route = "signup_screen")
    object OtpVerifyScreen: Screen(route = "otp_verify")
}
