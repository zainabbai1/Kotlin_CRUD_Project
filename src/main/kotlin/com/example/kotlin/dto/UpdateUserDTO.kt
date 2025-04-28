package com.example.kotlin.dto

data class UpdateUserDTO(
    val name: String? = null,
    val email: String? = null,
    val age: Int? = null
)