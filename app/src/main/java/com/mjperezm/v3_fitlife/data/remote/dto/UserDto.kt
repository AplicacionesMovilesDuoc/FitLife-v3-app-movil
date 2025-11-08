package com.mjperezm.v3_fitlife.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id")
    val id: Int,

    @SerializedName("email")
    val email: String,

    @SerializedName("name")
    val name: String?,

    @SerializedName("role")
    val role: String?, // "user" o "trainer"

    @SerializedName("created_at")
    val createdAt: Long?,

    @SerializedName("peso")
    val peso: Double?,

    @SerializedName("altura")
    val altura: Double?,

    @SerializedName("objetivo")
    val objetivo: String?
)
