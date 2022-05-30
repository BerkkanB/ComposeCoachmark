package com.berkkanb.composecoachmark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.berkkanb.coachmark.CoachMarkProvider
import com.berkkanb.coachmark.CoachMarkState
import com.berkkanb.coachmark.CoachmarkAlignment
import com.berkkanb.coachmark.coachMark
import com.berkkanb.composecoachmark.ui.theme.ComposeCoachmarkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeCoachmarkTheme {
                var coachmarkOffset by remember {
                    mutableStateOf(Offset.Unspecified)
                }
                var coachmarkSize by remember {
                    mutableStateOf(IntSize.Zero)
                }
                val description =
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla ullamcorper et nisi vel dapibus."
                CoachMarkProvider(
                    isVisible = true,
                    onDismiss = {},
                    coachMarkState = CoachMarkState(
                        cornerRadius = 4.dp,
                        coachmarkSize = coachmarkSize,
                        coachmarkPosition = coachmarkOffset,
                        description = description,
                        coachmarkAlignment = CoachmarkAlignment.BOTTOM
                    )
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colors.background
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Greeting("Android", setCoachmarkCallback = { offset, size ->
                                coachmarkOffset = offset
                                coachmarkSize = size
                            })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, setCoachmarkCallback: (offset: Offset, size: IntSize) -> Unit) {
    Button(onClick = { /*TODO*/ }, modifier = Modifier.coachMark(setCoachmarkCallback)) {
        Text(
            text = "Hello $name!"
        )
    }
}

