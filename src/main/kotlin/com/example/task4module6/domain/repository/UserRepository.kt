package com.example.task4module6.domain.repository

interface UserRepository {
    fun isValidCredentials(login: String, password: String): Boolean
}
