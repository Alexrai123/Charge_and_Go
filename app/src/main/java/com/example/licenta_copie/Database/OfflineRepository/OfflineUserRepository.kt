package com.example.licenta_copie.Database.OfflineRepository

import com.example.licenta_copie.Database.Dao.UserDao
import com.example.licenta_copie.Database.Entity.User
import com.example.licenta_copie.Database.Repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OfflineUserRepository(private val userDao: UserDao) : UserRepository {
    override fun getPasswordById(id: Int): Flow<String?> = userDao.getPasswordById(id)
    override fun getPasswordByEmail(email: String): Flow<String?> = userDao.getPasswordByEmail(email)
    override suspend fun updatePassword(email: String, phoneNumber: String, newPassword: String){
        withContext(Dispatchers.IO){
            userDao.updatePassword(email, phoneNumber, newPassword)
        }
    }
    override suspend fun updateEmail(newEmail: String, phoneNumber: String, password: String){
        withContext(Dispatchers.IO){
            userDao.updateEmail(newEmail, phoneNumber, password)
        }
    }
    override suspend fun updatePhoneNumber(email: String, newPhoneNumber: String, password: String){
        withContext(Dispatchers.IO){
            userDao.updatePhoneNumber(email, newPhoneNumber, password)
        }
    }
    override fun getAllUsersStream(): Flow<List<User>> = userDao.getAllUsers()
    override fun getUserStream(id: Int): Flow<User?> = userDao.getUserById(id)
    override fun getUserByEmailAndPassword(email: String, password: String): Flow<User?> = userDao.getUserByEmailAndPassword(email, password)
    override fun getUserIdByEmailAndPassword(email: String, password: String): Flow<Int?> = userDao.getUserIDByEmailAndPassword(email, password)
    override fun getUserByEmail(email: String): Flow<User?> = userDao.getUserByEmail(email)
    override fun getUserById(id: Int): Flow<User?> = userDao.getUserById(id)
    override suspend fun deleteUserById(id: Int) = userDao.deleteUserById(id)
    override suspend fun userExists(email: String, password: String): Boolean = userDao.userExists(email, password)
    override suspend fun emailAndPhoneNumberExists(email: String, phoneNumber: String): Boolean = userDao.emailAndPhoneNumberExists(email, phoneNumber)
    override suspend fun insertUser(user: User) = userDao.insert(user)
    override suspend fun deleteUser(user: User) = userDao.delete(user)
    override suspend fun updateUser(user: User) = userDao.update(user)
}