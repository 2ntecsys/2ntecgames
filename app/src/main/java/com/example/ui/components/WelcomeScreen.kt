package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R

@Composable
fun WelcomeScreen(
    childName: String,
    userAvatarUri: String?,
    gender: String = "BOY",
    currentLevelNumber: Int = 1,
    onStartGame: () -> Unit,
    onChangeName: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isGirl = gender == "GIRL"
    val infiniteTransition = rememberInfiniteTransition(label = "AvatarPulse")
    val avatarScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AvatarPulseScale"
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFE1F5FE)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = if (isGirl) {
                            listOf(
                                Color(0xFFC2185B),
                                Color(0xFFE91E63),
                                Color(0xFFF8BBD0)
                            )
                        } else {
                            listOf(
                                Color(0xFF0288D1),
                                Color(0xFF29B6F6),
                                Color(0xFFB3E5FC)
                            )
                        }
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Raining colorful paper confetti (تمطر الشاشة قصاصات أوراق ملونة)
            ConfettiView(
                isActive = true,
                particleCount = 120
            )

            Card(
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .padding(16.dp)
                    .testTag("welcome_screen_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Child Image in Glowing Circular Frame
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .scale(avatarScale)
                            .background(
                                Brush.radialGradient(
                                    colors = if (isGirl) {
                                        listOf(Color(0xFFF48FB1), Color(0xFFE91E63))
                                    } else {
                                        listOf(Color(0xFFFFD54F), Color(0xFFFF9800))
                                    }
                                ),
                                CircleShape
                            )
                            .border(5.dp, Color.White, CircleShape)
                            .padding(6.dp)
                            .clip(CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!userAvatarUri.isNullOrEmpty()) {
                            AsyncImage(
                                model = userAvatarUri,
                                contentDescription = "صورة الطفل",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(if (isGirl) Color(0xFFFCE4EC) else Color(0xFFE1F5FE)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (isGirl) "👧" else "👦",
                                    fontSize = 70.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Greeting Text
                    Text(
                        text = if (isGirl) "أهلاً بكِ يا بطلة" else "أهلاً بك يا بطل",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        ),
                        color = if (isGirl) Color(0xFFAD1457) else Color(0xFFE65100),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Large Child Name
                    val defaultTitle = if (isGirl) "بطلة الزو" else "بطل الزو"
                    Text(
                        text = childName.ifEmpty { defaultTitle },
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 36.sp
                        ),
                        color = if (isGirl) Color(0xFF880E4F) else Color(0xFF283593),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag("text_child_name_large")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (isGirl) {
                            "جاهزة لمغامرة مطابقة ظلال الحيوانات في الحديقة؟ 🦁🐘🦒"
                        } else {
                            "جاهز لمغامرة مطابقة ظلال الحيوانات في الحديقة؟ 🦁🐘🦒"
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF00838F),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(28.dp))

                    // Start Game Button
                    Button(
                        onClick = onStartGame,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("btn_start_playing_now")
                    ) {
                        Text(
                            text = if (currentLevelNumber > 1) "متابعة اللعب (المرحلة $currentLevelNumber) 🚀" else "ابدأ اللعب الآن 🚀",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Change Name Button
                    OutlinedButton(
                        onClick = onChangeName,
                        shape = RoundedCornerShape(18.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_change_child_name")
                    ) {
                        Text(
                            text = "تعديل الاسم والصورة ✏️",
                            color = Color(0xFF0277BD),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
