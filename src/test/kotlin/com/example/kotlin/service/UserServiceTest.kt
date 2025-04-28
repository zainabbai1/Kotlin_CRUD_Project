package com.example.kotlin.service

import com.example.kotlin.dto.CreateUserDTO
import com.example.kotlin.dto.UpdateUserDTO
import com.example.kotlin.model.UserEntity
import com.example.kotlin.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import java.util.*
import java.util.concurrent.TimeUnit

@ExtendWith(MockitoExtension::class)  //This annotation tells JUnit to use Mockito's extension to handle mock objects.
class UserServiceTest {

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userService: UserService  //This annotation tells Mockito to inject the mock objects into userService.

    @Test
    //@Timeout(value = 1, unit = TimeUnit.MILLISECONDS)
    fun `test createUser`() {
        val dto = CreateUserDTO(name = "John Doe", email = "john.doe@example.com", age = 30)
        val entity = UserEntity(id = 1L, name = dto.name, email = dto.email, age = dto.age)
        whenever(userRepository.save(any<UserEntity>())).thenReturn(entity)

        val result = userService.createUser(dto)
        assertNotNull(result)
        assertEquals(dto.name, result.name)
        assertEquals(dto.email, result.email)
        assertEquals(dto.age, result.age)
    }

    @Test
       fun `test getUserById`() {
        val entity = UserEntity(id = 1L, name = "John Doe", email = "john.doe@example.com", age = 30)
        whenever(userRepository.findById(1L)).thenReturn(Optional.of(entity))

        val result = userService.getUserById(1L)
        assertNotNull(result)
        assertEquals(entity.name, result?.name)
        assertEquals(entity.email, result?.email)
        assertEquals(entity.age, result?.age)
    }

    @Test
    fun `test updateUser`() {
        val existingEntity = UserEntity(id = 1L, name = "John Doe", email = "john.doe@example.com", age = 30)
        val updateDTO = UpdateUserDTO(name = "Jane Doe", email = "jane.doe@example.com", age = 25)
        val updatedEntity = UserEntity(id = 1L, name = updateDTO.name!!, email = updateDTO.email!!, age = updateDTO.age!!)
        whenever(userRepository.findById(1L)).thenReturn(Optional.of(existingEntity))
        whenever(userRepository.save(any<UserEntity>())).thenReturn(updatedEntity)

        val result = userService.updateUser(1L, updateDTO)
        assertNotNull(result)
        assertEquals(updateDTO.name, result?.name)
        assertEquals(updateDTO.email, result?.email)
        assertEquals(updateDTO.age, result?.age)
    }

    @Test
    fun `test deleteUser`() {
        val entity = UserEntity(id = 1L, name = "John Doe", email = "john.doe@example.com", age = 30)
        whenever(userRepository.findById(1L)).thenReturn(Optional.of(entity))

        val result = userService.deleteUser(1L)
        assertTrue(result)
    }
}
