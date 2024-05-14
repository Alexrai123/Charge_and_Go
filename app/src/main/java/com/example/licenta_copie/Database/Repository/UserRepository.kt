package com.example.licenta_copie.Database.Repository

import com.example.licenta_copie.Database.Entity.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getPasswordById(id: Int): Flow<String?>
    fun getPasswordByEmail(email: String): Flow<String?>
    suspend fun updatePassword(email: String, phoneNumber: String, newPassword: String)
    fun getAllUsersStream(): Flow<List<User>>
    fun getUserStream(id: Int): Flow<User?>
    fun getUserByEmailAndPassword(email: String, password: String): Flow<User?>
    fun getUserIdByEmailAndPassword(email: String, password: String): Flow<Int?>
    fun getUserByEmail(email: String): Flow<User?>
    fun getUserById(id: Int): Flow<User?>
    suspend fun deleteUserById(id: Int)
    suspend fun userExists(email: String, password: String): Boolean
    suspend fun emailAndPhoneNumberExists(email: String, phoneNumber: String): Boolean
    suspend fun insertUser(user: User)
    suspend fun deleteUser(user: User)
    suspend fun updateUser(user: User)
}