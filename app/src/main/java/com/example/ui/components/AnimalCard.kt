package com.example.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.Animal

import androidx.compose.ui.layout.LayoutCoordinates

/**
 * Draggable Standalone 3D Animal Figure without any background box
 */
@Composable
fun AnimalCard(
    animal: Animal,
    cardSize: Dp,
    isMatched: Boolean,
    isBeingDragged: Boolean,
    onDragStart: (Animal, LayoutCoordinates, Offset) -> Unit,
    onDragDelta: (Offset) -> Unit,
    onDragEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isMatched) return // Hide bottom figure once matched

    var cardCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }

    Box(
        modifier = modifier
            .size(cardSize)
            .alpha(if (isBeingDragged) 0.2f else 1.0f)
            .onGloballyPositioned { coordinates ->
                cardCoordinates = coordinates
            }
            .pointerInput(animal.id) {
                detectDragGestures(
                    onDragStart = { offsetInCard ->
                        cardCoordinates?.let { coords ->
                            onDragStart(animal, coords, offsetInCard)
                        }
                    },
                    onDragEnd = {
                        onDragEnd()
                    },
                    onDragCancel = {
                        onDragEnd()
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        onDragDelta(dragAmount)
                    }
                )
            }
            .testTag("animal_card_${animal.id}"),
        contentAlignment = Alignment.Center
    ) {
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
                    isShadow = false,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "${animal.arabicName} ${animal.emoji}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color(0xFF1B263B)
            )
        }
    }
}

/**
 * Floating Standalone 3D Animal Figure Overlay during drag
 */
@Composable
fun FloatingAnimalCardOverlay(
    animal: Animal,
    cardSize: Dp,
    tiltAngle: Float = 0f,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(cardSize)
            .graphicsLayer {
                rotationZ = tiltAngle
                scaleX = 1.18f
                scaleY = 1.18f
                shadowElevation = 14f
            },
        contentAlignment = Alignment.Center
    ) {
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
                    isShadow = false,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "${animal.arabicName} ${animal.emoji}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold
                ),
                color = Color(0xFF1B263B)
            )
        }
    }
}
