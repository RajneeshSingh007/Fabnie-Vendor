package com.fabnie.vendor.network

import com.fabnie.vendor.model.*
import com.fabnie.vendor.model.addproduct.AddProductRequestModel
import com.fabnie.vendor.model.addproduct.AddProductResponseModel
import com.fabnie.vendor.model.bankdetail.BankDetailResponseModel
import com.fabnie.vendor.model.bankdetail.UpdateBankDetailRequestModel
import com.fabnie.vendor.model.category.CategoryResponseModel
import com.fabnie.vendor.model.editproduct.EditProductResponseModel
import com.fabnie.vendor.model.login.LoginResponseModel
import com.fabnie.vendor.model.notification.NotificationResponseModel
import com.fabnie.vendor.model.notificationlist.NotificationList
import com.fabnie.vendor.model.orders.OrderDetailResponseModel
import com.fabnie.vendor.model.orders.OrderResponseModel
import com.fabnie.vendor.model.product.ProductResponseModel
import com.fabnie.vendor.model.profile.ProfileResponseModel
import com.fabnie.vendor.model.subcategory.SubCategoryResponseModel
import com.fabnie.vendor.model.userproduct.UserProductResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("vendor/all_products")
    suspend fun getProducts(@Header("Authorization") authorization: String): Response<ProductResponseModel>

    @GET("user_products")
    suspend fun getUserProducts(@Header("Authorization") authorization: String): Response<UserProductResponseModel>

    @POST("auth/login")
    suspend fun loginUser(@Body body: LoginRequestModel): Response<LoginResponseModel>

    @Multipart
    @POST("send-mobile-otp")
    suspend fun sendOtp( @Part("mobile_no") mobile_no: RequestBody): Response<Otp>

    @POST("vendor/add_product")
    suspend fun addProduct(
        @Header("Authorization") authorization: String,
        @Body body: AddProductRequestModel
    ): Response<AddProductResponseModel>

    @POST("vendor/update_product")
    suspend fun updateProduct(
        @Header("Authorization") authorization: String,
        @Body body: AddProductRequestModel
    ): Response<AddProductResponseModel>

    @GET("auth/edit_product/{id}")
    suspend fun editProduct(@Path("id") id: Int): EditProductResponseModel

    @GET("get_categories")
    suspend fun getCategories(): Response<CategoryResponseModel>

    @GET("get_subcategories/{id}")
    suspend fun getSubCategories(@Path("id") id: Int): Response<SubCategoryResponseModel>

    @GET("vendor/get_profile")
    suspend fun getProfile(@Header("Authorization") authorization: String): Response<ProfileResponseModel>

    @Multipart
    @POST("vendor/update_profile")
    suspend fun updateProfile(
        @Header("Authorization") authorization: String,
        @Part photo: MultipartBody.Part,
        @Part("firstname") firstname: RequestBody,
        @Part("lastname") lastname: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("city") city: RequestBody,
        @Part("area") area: RequestBody,
        @Part("address") address: RequestBody,
        ): Response<ProfileResponseModel>

    @Multipart
    @POST("vendor/update_profile")
    suspend fun updateProfile(
        @Header("Authorization") authorization: String,
        @Part("firstname") firstname: RequestBody,
        @Part("lastname") lastname: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("email") email: RequestBody,
        @Part("city") city: RequestBody,
        @Part("area") area: RequestBody,
        @Part("address") address: RequestBody,
    ): Response<ProfileResponseModel>


    @GET("vendor/get_bank_details")
    suspend fun getBankDetail(@Header("Authorization") authorization: String): Response<BankDetailResponseModel>

    @POST("vendor/update_bank_details")
    suspend fun updateBankDetail(
        @Header("Authorization") authorization: String,
        @Body body: UpdateBankDetailRequestModel
    ): Response<BankDetailResponseModel>

    @GET("vendor/get_push_notification")
    suspend fun getNotifications(@Header("Authorization") authorization: String): Response<NotificationResponseModel>

    @GET("get_measurements")
    suspend fun getMeasurements(@Header("Authorization") authorization: String): Response<MeasurementResponseModel>

    @Multipart
    @POST("vendor/update_push_notification")
    suspend fun pushNotifications(
        @Header("Authorization") authorization: String,
        @Part("push_notification") push_notification: RequestBody,
    ): Response<NotificationResponseModel>

    @GET("vendor/notifications")
    suspend fun getNotificationList(@Header("Authorization") authorization: String): Response<NotificationList>

    @GET("vendor/more-notifications/{id}")
    suspend fun getNotificationList(
        @Path("id") id: Int,
        @Header("Authorization") authorization: String
    ): Response<NotificationList>

    @GET("vendor/delete-notification/{id}")
    suspend fun deleteNotification(
        @Path("id") id: Int,
        @Header("Authorization") authorization: String
    ): Response<ResponsesModel>

    @GET("vendor/all_orders")
    suspend fun getOrders(@Header("Authorization") authorization: String): Response<OrderResponseModel>

    @GET("vendor/order_detail/{id}")
    suspend fun getOrderDetails(
        @Path("id") id: Int,
        @Header("Authorization") authorization: String
    ): Response<OrderDetailResponseModel>

    @Multipart
    @POST("vendor/order_status_change")
    suspend fun updateOrderStatus(
        @Header("Authorization") authorization: String,
        @Part("order_id") order_Id: RequestBody,
        @Part("status") status: RequestBody,
    ): Response<ResponsesModel>


    @GET("vendor/payments")
    suspend fun getPaymentDetails(@Header("Authorization") authorization: String): Response<PaymentResponseModel>

    @GET("vendor/payments")
    suspend fun getPaymentDetailsByMonth(
        @Query("month") month: Int,
        @Header("Authorization") authorization: String
    ): Response<PaymentResponseModel>

}