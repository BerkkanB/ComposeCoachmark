package com.berkkanb.coachmark

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize


fun Modifier.coachMark(setCoachmarkCallback: (Offset, IntSize) -> Unit): Modifier {
    return this.onGloballyPositioned {
        val position = it.positionInRoot()
        val size = it.size
        setCoachmarkCallback.invoke(position, size)
    }
}

@Composable
fun CoachMarkProvider(
    coachMarkState: MutableList<CoachMarkState>,
    isVisible: Boolean = false,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {

    var visibility by remember {
        mutableStateOf(isVisible)
    }

    Surface() {
        Box() {
            content()
            if (visibility && coachMarkState.isEmpty().not()) {
                var index by remember {
                    mutableStateOf(0)
                }
                with(coachMarkState[index]) {
                    if (coachmarkPosition != Offset.Unspecified) {

                        var descriptionHeight by remember {
                            mutableStateOf(0)
                        }
                        val iconWidth = 64.dp
                        val iconMid = (coachmarkSize.width - iconWidth.toPx()) / 2
                        val iconPositionX = (coachmarkPosition.x + iconMid).toDp()
                        var iconPositionY = (coachmarkPosition.y + coachmarkSize.height + 50f).toDp()
                        var textPositionY = (coachmarkPosition.y + coachmarkSize.height + 250f).toDp()

                        if (coachmarkAlignment == CoachmarkAlignment.TOP) {
                            iconPositionY = (coachmarkPosition.y - iconWidth.toPx() - 50f).toDp()
                            textPositionY =
                                (coachmarkPosition.y - iconWidth.toPx() - descriptionHeight - 100f).toDp()
                        }

                        Canvas(modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                if (index < coachMarkState.size -1){
                                    index = index.plus(1)
                                }
                                else {
                                    visibility = false
                                }
                            }
                            .graphicsLayer {
                                alpha = 0.99f
                            }) {
                            drawRect(
                                color = Color.Black.copy(0.5f)
                            )
                            drawRoundRect(
                                color = Color.Transparent,
                                topLeft = coachmarkPosition,
                                size = coachmarkSize.toSize(),
                                blendMode = BlendMode.Clear,
                                cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx())
                            )
                        }
                        Image(
                            modifier = Modifier
                                .absoluteOffset(iconPositionX, iconPositionY)
                                .size(iconWidth)
                                .rotate(if (coachmarkAlignment == CoachmarkAlignment.BOTTOM) 180f else 0f),
                            painter = painterResource(id = R.drawable.ic_forward),
                            contentDescription = ""
                        )
                        Text(
                            text = description,
                            modifier = Modifier
                                .fillMaxWidth()
                                .absoluteOffset(0.dp, textPositionY)
                                .onGloballyPositioned {
                                    descriptionHeight = it.size.height
                                },
                            fontSize = 18.sp,
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                        Row(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 20.dp, end = 30.dp)
                                .clickable {
                                    visibility = false
                                    onDismiss.invoke()
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Filled.Close,
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .size(18.dp),
                                contentDescription = ""
                            )
                            Text(
                                text = "CLOSE",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

data class CoachMarkState(
    val coachmarkPosition: Offset = Offset.Unspecified,
    val coachmarkSize: IntSize = IntSize.Zero,
    val description: String = "",
    val cornerRadius: Dp = 0.dp,
    val coachmarkAlignment: CoachmarkAlignment = CoachmarkAlignment.BOTTOM
)

enum class CoachmarkAlignment {
    TOP,
    BOTTOM
}

@Composable
private fun Float.toDp(): Dp {
    return with(LocalDensity.current) {
        this@toDp.toDp()
    }
}

@Composable
private fun Dp.toPx(): Float {
    return with(LocalDensity.current) {
        this@toPx.toPx()
    }
}