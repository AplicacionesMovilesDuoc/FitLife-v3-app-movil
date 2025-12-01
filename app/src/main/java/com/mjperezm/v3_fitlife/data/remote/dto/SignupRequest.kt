package com.mjperezm.v3_fitlife.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SignupRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("role")
    val role: String? = "USUARIO", // "USUARIO" o "ENTRENADOR"

    @SerializedName("nombre")
    val nombre: String? = null,

    @SerializedName("telefono")
    val telefono: String? = null
)
