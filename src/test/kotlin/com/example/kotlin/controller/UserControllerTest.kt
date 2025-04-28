package com.example.kotlin.controller

import com.example.kotlin.dto.CreateUserDTO
import com.example.kotlin.dto.UpdateUserDTO
import com.example.kotlin.dto.UserDTO
import com.example.kotlin.service.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

@WebMvcTest(UserController::class)
class UserControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc   //Injects the MockMvc object to simulate HTTP requests.

    @MockBean
    private lateinit var userService: UserService  //Creates a mock instance of UserService to be used in tests.

    private val objectMapper = jacksonObjectMapper() // Initializes an ObjectMapper for JSON serialization and deserialization.

    @Test
    fun `test createUser`() {
        val dto = CreateUserDTO(name = "John Doe", email = "john.doe@example.com", age = 30)
        val userDTO = UserDTO(id = 1L, name = dto.name, email = dto.email, age = dto.age)
        whenever(userService.createUser(dto)).thenReturn(userDTO)

        val result = mockMvc.perform(
            post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))  //dto as JSON content
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value(dto.name))
            .andExpect(jsonPath("$.email").value(dto.email))
            .andExpect(jsonPath("$.age").value(dto.age))
            .andReturn()

        val response: UserDTO = objectMapper.readValue(result.response.contentAsString)
        assertEquals(dto.name, response.name) // Asserts that the name in the response matches the expected name.
        assertEquals(dto.email, response.email)
        assertEquals(dto.age, response.age)
    }

    @Test
    fun `test getUserById`() {
        val userDTO = UserDTO(id = 1L, name = "John Doe", email = "john.doe@example.com", age = 30)
        whenever(userService.getUserById(1L)).thenReturn(userDTO)

        mockMvc.perform(get("/users/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value(userDTO.name))
            .andExpect(jsonPath("$.email").value(userDTO.email))
            .andExpect(jsonPath("$.age").value(userDTO.age))
    }

    @Test
    fun `test updateUser`() {
        val updateDTO = UpdateUserDTO(name = "Jane Doe", email = "jane.doe@example.com", age = 25)
        val userDTO = UserDTO(id = 1L, name = updateDTO.name!!, email = updateDTO.email!!, age = updateDTO.age!!)
        whenever(userService.updateUser(1L, updateDTO)).thenReturn(userDTO)

        mockMvc.perform(
            patch("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value(updateDTO.name))
            .andExpect(jsonPath("$.email").value(updateDTO.email))
            .andExpect(jsonPath("$.age").value(updateDTO.age))
    }

    @Test
    fun `test deleteUser`() {
        whenever(userService.deleteUser(1L)).thenReturn(true)

        mockMvc.perform(delete("/users/1"))
            .andExpect(status().isNoContent)
    }
}
