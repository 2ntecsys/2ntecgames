package com.example.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.Animal
import com.example.data.Level
import com.example.data.LevelRepository
import com.example.sound.SoundManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.example.data.GameMode

data class GameUiState(
    val childName: String = "",
    val userAvatarUri: String? = null,
    val gender: String = "BOY",
    val selectedMode: GameMode = GameMode.ZOO_ANIMALS,
    val isFirstLaunch: Boolean = true,
    val isWelcomeScreenActive: Boolean = true,
    val isModeSelectionOpen: Boolean = false,
    val isNameDialogShowing: Boolean = false,
    val currentLevelIndex: Int = 0,
    val currentLevel: Level = LevelRepository.animalLevels[0],
    val currentAnimals: List<Animal> = emptyList(),
    val targetAnimalsShuffled: List<Animal> = emptyList(),
    val matchedAnimalIds: Set<String> = emptySet(),
    val isConfettiActive: Boolean = false,
    val isStageCompleteDialogOpen: Boolean = false,
    val isGrandVictory: Boolean = false,
    val isLevelSelectorOpen: Boolean = false,
    val unlockedLevelMax: Int = 1,
    val soundEnabled: Boolean = true
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val soundManager = SoundManager(application)
    private val prefs = application.getSharedPreferences("AnimalPuzzlePrefs", Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Map storing top target shadow slot screen bounds: Animal ID -> Rect
    val shadowTargetsBoundsMap = mutableMapOf<String, Rect>()

    private fun getUserKey(key: String, userName: String = _uiState.value.childName): String {
        val clean = userName.trim().lowercase()
        return if (clean.isNotEmpty()) "${key}_$clean" else key
    }

    init {
        val savedName = prefs.getString("user_child_name", "") ?: ""
        val isFirst = savedName.isEmpty()
        val savedGender = if (savedName.isNotEmpty()) {
            prefs.getString(getUserKey("user_gender", savedName), prefs.getString("user_gender", "BOY")) ?: "BOY"
        } else {
            prefs.getString("user_gender", "BOY") ?: "BOY"
        }
        soundManager.isGirl = (savedGender == "GIRL")

        val savedModeName = prefs.getString("selected_mode", GameMode.ZOO_ANIMALS.name) ?: GameMode.ZOO_ANIMALS.name
        val savedMode = try { GameMode.valueOf(savedModeName) } catch (_: Exception) { GameMode.ZOO_ANIMALS }

        val savedAvatar = if (savedName.isNotEmpty()) {
            prefs.getString(getUserKey("user_avatar_uri", savedName), null)
                ?: prefs.getString("user_avatar_uri", null)
        } else {
            prefs.getString("user_avatar_uri", null)
        }
        val modeKey = savedMode.name
        val savedLastLevel = if (savedName.isNotEmpty()) {
            prefs.getInt(getUserKey("last_level_index_$modeKey", savedName), prefs.getInt("last_level_index_$modeKey", 0))
        } else {
            prefs.getInt("last_level_index_$modeKey", 0)
        }
        val savedUnlocked = if (savedName.isNotEmpty()) {
            prefs.getInt(getUserKey("unlocked_level_max_$modeKey", savedName), prefs.getInt("unlocked_level_max_$modeKey", maxOf(1, savedLastLevel + 1)))
        } else {
            prefs.getInt("unlocked_level_max_$modeKey", 1)
        }

        _uiState.value = _uiState.value.copy(
            childName = savedName,
            userAvatarUri = savedAvatar,
            gender = savedGender,
            selectedMode = savedMode,
            isFirstLaunch = isFirst,
            isWelcomeScreenActive = true,
            isModeSelectionOpen = false,
            isNameDialogShowing = isFirst,
            unlockedLevelMax = savedUnlocked
        )

        loadLevel(savedLastLevel, savedMode)

        if (!isFirst && savedName.isNotEmpty()) {
            soundManager.playWelcomeSound(savedName)
        }
    }

    fun triggerWelcomeAudio() {
        soundManager.playWelcomeSound(_uiState.value.childName)
    }

    fun playDragStartSound() {
        soundManager.playDragStartSound()
    }

    fun saveChildProfile(name: String, avatarUri: String?, gender: String = "BOY") {
        val defaultTitle = if (gender == "GIRL") "بطلة" else "بطل"
        val trimmed = name.trim().ifEmpty { defaultTitle }
        soundManager.isGirl = (gender == "GIRL")

        val editor = prefs.edit()
            .putString("user_child_name", trimmed)
            .putString("user_gender", gender)
            .putString(getUserKey("user_gender", trimmed), gender)

        if (avatarUri != null) {
            editor.putString(getUserKey("user_avatar_uri", trimmed), avatarUri)
            editor.putString("user_avatar_uri", avatarUri)
        }
        
        // Retrieve last level and progress specific to this child & mode
        val modeKey = _uiState.value.selectedMode.name
        val userLastLevel = prefs.getInt(getUserKey("last_level_index_$modeKey", trimmed), 0)
        val userUnlocked = prefs.getInt(getUserKey("unlocked_level_max_$modeKey", trimmed), maxOf(1, userLastLevel + 1))
        editor.apply()

        _uiState.value = _uiState.value.copy(
            childName = trimmed,
            userAvatarUri = avatarUri ?: prefs.getString(getUserKey("user_avatar_uri", trimmed), null),
            gender = gender,
            isFirstLaunch = false,
            isNameDialogShowing = false,
            isWelcomeScreenActive = true,
            isModeSelectionOpen = false,
            isConfettiActive = true,
            unlockedLevelMax = userUnlocked
        )

        loadLevel(userLastLevel, _uiState.value.selectedMode)
        soundManager.playWelcomeSound(trimmed)
    }

    fun selectGameMode(mode: GameMode) {
        val modeKey = mode.name
        val modeLastLevel = prefs.getInt(getUserKey("last_level_index_$modeKey"), prefs.getInt("last_level_index_$modeKey", 0))
        val modeUnlocked = prefs.getInt(getUserKey("unlocked_level_max_$modeKey"), prefs.getInt("unlocked_level_max_$modeKey", 1))

        _uiState.value = _uiState.value.copy(
            selectedMode = mode,
            isWelcomeScreenActive = false,
            isModeSelectionOpen = false,
            unlockedLevelMax = modeUnlocked
        )

        loadLevel(modeLastLevel, mode)
        soundManager.playMatchSnap()
    }

    fun openModeSelection() {
        _uiState.value = _uiState.value.copy(
            isWelcomeScreenActive = true,
            isModeSelectionOpen = true,
            isConfettiActive = true
        )
    }

    fun saveChildName(name: String) {
        saveChildProfile(name, _uiState.value.userAvatarUri, _uiState.value.gender)
    }

    fun openNameInput() {
        _uiState.value = _uiState.value.copy(isNameDialogShowing = true)
    }

    fun closeNameInput() {
        if (_uiState.value.childName.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(isNameDialogShowing = false)
        }
    }

    fun startPlayingGame() {
        _uiState.value = _uiState.value.copy(
            isWelcomeScreenActive = true,
            isModeSelectionOpen = true,
            isConfettiActive = false
        )
        soundManager.playMatchSnap()
    }

    fun returnToWelcomeScreen() {
        _uiState.value = _uiState.value.copy(
            isWelcomeScreenActive = true,
            isModeSelectionOpen = true,
            isConfettiActive = true
        )
        soundManager.playWelcomeSound(_uiState.value.childName)
    }

    fun loadLevel(levelIndex: Int, mode: GameMode = _uiState.value.selectedMode) {
        val currentLevels = LevelRepository.getLevelsForMode(mode)
        val safeIndex = levelIndex.coerceIn(0, currentLevels.size - 1)
        val level = currentLevels[safeIndex]
        val animals = LevelRepository.getAnimalsForLevel(level.levelNumber, mode)

        val modeKey = mode.name
        prefs.edit()
            .putString("selected_mode", modeKey)
            .putInt("last_level_index_$modeKey", safeIndex)
            .putInt(getUserKey("last_level_index_$modeKey"), safeIndex)
            .apply()

        // Scramble target positions relative to draggable bottom cards
        val targetSeed = level.levelNumber * 17
        val targetShuffled = animals.shuffled(java.util.Random(targetSeed.toLong()))

        shadowTargetsBoundsMap.clear()

        _uiState.value = _uiState.value.copy(
            currentLevelIndex = safeIndex,
            currentLevel = level,
            currentAnimals = animals,
            targetAnimalsShuffled = targetShuffled,
            matchedAnimalIds = emptySet(),
            isConfettiActive = false,
            isStageCompleteDialogOpen = false,
            isGrandVictory = false,
            isLevelSelectorOpen = false
        )
    }

    fun registerShadowTargetPosition(animal: Animal, bounds: Rect) {
        shadowTargetsBoundsMap[animal.id] = bounds
    }

    fun onAnimalMatched(animal: Animal) {
        val newMatched = _uiState.value.matchedAnimalIds + animal.id
        val isLevelComplete = newMatched.size >= _uiState.value.currentAnimals.size

        _uiState.value = _uiState.value.copy(
            matchedAnimalIds = newMatched,
            isConfettiActive = true
        )

        // Confetti reset timer
        viewModelScope.launch {
            delay(2400)
            _uiState.value = _uiState.value.copy(isConfettiActive = false)
        }

        // Harmonized Speech & Screen Progression
        soundManager.playCorrectMatchSound(
            childName = _uiState.value.childName,
            animalName = animal.arabicName,
            onDone = {
                if (isLevelComplete) {
                    viewModelScope.launch {
                        delay(400)
                        onLevelCompleted()
                    }
                }
            }
        )

        if (isLevelComplete) {
            viewModelScope.launch {
                delay(2600)
                if (!_uiState.value.isStageCompleteDialogOpen && !_uiState.value.isGrandVictory) {
                    onLevelCompleted()
                }
            }
        }
    }

    fun onAnimalMissed() {
        soundManager.playWrongDrop()
    }

    /**
     * Called when user drops an animal card at screen center offset
     */
    fun onDropAnimalCard(animal: Animal, dropCenter: Offset): Boolean {
        val targetBounds = shadowTargetsBoundsMap[animal.id] ?: return false

        val expandedTarget = Rect(
            left = targetBounds.left - 140f,
            top = targetBounds.top - 140f,
            right = targetBounds.right + 140f,
            bottom = targetBounds.bottom + 140f
        )

        val isHit = expandedTarget.contains(dropCenter)

        if (isHit) {
            onAnimalMatched(animal)
            return true
        } else {
            onAnimalMissed()
            return false
        }
    }

    private fun onLevelCompleted() {
        if (_uiState.value.isStageCompleteDialogOpen || _uiState.value.isGrandVictory) return

        val mode = _uiState.value.selectedMode
        val modeKey = mode.name
        val currentLevels = LevelRepository.getLevelsForMode(mode)
        val currentIdx = _uiState.value.currentLevelIndex
        val nextLevelNumber = currentIdx + 2 // 1-indexed next level

        // Unlock next level in prefs both globally and for current user per mode
        val newUnlockedMax = maxOf(_uiState.value.unlockedLevelMax, nextLevelNumber)
        prefs.edit()
            .putInt("unlocked_level_max_$modeKey", newUnlockedMax)
            .putInt(getUserKey("unlocked_level_max_$modeKey"), newUnlockedMax)
            .apply()

        _uiState.value = _uiState.value.copy(unlockedLevelMax = newUnlockedMax)

        if (currentIdx >= currentLevels.size - 1) {
            // Completed all levels for this mode! Show Grand Victory Screen with Trophy!
            soundManager.playVictorySound(_uiState.value.childName)
            _uiState.value = _uiState.value.copy(
                isGrandVictory = true,
                isConfettiActive = true
            )
        } else {
            // Completed stage: celebrate with applause sound and open stage complete dialog
            soundManager.playStageCompleteSound(_uiState.value.childName)
            _uiState.value = _uiState.value.copy(isStageCompleteDialogOpen = true)
        }
    }

    fun nextLevel() {
        val mode = _uiState.value.selectedMode
        val currentLevels = LevelRepository.getLevelsForMode(mode)
        val nextIdx = _uiState.value.currentLevelIndex + 1
        if (nextIdx < currentLevels.size) {
            loadLevel(nextIdx, mode)
        } else {
            loadLevel(0, mode)
        }
    }

    fun restartCurrentLevel() {
        loadLevel(_uiState.value.currentLevelIndex, _uiState.value.selectedMode)
    }

    fun openLevelSelector() {
        _uiState.value = _uiState.value.copy(isLevelSelectorOpen = true)
    }

    fun closeLevelSelector() {
        _uiState.value = _uiState.value.copy(isLevelSelectorOpen = false)
    }

    fun toggleSound() {
        val newSoundState = !_uiState.value.soundEnabled
        soundManager.soundEnabled = newSoundState
        _uiState.value = _uiState.value.copy(soundEnabled = newSoundState)
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.onDestroy()
    }
}
