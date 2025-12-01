package com.mjperezm.v3_fitlife.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("_id")
    val id: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("role")
    val role: String, // "USUARIO", "ENTRENADOR", "ADMIN"

    @SerializedName("isActive")
    val isActive: Boolean? = true,

    @SerializedName("emailVerified")
    val emailVerified: Boolean? = false,

    // Campos del perfil (cuando se obtiene con populate)
    @SerializedName("nombre")
    val name: String? = null,

    @SerializedName("telefono")
    val telefono: String? = null,

    @SerializedName("edad")
    val edad: Int? = null,

    @SerializedName("peso")
    val peso: Double? = null,

    @SerializedName("altura")
    val altura: Double? = null,

    @SerializedName("objetivo")
    val objetivo: String? = null
)
