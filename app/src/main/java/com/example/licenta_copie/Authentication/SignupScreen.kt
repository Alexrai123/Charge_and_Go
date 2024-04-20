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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.licenta_copie.Database.AppDatabase
import com.example.licenta_copie.Database.Entity.User
import com.example.licenta_copie.Database.OfflineRepository.OfflineUserRepository
import com.example.licenta_copie.R
import com.example.licenta_copie.ui.theme.Black
import com.example.licenta_copie.ui.theme.BlueGray
import com.example.licenta_copie.ui.theme.focusedTextFieldText
import com.example.licenta_copie.ui.theme.textFieldContainer
import com.example.licenta_copie.ui.theme.unfocusedTextFieldText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SignupScreen(onSign: () -> Unit) {
    var emailInput by remember { mutableStateOf("") }
    var phoneNumberInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    val userRepository = OfflineUserRepository(
        userDao = AppDatabase.getDatabase(LocalContext.current).userDao()
    )
    val notification = rememberSaveable{ mutableStateOf("") }
    if(notification.value.isNotEmpty()){
        Toast.makeText(LocalContext.current, notification.value, Toast.LENGTH_LONG).show()
        notification.value = " "
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
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = emailInput,
                    onValueChange = {emailInput = it},
                    label = {
                        Text(text = "Email", style = MaterialTheme.typography.labelMedium)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                        unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                    ),
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = phoneNumberInput,
                    onValueChange = {phoneNumberInput = it},
                    label = {
                        Text(text = "Phone Number", style = MaterialTheme.typography.labelMedium)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                        unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                    ),
                )
                Spacer(modifier = Modifier.height(15.dp))
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordInput,
                    onValueChange = {passwordInput = it},
                    label = {
                        Text(text = "Password", style = MaterialTheme.typography.labelMedium)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.textFieldContainer,
                        focusedLabelColor = MaterialTheme.colorScheme.focusedTextFieldText,
                        unfocusedLabelColor = MaterialTheme.colorScheme.unfocusedTextFieldText,
                    ),
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    onClick = {
                        val user = User(email = emailInput, phoneNumber = phoneNumberInput, password = passwordInput)
                        CoroutineScope(Dispatchers.Main).launch {
                            userRepository.insertUser(user)
                            onSign()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSystemInDarkTheme()) BlueGray else Black,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(size = 4.dp)
                ) {
                    Text(
                        text = "Submit",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                val uiColor = if (isSystemInDarkTheme()) Color.White else Black
                Box(modifier = Modifier
                    .fillMaxHeight(fraction = 0.8f)
                    .fillMaxWidth(),
                    contentAlignment = Alignment.BottomCenter
                ){
                }
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
            text = stringResource(id = R.string.createAccount),
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}
@Composable
@Preview
fun preview(){
    SignupScreen {

    }
}