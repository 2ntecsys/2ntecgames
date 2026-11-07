package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.GameMode
import com.example.data.Level
import com.example.data.LevelRepository

@Composable
fun LevelSelectorScreen(
    unlockedLevelMax: Int,
    selectedMode: GameMode = GameMode.ZOO_ANIMALS,
    onSelectLevel: (Level) -> Unit,
    onBackToGame: () -> Unit,
    onGoToHome: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val levels = LevelRepository.getLevelsForMode(selectedMode)

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFE1F5FE)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = onBackToGame,
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .size(46.dp)
                        .testTag("btn_back_from_selector")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = Color(0xFF0277BD)
                    )
                }

                val titleText = if (selectedMode == GameMode.ZOO_ANIMALS) {
                    "خريطة حديقة الحيوان 🦁"
                } else {
                    "خريطة الأشكال والظلال 📐"
                }

                Text(
                    text = titleText,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    ),
                    color = Color(0xFF01579B)
                )

                IconButton(
                    onClick = { onGoToHome?.invoke() ?: onBackToGame() },
                    modifier = Modifier
                        .background(Color.White, CircleShape)
                        .size(46.dp)
                        .testTag("btn_home_from_selector")
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "القائمة الرئيسية",
                        tint = Color(0xFFE65100)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Grid of Levels
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(levels) { level ->
                    val isUnlocked = level.levelNumber <= unlockedLevelMax

                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUnlocked) Color.White else Color(0xFFECEFF1)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (isUnlocked) 8.dp else 2.dp
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clickable(enabled = isUnlocked) {
                                onSelectLevel(level)
                            }
                            .testTag("level_item_${level.levelNumber}")
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (isUnlocked) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = level.badgeIcon,
                                        fontSize = 32.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = level.titleArabic,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 17.sp
                                        ),
                                        color = Color(0xFF283593),
                                        textAlign = TextAlign.Center
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = level.subtitleArabic,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        color = Color(0xFF00838F),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            } else {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "مغلق",
                                        tint = Color(0xFF78909C),
                                        modifier = Modifier.size(36.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = level.titleArabic,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        ),
                                        color = Color(0xFF78909C)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
