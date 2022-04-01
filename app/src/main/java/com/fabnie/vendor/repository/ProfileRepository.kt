package com.fabnie.vendor.repository

import android.util.Log
import com.fabnie.vendor.model.profile.ProfileResponseModel
import com.fabnie.vendor.model.profile.UpdateProfileRequestModel
import com.fabnie.vendor.network.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProfileRepository
@Inject
constructor(private val apiService: ApiService) {

    //Get
    private val profileFlowData = MutableStateFlow<Response<ProfileResponseModel>>(Response.Empty())
    val profileData: StateFlow<Response<ProfileResponseModel>>
        get() = profileFlowData

    //Update
    private val updateProfileFlowData =
        MutableStateFlow<Response<ProfileResponseModel>>(Response.Empty())
    val updateProfileData: StateFlow<Response<ProfileResponseModel>>
        get() = updateProfileFlowData

    suspend fun getProfile(token: String) {
        Log.d("AuthToken", "$token")
        try {
            profileFlowData.emit(Response.Loading())
            val result = apiService.getProfile(authorization = "Bearer $token")
            Log.d("[Profile]", "Success- ${result.body()}")
            if (result.body() != null) {
                profileFlowData.emit(Response.Success(data = result.body()))
            } else {
                profileFlowData.emit(Response.Error(error = "Something wents wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[Profile]", "Exception- ${e.message.toString()}")
            profileFlowData.emit(Response.Error(error = e.message.toString()))
        }
    }

    suspend fun updateProfile(token: String, data: UpdateProfileRequestModel) {
        Log.d("AuthToken", "$token")
        val file = File(data.photo)
        Log.d("FILE9999", "$file")
        try {
            updateProfileFlowData.emit(Response.Loading())

            val requestBody: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            val parts: MultipartBody.Part =
                MultipartBody.Part.createFormData("photo", file.name, requestBody)
            val firstname: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"), data.firstname)
            val lastname: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"), data.lastname)
            val phone: RequestBody = RequestBody.create(MediaType.parse("text/plain"), data.phone)
            val email: RequestBody = RequestBody.create(MediaType.parse("text/plain"), data.email)
            val city: RequestBody = RequestBody.create(MediaType.parse("text/plain"), data.city)
            val area: RequestBody = RequestBody.create(MediaType.parse("text/plain"), data.area)
            val address: RequestBody = RequestBody.create(MediaType.parse("text/plain"), data.address)
            val result = if (file.exists()) {
                apiService.updateProfile(
                    authorization = "Bearer $token",
                    photo = parts,
                    firstname = firstname,
                    lastname = lastname,
                    phone = phone,
                    email = email,
                    city = city,
                    area = area,
                    address = address
                )
            } else {
                apiService.updateProfile(
                    authorization = "Bearer $token",
                    firstname = firstname,
                    lastname = lastname,
                    phone = phone,
                    email = email,
                    city = city,
                    area = area,
                    address = address
                )
            }
            Log.d("[UpdateProfile]", "Success- ${result.body()}")
            if (result.code() == 200) {
                updateProfileFlowData.emit(Response.Success(data = result.body()))
            } else {
                updateProfileFlowData.emit(Response.Error(error = "Something wents wrong!"))
            }
        } catch (e: Exception) {
            Log.d("[UpdateProfile]", "Exception- ${e.message.toString()}")
            updateProfileFlowData.emit(Response.Error(error = "Something wents wrong!"))
        }
    }
}