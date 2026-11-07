package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VictoryScreen(
    childName: String = "",
    isGirl: Boolean = false,
    onPlayAgain: () -> Unit,
    onOpenLevelSelector: () -> Unit,
    onGoToHome: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "TrophyPulse")
    val trophyScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TrophyScale"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (isGirl) {
                        listOf(
                            Color(0xFF880E4F),
                            Color(0xFFC2185B),
                            Color(0xFFE91E63)
                        )
                    } else {
                        listOf(
                            Color(0xFF0D47A1),
                            Color(0xFF1976D2),
                            Color(0xFF42A5F5)
                        )
                    }
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Continuous Celebration Confetti
        ConfettiView(isActive = true, particleCount = 100)

        Card(
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp)
                .testTag("grand_victory_screen")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val title = if (isGirl) "🏆 بطلة مطابقة الظلال! 🏆" else "🏆 بطل مطابقة الظلال! 🏆"
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 24.sp
                    ),
                    color = if (isGirl) Color(0xFFC2185B) else Color(0xFFE65100),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                val subText = if (isGirl) {
                    val namePart = if (childName.isNotEmpty()) " يا $childName" else ""
                    "مبارك لكِ$namePart! لقد أكملتِ جميع الـ 10 مراحل بنجاح باهر!"
                } else {
                    val namePart = if (childName.isNotEmpty()) " يا $childName" else ""
                    "مبارك لك$namePart! لقد أكملت جميع الـ 10 مراحل بنجاح باهر!"
                }
                Text(
                    text = subText,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isGirl) Color(0xFFAD1457) else Color(0xFF1565C0),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 3D Animated Golden Trophy Cup Canvas Drawing
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .scale(trophyScale),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val w = size.width
                        val h = size.height
                        val cx = w / 2f

                        // Pedestal Base
                        drawRoundRect(
                            color = Color(0xFF5D4037),
                            topLeft = Offset(cx - w * 0.35f, h * 0.8f),
                            size = Size(w * 0.7f, h * 0.15f),
                            cornerRadius = CornerRadius(12f, 12f)
                        )
                        drawRoundRect(
                            color = Color(0xFF8D6E63),
                            topLeft = Offset(cx - w * 0.28f, h * 0.72f),
                            size = Size(w * 0.56f, h * 0.09f),
                            cornerRadius = CornerRadius(8f, 8f)
                        )

                        // Stem
                        drawRect(
                            color = Color(0xFFFFB300),
                            topLeft = Offset(cx - w * 0.08f, h * 0.55f),
                            size = Size(w * 0.16f, h * 0.18f)
                        )

                        // Handles (Left & Right)
                        drawCircle(
                            color = Color(0xFFFFC107),
                            radius = w * 0.22f,
                            center = Offset(cx - w * 0.25f, h * 0.32f),
                            style = Stroke(width = 16f)
                        )
                        drawCircle(
                            color = Color(0xFFFFC107),
                            radius = w * 0.22f,
                            center = Offset(cx + w * 0.25f, h * 0.32f),
                            style = Stroke(width = 16f)
                        )

                        // Cup Bowl
                        val cupPath = Path().apply {
                            moveTo(cx - w * 0.32f, h * 0.12f)
                            lineTo(cx + w * 0.32f, h * 0.12f)
                            cubicTo(
                                cx + w * 0.3f, h * 0.5f,
                                cx + w * 0.15f, h * 0.58f,
                                cx, h * 0.58f
                            )
                            cubicTo(
                                cx - w * 0.15f, h * 0.58f,
                                cx - w * 0.3f, h * 0.5f,
                                cx - w * 0.32f, h * 0.12f
                            )
                            close()
                        }
                        drawPath(
                            path = cupPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFFFD54F),
                                    Color(0xFFFFB300),
                                    Color(0xFFFF8F00)
                                )
                            )
                        )

                        // Star Emblem on Trophy
                        val starPath = Path().apply {
                            val rOuter = w * 0.09f
                            val rInner = w * 0.04f
                            val starCy = h * 0.32f
                            for (i in 0 until 10) {
                                val angle = i * Math.PI / 5 - Math.PI / 2
                                val r = if (i % 2 == 0) rOuter else rInner
                                val x = cx + (r * Math.cos(angle)).toFloat()
                                val y = starCy + (r * Math.sin(angle)).toFloat()
                                if (i == 0) moveTo(x, y) else lineTo(x, y)
                            }
                            close()
                        }
                        drawPath(starPath, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stars Summary Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFFFF8E1),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⭐⭐⭐ 30 / 30 نجمة الذهبية! ⭐⭐⭐", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFF57F17))
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenLevelSelector,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0288D1)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("btn_victory_level_selector")
                        ) {
                            Text(
                                text = "خريطة المراحل 🗺️",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onPlayAgain,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F00)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .testTag("btn_play_again")
                        ) {
                            Text(
                                text = "اللعب مجددًا 🔄",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    if (onGoToHome != null) {
                        Button(
                            onClick = onGoToHome,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF43A047)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("btn_victory_home")
                        ) {
                            Text(
                                text = "العودة للقائمة الرئيسية 🏠",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }
    }
}
