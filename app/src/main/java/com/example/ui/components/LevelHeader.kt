package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grid3x3
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.Level

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.graphics.Brush

@Composable
fun LevelHeader(
    currentLevel: Level,
    childName: String,
    userAvatarUri: String?,
    gender: String = "BOY",
    matchedCount: Int,
    totalCount: Int,
    soundEnabled: Boolean,
    onToggleSound: () -> Unit,
    onRestartLevel: () -> Unit,
    onOpenLevelSelector: () -> Unit,
    onOpenWelcomeScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isGirl = gender == "GIRL"
    val progressAnimated by animateFloatAsState(
        targetValue = if (totalCount > 0) matchedCount / totalCount.toFloat() else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "LevelProgress"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .shadow(4.dp, RoundedCornerShape(20.dp)),
        shape = RoundedCornerShape(20.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Buttons (Avatar / Home & Stage Selector)
            Row(verticalAlignment = Alignment.CenterVertically) {
                // User Avatar Button opening welcome screen
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .shadow(2.dp, CircleShape)
                        .background(if (isGirl) Color(0xFFF8BBD0) else Color(0xFFFFD54F), CircleShape)
                        .border(2.dp, if (isGirl) Color(0xFFE91E63) else Color(0xFF0288D1), CircleShape)
                        .clip(CircleShape)
                        .clickable(onClick = onOpenWelcomeScreen)
                        .testTag("btn_home_welcome"),
                    contentAlignment = Alignment.Center
                ) {
                    if (!userAvatarUri.isNullOrEmpty()) {
                        AsyncImage(
                            model = userAvatarUri,
                            contentDescription = "صورة الملف الشخصي",
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
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                IconButton(
                    onClick = onOpenWelcomeScreen,
                    modifier = Modifier
                        .background(Color(0xFFFFF3E0), CircleShape)
                        .size(38.dp)
                        .testTag("btn_main_home")
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "القائمة الرئيسية",
                        tint = Color(0xFFE65100)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                IconButton(
                    onClick = onOpenLevelSelector,
                    modifier = Modifier
                        .background(Color(0xFFE0F7FA), CircleShape)
                        .size(38.dp)
                        .testTag("btn_level_map")
                ) {
                    Icon(
                        imageVector = Icons.Default.Grid3x3,
                        contentDescription = "قائمة المراحل",
                        tint = Color(0xFF0288D1)
                    )
                }
            }

            // Level Info & Progress Indicator
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = currentLevel.badgeIcon,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${currentLevel.titleArabic} (${currentLevel.levelNumber}/10)",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        ),
                        color = Color(0xFF283593)
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Smooth Animated Progress Bar
                Box(
                    modifier = Modifier
                        .width(115.dp)
                        .height(7.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFECEFF1))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progressAnimated.coerceIn(0f, 1f))
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF43A047), Color(0xFF81C784))
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Child greeting & score
                Text(
                    text = if (childName.isNotEmpty()) "أهلاً $childName 👋 ($matchedCount/$totalCount 🌟)" else "تم مطابقة $matchedCount من $totalCount 🌟",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    color = Color(0xFF2E7D32)
                )
            }

            // Action Buttons (Sound & Reset)
            Row {
                IconButton(
                    onClick = onToggleSound,
                    modifier = Modifier
                        .background(if (soundEnabled) Color(0xFFFFF3E0) else Color(0xFFFFEBEE), CircleShape)
                        .size(38.dp)
                        .testTag("btn_toggle_sound")
                ) {
                    Icon(
                        imageVector = if (soundEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "الصوت",
                        tint = if (soundEnabled) Color(0xFFE65100) else Color(0xFFC62828)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                IconButton(
                    onClick = onRestartLevel,
                    modifier = Modifier
                        .background(Color(0xFFF3E5F5), CircleShape)
                        .size(38.dp)
                        .testTag("btn_restart_level")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "إعادة المرحلة",
                        tint = Color(0xFF6A1B9A)
                    )
                }
            }
        }
    }
}
