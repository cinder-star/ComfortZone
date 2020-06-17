package com.sihan.comfortzone.repositories

interface BaseLogin {
    abstract fun validate(credential: String): Boolean
    abstract fun login(username: String, password: String)
}