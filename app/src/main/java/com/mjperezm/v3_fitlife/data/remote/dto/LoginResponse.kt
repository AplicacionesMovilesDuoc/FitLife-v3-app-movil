package com.mjperezm.v3_fitlife.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("authToken")
    val authToken: String,

    @SerializedName("user")
    val user: UserDto
)
