package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Animal

import androidx.compose.ui.layout.LayoutCoordinates

@Composable
fun ShadowTarget(
    animal: Animal,
    isMatched: Boolean,
    cardSize: Dp,
    onPositioned: (Animal, LayoutCoordinates) -> Unit,
    modifier: Modifier = Modifier
) {
    val scaleState by animateFloatAsState(
        targetValue = if (isMatched) 1.08f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "TargetScale"
    )

    val glowAlpha by animateFloatAsState(
        targetValue = if (isMatched) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "GlowAlpha"
    )

    // Pure standalone 3D animal silhouette without any frame, border, or background card
    Box(
        modifier = modifier
            .size(cardSize)
            .scale(scaleState)
            .onGloballyPositioned { coordinates ->
                onPositioned(animal, coordinates)
            }
            .testTag("shadow_target_${animal.id}"),
        contentAlignment = Alignment.Center
    ) {
        // Celebratory soft aura ring upon successful match
        if (isMatched && glowAlpha > 0f) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension * 0.44f
                drawCircle(
                    color = Color(0xFFFFD54F).copy(alpha = glowAlpha * 0.35f),
                    radius = radius * 1.15f,
                    center = Offset(size.width / 2f, size.height * 0.45f)
                )
                drawCircle(
                    color = Color(0xFFFFB300).copy(alpha = glowAlpha * 0.5f),
                    radius = radius,
                    center = Offset(size.width / 2f, size.height * 0.45f),
                    style = Stroke(width = 3.dp.toPx())
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(2.dp)
        ) {
            Box(
                modifier = Modifier.size(cardSize * 0.78f),
                contentAlignment = Alignment.Center
            ) {
                AnimalVectorArt(
                    animal = animal,
                    isShadow = !isMatched,
                    shadowColor = Color(0xFF0F172A),
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            AnimatedVisibility(
                visible = isMatched,
                enter = fadeIn(tween(250)) + scaleIn(spring(dampingRatio = Spring.DampingRatioMediumBouncy))
            ) {
                Text(
                    text = "${animal.arabicName} ${animal.emoji}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = animal.secondaryColor
                )
            }

            if (!isMatched) {
                Text(
                    text = "؟",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = Color(0xFF455A64)
                )
            }
        }
    }
}
