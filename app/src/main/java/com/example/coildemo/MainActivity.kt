package com.example.coildemo

import MainScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import coil.Coil
import coil.Coil.imageLoader
import coil.imageLoader
import com.example.coildemo.ui.theme.CoilSizeUrlParametersDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoilSizeUrlParametersDemoTheme {
                MainScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
