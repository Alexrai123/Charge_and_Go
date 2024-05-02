package com.example.licenta_copie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.licenta_copie.ModelView.SharedViewModel
import com.example.licenta_copie.navigation.RootNavigationGraph
import com.example.licenta_copie.ui.theme.ComposeLoginScreenInitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeLoginScreenInitTheme {
                RootNavigationGraph(navController = rememberNavController(), SharedViewModel())
            }
        }
    }
}