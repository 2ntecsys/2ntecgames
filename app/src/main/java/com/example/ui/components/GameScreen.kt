package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.data.Animal
import com.example.viewmodel.GameViewModel
import kotlin.math.roundToInt

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var rootCoordinates by remember { mutableStateOf<LayoutCoordinates?>(null) }
    val shadowTargetCoordsMap = remember { mutableMapOf<String, LayoutCoordinates>() }

    // Direct, zero-latency 1:1 finger tracking drag state
    var draggingAnimal by remember { mutableStateOf<Animal?>(null) }
    var dragFingerInBox by remember { mutableStateOf(Offset.Zero) }
    var touchOffsetFromCardCenter by remember { mutableStateOf(Offset.Zero) }
    var dragOriginCenterInBox by remember { mutableStateOf(Offset.Zero) }
    var isAnimatingRelease by remember { mutableStateOf(false) }
    val releaseAnimatable = remember { Animatable(Offset.Zero, Offset.VectorConverter) }

    if (uiState.isNameDialogShowing) {
        OnboardingNameDialog(
            initialName = uiState.childName,
            initialAvatarUri = uiState.userAvatarUri,
            initialGender = uiState.gender,
            onSaveName = { name, avatarUri, gender -> viewModel.saveChildProfile(name, avatarUri, gender) },
            onDismiss = { viewModel.closeNameInput() }
        )
    }

    if (uiState.isWelcomeScreenActive) {
        if (uiState.isModeSelectionOpen) {
            ModeSelectionScreen(
                childName = uiState.childName,
                userAvatarUri = uiState.userAvatarUri,
                gender = uiState.gender,
                onSelectMode = { mode -> viewModel.selectGameMode(mode) },
                onChangeProfile = { viewModel.openNameInput() }
            )
        } else {
            androidx.compose.runtime.LaunchedEffect(Unit) {
                viewModel.triggerWelcomeAudio()
            }
            WelcomeScreen(
                childName = uiState.childName,
                userAvatarUri = uiState.userAvatarUri,
                gender = uiState.gender,
                currentLevelNumber = uiState.currentLevel.levelNumber,
                onStartGame = { viewModel.startPlayingGame() },
                onChangeName = { viewModel.openNameInput() }
            )
        }
        return
    }

    if (uiState.isGrandVictory) {
        VictoryScreen(
            childName = uiState.childName,
            isGirl = uiState.gender == "GIRL",
            onPlayAgain = { viewModel.loadLevel(0) },
            onOpenLevelSelector = { viewModel.openLevelSelector() },
            onGoToHome = { viewModel.openModeSelection() }
        )
        return
    }

    if (uiState.isLevelSelectorOpen) {
        LevelSelectorScreen(
            unlockedLevelMax = uiState.unlockedLevelMax,
            selectedMode = uiState.selectedMode,
            onSelectLevel = { level ->
                viewModel.loadLevel(level.levelNumber - 1)
            },
            onBackToGame = { viewModel.closeLevelSelector() },
            onGoToHome = { viewModel.openModeSelection() }
        )
        return
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFFE1F5FE)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned { coordinates ->
                    rootCoordinates = coordinates
                }
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE1F5FE),
                            Color(0xFFE0F7FA)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                // Top Header Bar
                LevelHeader(
                    currentLevel = uiState.currentLevel,
                    childName = uiState.childName,
                    userAvatarUri = uiState.userAvatarUri,
                    gender = uiState.gender,
                    matchedCount = uiState.matchedAnimalIds.size,
                    totalCount = uiState.currentAnimals.size,
                    soundEnabled = uiState.soundEnabled,
                    onToggleSound = { viewModel.toggleSound() },
                    onRestartLevel = { viewModel.restartCurrentLevel() },
                    onOpenLevelSelector = { viewModel.openLevelSelector() },
                    onOpenWelcomeScreen = { viewModel.returnToWelcomeScreen() }
                )

                Spacer(modifier = Modifier.height(10.dp))

                AnimatedContent(
                    targetState = uiState.currentLevelIndex,
                    transitionSpec = {
                        slideInHorizontally(
                            initialOffsetX = { fullWidth -> fullWidth },
                            animationSpec = tween(350)
                        ) + fadeIn(animationSpec = tween(350)) togetherWith
                        slideOutHorizontally(
                            targetOffsetX = { fullWidth -> -fullWidth },
                            animationSpec = tween(350)
                        ) + fadeOut(animationSpec = tween(350))
                    },
                    label = "LevelSlideTransition",
                    modifier = Modifier.fillMaxSize()
                ) { _ ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // TOP SECTION: Shadow Silhouette Targets
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFE0F7FA),
                                shadowElevation = 2.dp
                            ) {
                                Text(
                                    text = "💡 طابق الحيوان مع ظله بالاعلى:",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 15.sp
                                    ),
                                    color = Color(0xFF0277BD),
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            val itemCount = uiState.targetAnimalsShuffled.size
                            val cardSize = when {
                                itemCount <= 3 -> 115.dp
                                itemCount <= 5 -> 105.dp
                                itemCount <= 7 -> 96.dp
                                else -> 90.dp
                            }

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                maxItemsInEachRow = if (itemCount <= 4) 3 else 4
                            ) {
                                uiState.targetAnimalsShuffled.forEach { animal ->
                                    val isMatched = uiState.matchedAnimalIds.contains(animal.id)
                                    Box(modifier = Modifier.padding(horizontal = 6.dp)) {
                                        ShadowTarget(
                                            animal = animal,
                                            isMatched = isMatched,
                                            cardSize = cardSize,
                                            onPositioned = { targetAnimal, coords ->
                                                shadowTargetCoordsMap[targetAnimal.id] = coords
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // BOTTOM SECTION: Draggable Animal Figures (Clean open layout without yellow background card)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val itemCount = uiState.currentAnimals.size
                            val cardSize = when {
                                itemCount <= 3 -> 115.dp
                                itemCount <= 5 -> 105.dp
                                itemCount <= 7 -> 96.dp
                                else -> 90.dp
                            }

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalArrangement = Arrangement.spacedBy(14.dp),
                                maxItemsInEachRow = if (itemCount <= 4) 3 else 4
                            ) {
                                uiState.currentAnimals.forEach { animal ->
                                    val isMatched = uiState.matchedAnimalIds.contains(animal.id)
                                    val isBeingDragged = draggingAnimal?.id == animal.id

                                    Box(modifier = Modifier.padding(horizontal = 6.dp)) {
                                        AnimalCard(
                                            animal = animal,
                                            cardSize = cardSize,
                                            isMatched = isMatched,
                                            isBeingDragged = isBeingDragged,
                                            onDragStart = { draggedAnimal, cardCoords, offsetInCard ->
                                                val root = rootCoordinates
                                                if (root != null && root.isAttached && cardCoords.isAttached && !isAnimatingRelease) {
                                                    val rootPos = root.positionInRoot()
                                                    val cardPos = cardCoords.positionInRoot()
                                                    val cardW = cardCoords.size.width.toFloat()
                                                    val cardH = cardCoords.size.height.toFloat()

                                                    // Calculate coordinates in the exact local coordinate space of the root Box
                                                    val cardTopLeftInBox = cardPos - rootPos
                                                    val cardCenterInBox = Offset(cardTopLeftInBox.x + cardW / 2f, cardTopLeftInBox.y + cardH / 2f)
                                                    val touchInBox = cardTopLeftInBox + offsetInCard

                                                    dragOriginCenterInBox = cardCenterInBox
                                                    touchOffsetFromCardCenter = offsetInCard - Offset(cardW / 2f, cardH / 2f)
                                                    dragFingerInBox = touchInBox
                                                    draggingAnimal = draggedAnimal
                                                    isAnimatingRelease = false

                                                    viewModel.playDragStartSound()
                                                }
                                            },
                                            onDragDelta = { delta ->
                                                if (!isAnimatingRelease) {
                                                    // Direct 1:1 instantaneous finger movement tracking without lag!
                                                    dragFingerInBox = Offset(dragFingerInBox.x + delta.x, dragFingerInBox.y + delta.y)
                                                }
                                            },
                                            onDragEnd = {
                                                if (!isAnimatingRelease && draggingAnimal != null) {
                                                    val targetAnimal = draggingAnimal!!
                                                    val root = rootCoordinates
                                                    val targetCoords = shadowTargetCoordsMap[targetAnimal.id]
                                                    val currentCardCenterInBox = dragFingerInBox - touchOffsetFromCardCenter

                                                    var matched = false
                                                    var targetCenterInBox = Offset.Zero

                                                    if (root != null && root.isAttached && targetCoords != null && targetCoords.isAttached) {
                                                        val rootPos = root.positionInRoot()
                                                        val targetPos = targetCoords.positionInRoot()
                                                        val targetW = targetCoords.size.width.toFloat()
                                                        val targetH = targetCoords.size.height.toFloat()
                                                        val targetTopLeftInBox = targetPos - rootPos
                                                        targetCenterInBox = Offset(targetTopLeftInBox.x + targetW / 2f, targetTopLeftInBox.y + targetH / 2f)

                                                        // Generous hit threshold for kids (140px margin around target shadow)
                                                        val hitArea = Rect(
                                                            left = targetTopLeftInBox.x - 140f,
                                                            top = targetTopLeftInBox.y - 140f,
                                                            right = targetTopLeftInBox.x + targetW + 140f,
                                                            bottom = targetTopLeftInBox.y + targetH + 140f
                                                        )

                                                        matched = hitArea.contains(currentCardCenterInBox) || hitArea.contains(dragFingerInBox)
                                                    }

                                                    isAnimatingRelease = true
                                                    coroutineScope.launch {
                                                        if (matched) {
                                                            // Fluid spring snap directly into the target shadow center!
                                                            releaseAnimatable.snapTo(currentCardCenterInBox)
                                                            releaseAnimatable.animateTo(
                                                                targetValue = targetCenterInBox,
                                                                animationSpec = spring(
                                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                                    stiffness = Spring.StiffnessMedium
                                                                )
                                                            )
                                                            viewModel.onAnimalMatched(targetAnimal)
                                                        } else {
                                                            // Fluid spring return back to original card slot
                                                            releaseAnimatable.snapTo(currentCardCenterInBox)
                                                            releaseAnimatable.animateTo(
                                                                targetValue = dragOriginCenterInBox,
                                                                animationSpec = spring(
                                                                    dampingRatio = Spring.DampingRatioLowBouncy,
                                                                    stiffness = Spring.StiffnessMedium
                                                                )
                                                            )
                                                            viewModel.onAnimalMissed()
                                                        }
                                                        draggingAnimal = null
                                                        isAnimatingRelease = false
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }

            // FLOATING DRAG OVERLAY LAYER (Top Z-Index: 1000)
            draggingAnimal?.let { animal ->
                val itemCount = uiState.currentAnimals.size
                val cardSize = when {
                    itemCount <= 3 -> 115.dp
                    itemCount <= 5 -> 105.dp
                    itemCount <= 7 -> 96.dp
                    else -> 90.dp
                }

                val density = LocalDensity.current
                val cardSizePx = with(density) { cardSize.toPx() }

                // The figure is placed directly under the finger at all times!
                val currentCenter = if (isAnimatingRelease) {
                    releaseAnimatable.value
                } else {
                    dragFingerInBox - touchOffsetFromCardCenter
                }

                androidx.compose.runtime.CompositionLocalProvider(
                    androidx.compose.ui.platform.LocalLayoutDirection provides androidx.compose.ui.unit.LayoutDirection.Ltr
                ) {
                    Box(
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    (currentCenter.x - cardSizePx / 2f).roundToInt(),
                                    (currentCenter.y - cardSizePx / 2f).roundToInt()
                                )
                            }
                            .zIndex(1000f)
                    ) {
                        FloatingAnimalCardOverlay(
                            animal = animal,
                            cardSize = cardSize
                        )
                    }
                }
            }

            // Confetti Layer (أوراق ملونة)
            ConfettiView(isActive = uiState.isConfettiActive)

            // Stage Complete Modal Dialog
            if (uiState.isStageCompleteDialogOpen) {
                VictoryDialog(
                    levelNumber = uiState.currentLevel.levelNumber,
                    childName = uiState.childName,
                    isGirl = uiState.gender == "GIRL",
                    onNextLevel = { viewModel.nextLevel() },
                    onReplayLevel = { viewModel.restartCurrentLevel() },
                    onGoToHome = { viewModel.openModeSelection() },
                    onDismiss = { viewModel.restartCurrentLevel() }
                )
            }
        }
    }
}
