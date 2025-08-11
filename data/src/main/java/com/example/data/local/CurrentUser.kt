package com.example.data.local

import com.example.domain.model.Profile


object CurrentUser {
    @Volatile
    var user: Profile? = null
}