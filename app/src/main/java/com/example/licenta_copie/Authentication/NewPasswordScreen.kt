package com.example.licenta_copie.Authentication

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.licenta_copie.Database.AppDatabase
import com.example.licenta_copie.Database.OfflineRepository.OfflineUserRepository
import com.example.licenta_copie.ModelView.SharedViewModel
import com.example.licenta_copie.R
import com.example.licenta_copie.ui.theme.Black
import com.example.licenta_copie.ui.theme.BlueGray
import com.example.licenta_copie.ui.theme.focusedTextFieldText
import com.example.licenta_copie.ui.theme.textFieldContainer
import com.example.licenta_copie.ui.theme.unfocusedTextFieldText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun NewPasswordScreen(onChangePassword: () -> Unit, sharedViewModel: SharedViewModel) {
    var newPassword by remember { mutableStateOf("") }
    val userRepository = OfflineUserRepository(
        userDao = AppDatabase.getDatabase(LocalContext.current).userDao()
    )
    val notification = remember { mutableStateOf("") }
    val context = LocalContext.current

    if (notification.value.isNotEmpty()) {
        Toast.makeText(context, notification.value, Toast.LENGTH_LONG).show()
        notification.value = ""
    }

    Surface {
        Column(modifier = Modifier.fillMaxSize()) {
            TopSection()
            Spacer(modifier = Modifier.height(36.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
            ) {
                androidx.compose.material3.TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = {
                        Text(text = "New Password", style = MaterialTheme.typography.labelMedium)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                        unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                    ),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(modifier = Modifier.height(15.dp))
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            sharedViewModel.forgotPassword_email.value?.let { email ->
                                sharedViewModel.forgotPassword_phoneNumber.value?.let { phoneNumber ->
                                    userRepository.updatePassword(email, phoneNumber, newPassword)
                                    withContext(Dispatchers.Main) {
                                        notification.value = "Password Updated Successfully"
                                        onChangePassword()
                                    }
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) BlueGray else Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(size = 4.dp)
                ) {
                    Text(
                        text = "Reset my Password",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                val uiColor = if (isSystemInDarkTheme()) Color.White else Black
            }
        }
    }
}
@Composable
private fun TopSection() {
    val uiColor = if (isSystemInDarkTheme()) Color.White else Black
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.5f),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        Row(
            modifier = Modifier.padding(top = 80.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.app_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = uiColor
                )
                Text(
                    text = stringResource(id = R.string.find_CS),
                    style = MaterialTheme.typography.titleMedium,
                    color = uiColor
                )
            }
        }
        Text(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(alignment = Alignment.BottomCenter),
            text = stringResource(id = R.string.forgotpassword),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}