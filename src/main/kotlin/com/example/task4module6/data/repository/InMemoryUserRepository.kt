package com.example.task4module6.data.repository

import com.example.task4module6.domain.repository.UserRepository

class InMemoryUserRepository : UserRepository {
    private val users = mapOf(
        "admin" to "admin"
    )

    override fun isValidCredentials(login: String, password: String): Boolean {
        return users[login] == password
    }
}
