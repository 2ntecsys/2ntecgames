package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import java.util.Random

data class ConfettiParticle(
    val startX: Float,
    val speedY: Float,
    val speedX: Float,
    val size: Float,
    val color: Color,
    val rotationSpeed: Float,
    val initialPhase: Float,
    val isStar: Boolean
)

@Composable
fun ConfettiView(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    particleCount: Int = 70
) {
    if (!isActive) return

    val particles = remember(particleCount) {
        val rand = Random()
        val colors = listOf(
            Color(0xFFFFD700), // Gold
            Color(0xFFFF5252),
            Color(0xFFFFD54F),
            Color(0xFF4FC3F7),
            Color(0xFF81C784),
            Color(0xFFFF80AB),
            Color(0xFFB388FF),
            Color(0xFFFF9800)
        )
        List(particleCount) { i ->
            ConfettiParticle(
                startX = rand.nextFloat(),
                speedY = 0.25f + rand.nextFloat() * 0.75f,
                speedX = (rand.nextFloat() - 0.5f) * 0.35f,
                size = 14f + rand.nextFloat() * 18f,
                color = colors[rand.nextInt(colors.size)],
                rotationSpeed = 2f + rand.nextFloat() * 8f,
                initialPhase = rand.nextFloat() * 3.14f * 2f,
                isStar = i % 3 == 0 // Every third particle is a sparkling star!
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "ConfettiTransition")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ConfettiProgress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        for (p in particles) {
            val yProgress = (progress * p.speedY + p.initialPhase) % 1.0f
            val currentY = yProgress * (h + 120f) - 60f
            val currentX = (p.startX + p.speedX * progress) * w

            if (p.isStar) {
                // Draw Sparkling Star 🌟
                val radius = p.size * 0.8f
                val path = Path().apply {
                    val angleStep = Math.PI / 5.0
                    for (i in 0 until 10) {
                        val r = if (i % 2 == 0) radius else radius * 0.45f
                        val angle = i * angleStep - Math.PI / 2.0
                        val x = currentX + (r * kotlin.math.cos(angle)).toFloat()
                        val y = currentY + (r * kotlin.math.sin(angle)).toFloat()
                        if (i == 0) moveTo(x, y) else lineTo(x, y)
                    }
                    close()
                }
                drawPath(path = path, color = p.color)
            } else {
                // Draw Confetti Paper Rectangle 🎊
                drawRect(
                    color = p.color,
                    topLeft = Offset(currentX, currentY),
                    size = Size(p.size, p.size * 0.6f)
                )
            }
        }
    }
}

