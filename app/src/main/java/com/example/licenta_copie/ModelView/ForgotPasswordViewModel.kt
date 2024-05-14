package com.example.licenta_copie.ModelView

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.licenta_copie.Database.OfflineRepository.OfflineUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(private val userRepository: OfflineUserRepository) : ViewModel() {
    private val _notification = MutableStateFlow("")
    val notification: StateFlow<String> get() = _notification
    fun validateUser(email: String, phoneNumber: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = userRepository.emailAndPhoneNumberExists(email, phoneNumber)
            if (user == true) {
                onSuccess()
            } else {
                _notification.value = "Email or phone number is incorrect"
            }
        }
    }

    fun updatePassword(email: String, phoneNumber: String, userId: Int, newPassword: String) {
        viewModelScope.launch {
            userRepository.updatePassword(email, phoneNumber, newPassword)
            _notification.value = "Password updated successfully"
        }
    }
}