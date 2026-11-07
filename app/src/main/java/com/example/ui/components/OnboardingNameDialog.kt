package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.R

@Composable
fun OnboardingNameDialog(
    initialName: String,
    initialAvatarUri: String? = null,
    initialGender: String = "BOY",
    onSaveName: (name: String, avatarUri: String?, gender: String) -> Unit,
    onDismiss: () -> Unit
) {
    var nameInput by remember { mutableStateOf(initialName) }
    var selectedAvatarUri by remember { mutableStateOf(initialAvatarUri) }
    var selectedGender by remember { mutableStateOf(initialGender) }
    val isGirl = selectedGender == "GIRL"

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedAvatarUri = uri.toString()
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("dialog_name_input")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile picture picker circle
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            Brush.radialGradient(
                                colors = if (isGirl) {
                                    listOf(Color(0xFFF8BBD0), Color(0xFFEC407A))
                                } else {
                                    listOf(Color(0xFFFFD54F), Color(0xFFFF9800))
                                }
                            ),
                            CircleShape
                        )
                        .border(4.dp, Color.White, CircleShape)
                        .clip(CircleShape)
                        .clickable {
                            photoPickerLauncher.launch(
                                androidx.activity.result.PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                    contentAlignment = Alignment.Center
                ) {
                    if (!selectedAvatarUri.isNullOrEmpty()) {
                        AsyncImage(
                            model = selectedAvatarUri,
                            contentDescription = "صورة الطفل الشخصية",
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
                                fontSize = 46.sp
                            )
                        }
                    }

                    // Camera overlay icon badge
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(4.dp)
                            .size(28.dp)
                            .background(if (isGirl) Color(0xFFE91E63) else Color(0xFF0288D1), CircleShape)
                            .border(2.dp, Color.White, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "تغيير الصورة",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "اضغط على الصورة لاختيار صورتك الشخصية 📷",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    ),
                    color = if (isGirl) Color(0xFFE91E63) else Color(0xFF0288D1),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Title adapted for boy / girl
                Text(
                    text = if (isGirl) "أهلاً بكِ يا بطلة! 🌸" else "أهلاً بك يا بطل! 🚀",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp
                    ),
                    color = if (isGirl) Color(0xFFC2185B) else Color(0xFF0277BD),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Gender Selection Buttons (ولد / بنت)
                Text(
                    text = "اختر نوع المستخدم للناطق الصوتي:",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    ),
                    color = Color(0xFF455A64),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Boy option card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { selectedGender = "BOY" }
                            .testTag("select_gender_boy"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (!isGirl) Color(0xFFE1F5FE) else Color(0xFFF8FAFC)
                        ),
                        border = BorderStroke(
                            width = if (!isGirl) 2.5.dp else 1.dp,
                            color = if (!isGirl) Color(0xFF0288D1) else Color(0xFFCFD8DC)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (!isGirl) 4.dp else 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "👦", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "ولد",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (!isGirl) Color(0xFF01579B) else Color(0xFF64748B)
                                )
                                Text(
                                    text = "بطل",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (!isGirl) Color(0xFF0288D1) else Color(0xFF94A3B8)
                                )
                            }
                        }
                    }

                    // Girl option card
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { selectedGender = "GIRL" }
                            .testTag("select_gender_girl"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isGirl) Color(0xFFFCE4EC) else Color(0xFFF8FAFC)
                        ),
                        border = BorderStroke(
                            width = if (isGirl) 2.5.dp else 1.dp,
                            color = if (isGirl) Color(0xFFE91E63) else Color(0xFFCFD8DC)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = if (isGirl) 4.dp else 0.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(text = "👧", fontSize = 24.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "بنت",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isGirl) Color(0xFF880E4F) else Color(0xFF64748B)
                                )
                                Text(
                                    text = "بطلة",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isGirl) Color(0xFFE91E63) else Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    textStyle = TextStyle(
                        color = Color(0xFF1E293B),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    placeholder = {
                        Text(
                            text = if (isGirl) "اكتبي اسمك هنا..." else "اكتب اسمك هنا...",
                            fontSize = 16.sp,
                            color = Color(0xFF90A4AE)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "اسم الطفل",
                            tint = if (isGirl) Color(0xFFE91E63) else Color(0xFF0288D1)
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color(0xFF1E293B),
                        unfocusedTextColor = Color(0xFF1E293B),
                        disabledTextColor = Color(0xFF1E293B),
                        focusedBorderColor = if (isGirl) Color(0xFFE91E63) else Color(0xFF0288D1),
                        unfocusedBorderColor = Color(0xFFB0BEC5),
                        focusedContainerColor = Color(0xFFFFFDE7),
                        unfocusedContainerColor = Color(0xFFF8FAFC)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_child_name")
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onSaveName(nameInput, selectedAvatarUri, selectedGender) },
                    enabled = nameInput.trim().isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isGirl) Color(0xFFAD1457) else Color(0xFF2E7D32)
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_save_child_name")
                ) {
                    Text(
                        text = "حفظ وبدء اللعب 🚀",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}
