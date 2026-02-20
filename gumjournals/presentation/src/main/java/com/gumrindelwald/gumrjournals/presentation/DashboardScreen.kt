package com.gumrindelwald.gumjournals.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gumrindelwald.designsystem.AppFont
import com.gumrindelwald.designsystem.GumAppTheme
import com.gumrindelwald.gumapp.core.presentation.designsystem.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GumjournalsDashboardScreen() {
    val interactionSource = remember { MutableInteractionSource() }
    var showDialog by remember { mutableStateOf(false) }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer),
        ) {
            TopBar(padding = padding)
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(10.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                ) {
                    Text(
                        "How was your day?",
                        fontFamily = AppFont,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(vertical = 10.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.emoji_weary),
                            contentDescription = null,
                            modifier = Modifier
                                .width(40.dp)
                                .aspectRatio(1f)
                                .clickable(
                                    interactionSource,
                                    indication = null,
                                    onClick = { showDialog = true })
                        )

                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.emoji_sorrow),
                            contentDescription = null,
                            modifier = Modifier
                                .width(40.dp)
                                .aspectRatio(1f)
                        )
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.emoji_flat),
                            contentDescription = null,
                            modifier = Modifier
                                .width(40.dp)
                                .aspectRatio(1f)
                        )
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.emoji_relieved),
                            contentDescription = null,
                            modifier = Modifier
                                .width(40.dp)
                                .aspectRatio(1f)
                        )
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.emoji_estatic_hugging),
                            contentDescription = null,
                            modifier = Modifier
                                .width(40.dp)
                                .aspectRatio(1f)
                        )
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.emoji_happy),
                            contentDescription = null,
                            modifier = Modifier
                                .width(40.dp)
                                .aspectRatio(1f)
                        )
                    }

                    if (showDialog) {
                        BasicAlertDialog(
                            onDismissRequest = { showDialog = false },
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .aspectRatio(1f)
                                    .clip(
                                        shape = RoundedCornerShape(10.dp),
                                    )
                                    .background(MaterialTheme.colorScheme.tertiaryContainer)
                                    .padding(20.dp)

                            ) {
                                Text("Hello World")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    padding: PaddingValues
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = padding.calculateTopPadding() + 10.dp, start = 10.dp, end = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            border = BorderStroke(0.dp, Color.Transparent),
            onClick = {},
            modifier = Modifier
                .background(Color.Transparent)
                .width(40.dp)
                .width(40.dp)
                .aspectRatio(1f),
            contentPadding = PaddingValues(5.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(R.drawable.back_arrow),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                    contentDescription = null,

                    )
            }
        }
        Text("Wednesday, January 1 2026", fontSize = 20.sp)
        Spacer(modifier = Modifier.width(40.dp))
    }
}

@Preview
@Composable
fun PreviewGumjournalsDashboardScreen() {
    GumAppTheme {
        GumjournalsDashboardScreen()
    }
}


