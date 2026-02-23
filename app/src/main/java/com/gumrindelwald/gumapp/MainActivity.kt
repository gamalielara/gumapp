package com.gumrindelwald.gumapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.gumrindelwald.designsystem.GumAppTheme
import com.gumrindelwald.presentation.GumrunDashboardRoot


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            GumAppTheme {
                GumAppStartActivityContent()
            }
        }
    }
}

@Composable
fun GumAppStartActivityContent() {
    GumrunDashboardRoot()
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GumAppTheme {
        GumAppStartActivityContent()
    }
}