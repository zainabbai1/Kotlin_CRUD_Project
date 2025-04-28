package com.example.kotlin.service

import com.example.kotlin.dto.CreateUserDTO
import com.example.kotlin.dto.UpdateUserDTO
import com.example.kotlin.dto.UserDTO
import jakarta.transaction.Transactional
import com.example.kotlin.model.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import com.example.kotlin.repository.UserRepository
import org.springframework.stereotype.Service

@Service
@Transactional
class UserService @Autowired constructor(
    private val userRepository: UserRepository
) {
    fun createUser(dto: CreateUserDTO): UserDTO {
        val entity = UserEntity(
            name = dto.name,
            email = dto.email,
            age = dto.age
        )
        val saved = userRepository.save(entity)
        return saved.toDTO()
    }

    fun getUserById(id: Long): UserDTO? =
        userRepository.findById(id).orElse(null)?.toDTO()

    fun getAllUsers(): List<UserDTO> =
        userRepository.findAll().map { it.toDTO() }

    fun replaceUser(id: Long, dto: CreateUserDTO): UserDTO? {
        if (!userRepository.existsById(id)) return null

        val updated = UserEntity(
            id = id,
            name = dto.name,
            email = dto.email,
            age = dto.age
        )
        return userRepository.save(updated).toDTO()
    }

    fun updateUser(id: Long, dto: UpdateUserDTO): UserDTO? {
        val existing = userRepository.findById(id).orElse(null) ?: return null
        val updated = UserEntity(
            id = existing.id,
            name = dto.name ?: existing.name,
            email = dto.email ?: existing.email,
            age = dto.age ?: existing.age
        )
        return userRepository.save(updated).toDTO()
    }

    fun deleteUser(id: Long): Boolean {
        val existing = userRepository.findById(id).orElse(null) ?: return false
        userRepository.delete(existing)
        return true
    }

    fun getUserByEmail(email: String): UserEntity? =
        userRepository.findByEmail(email)

    // Extension function to map entity to DTO
    private fun UserEntity.toDTO() = UserDTO(id, name, email, age)
}