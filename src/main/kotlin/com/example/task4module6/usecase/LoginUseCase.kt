package com.example.task4module6.usecase

import com.example.task4module6.domain.repository.UserRepository

class LoginUseCase(private val userRepository: UserRepository) {
    operator fun invoke(login: String, password: String): Boolean {
        return userRepository.isValidCredentials(login, password)
    }
}
