package com.fabnie.vendor.model.profile

import android.net.Uri
import java.io.File

data class UpdateProfileRequestModel(
    val firstname: String ="",
    val lastname: String ="",
    val email: String ="",
    val phone: String ="",
//    val photo:File,
    var photo: String ="",
    var city: String ="",
    var area: String ="",
    var address: String ="",

    )
