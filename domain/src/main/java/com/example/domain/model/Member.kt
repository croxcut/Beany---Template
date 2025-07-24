package com.example.domain.model

import androidx.annotation.DrawableRes

data class Member(
    val name: String,
    val position: String,
    val bio: String,
    @DrawableRes val imageRes: Int
)