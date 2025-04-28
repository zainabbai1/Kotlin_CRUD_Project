package com.example.kotlin.controller

import com.example.kotlin.dto.CreateUserDTO
import com.example.kotlin.dto.UpdateUserDTO
import com.example.kotlin.dto.UserDTO
import com.example.kotlin.model.UserEntity
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import com.example.kotlin.service.UserService
import org.springframework.http.HttpStatus

@RestController
@RequestMapping("/users")
class UserController @Autowired constructor(
    private val userService: UserService
) {

    @PostMapping
    fun createUser(@RequestBody dto: CreateUserDTO): ResponseEntity<UserDTO> {
        val created = userService.createUser(dto)
        return ResponseEntity.ok(created)
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable id: Long): ResponseEntity<UserDTO> {
        val user = userService.getUserById(id)
        return if (user != null) ResponseEntity.ok(user)
        else ResponseEntity.notFound().build()
    }

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<UserDTO>> {
        val users = userService.getAllUsers()
        return ResponseEntity.ok(users)
    }

    @PutMapping("/{id}")
    fun putUser(
        @PathVariable id: Long,
        @RequestBody dto: CreateUserDTO
    ): ResponseEntity<UserDTO> // same as create because PUT = full update
    {
        val updated = userService.replaceUser(id, dto)
        return if (updated != null) ResponseEntity.ok(updated)
        else ResponseEntity.notFound().build()
    }

    @PatchMapping("/{id}")
    fun patchUser(@PathVariable id: Long, @RequestBody dto: UpdateUserDTO): ResponseEntity<UserDTO> {
        val updated = userService.updateUser(id, dto)
        return if (updated != null) ResponseEntity.ok(updated)
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable id: Long): ResponseEntity<Void> {
        return if (userService.deleteUser(id)) ResponseEntity.noContent().build()
        else ResponseEntity.notFound().build()
    }


    @GetMapping("/email")
    fun getUserByEmail(@RequestParam email: String): ResponseEntity<UserEntity> {
        val user = userService.getUserByEmail(email)
        return if (user != null) {
            ResponseEntity.ok(user)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
    }

}