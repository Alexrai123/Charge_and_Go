package com.example.licenta_copie.ModelView

import androidx.lifecycle.ViewModel
import com.example.licenta_copie.Database.Entity.User
import com.example.licenta_copie.Database.OfflineRepository.OfflineUserRepository
import kotlinx.coroutines.flow.Flow

class UserViewModel(val repository: OfflineUserRepository) : ViewModel() {
    val users: Flow<List<User>> = repository.getAllUsersStream()
    suspend fun insertUser(user: User) {
        repository.insertUser(user)
    }
    suspend fun deleteUser(user: User) {
        repository.deleteUser(user)
    }
    suspend fun updateUser(user: User) {
        repository.updateUser(user)
    }
}