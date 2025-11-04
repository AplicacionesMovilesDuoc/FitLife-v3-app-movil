package com.mjperezm.v3_fitlife

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.mjperezm.v3_fitlife.ui.theme.V3fitlifeTheme
import com.mjperezm.v3_fitlife.ui.screens.HomeScreen
import com.mjperezm.v3_fitlife.ui.theme.V3fitlifeTheme
import androidx.compose.material3.Surface


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Aplica el tema de Material Design 3
            V3fitlifeTheme {
                // Surface proporciona el fondo de la app
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Muestra la pantalla principal
                    HomeScreen()
                }
            }
        }
    }
}
