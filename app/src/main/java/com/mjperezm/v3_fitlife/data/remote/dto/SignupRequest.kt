package com.mjperezm.v3_fitlife.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("role")
    val role: String = "user" // user o trainer
)
