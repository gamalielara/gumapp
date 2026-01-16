package com.gumrindelwald.gumapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gumrindelwald.designsystem.AppFont
import com.gumrindelwald.designsystem.GumAppTheme
import com.gumrindelwald.gumapp.core.presentation.designsystem.R

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

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            "This is GumApp!",
            fontFamily = AppFont,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {

            Image(
                imageVector = ImageVector.vectorResource(R.drawable.emoji_weary),
                contentDescription = null,
                modifier = Modifier
                    .width(60.dp)
                    .aspectRatio(1f)
                    .clickable(true, onClick = {})


            )

            Image(
                imageVector = ImageVector.vectorResource(R.drawable.emoji_sorrow),
                contentDescription = null,
                modifier = Modifier
                    .width(60.dp)
                    .aspectRatio(1f)


            )
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.emoji_flat),
                contentDescription = null,
                modifier = Modifier
                    .width(60.dp)
                    .aspectRatio(1f)


            )
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.emoji_relieved),
                contentDescription = null,
                modifier = Modifier
                    .width(60.dp)
                    .aspectRatio(1f)


            )
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.emoji_estatic_hugging),
                contentDescription = null,
                modifier = Modifier
                    .width(60.dp)
                    .aspectRatio(1f)


            )
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.emoji_happy),
                contentDescription = null,
                modifier = Modifier
                    .width(60.dp)
                    .aspectRatio(1f)


            )


        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GumAppTheme {
        GumAppStartActivityContent()
    }
}