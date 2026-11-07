package com.example.data

enum class GameMode {
    ZOO_ANIMALS,
    SHAPES_AND_OBJECTS
}

data class Level(
    val levelNumber: Int,
    val itemCount: Int,
    val titleArabic: String,
    val subtitleArabic: String,
    val badgeIcon: String
)

object LevelRepository {
    val animalLevels = listOf(
        Level(1, 3, "المرحلة الأولى", "3 حيوانات مرحة", "🌱"),
        Level(2, 4, "المرحلة الثانية", "4 حيوانات أليفة", "🐣"),
        Level(3, 4, "المرحلة الثالثة", "4 حيوانات صحراوية", "⭐"),
        Level(4, 5, "المرحلة الرابعة", "5 حيوانات الغابة", "🌿"),
        Level(5, 5, "المرحلة الخامسة", "5 حيوانات المزرعة", "🍎"),
        Level(6, 6, "المرحلة السادسة", "6 حيوانات طائرة وبرية", "🎈"),
        Level(7, 6, "المرحلة السابعة", "6 أصدقاء غابة العجائب", "🎨"),
        Level(8, 7, "المرحلة الثامنة", "7 أبطال الغابة", "🔥"),
        Level(9, 8, "المرحلة التاسعة", "8 حيوانات شجاعة", "👑"),
        Level(10, 10, "المرحلة العاشرة", "المرحلة الكبرى: 10 حيوانات!", "🏆")
    )

    val shapeLevels = listOf(
        Level(1, 3, "المرحلة الأولى", "3 أشكال أساسية: مربع، مثلث، دائرة", "📐"),
        Level(2, 4, "المرحلة الثانية", "4 أشكال: مربع، مستطيل، شجرة، دائرة", "🔺"),
        Level(3, 4, "المرحلة الثالثة", "4 أشكال وأشياء: سيارة، طيارة، كوب، زجاجة", "🚗"),
        Level(4, 5, "المرحلة الرابعة", "5 أشكال وأشياء متنوعة", "✈️"),
        Level(5, 5, "المرحلة الخامسة", "5 عناصر ملونة", "🌳"),
        Level(6, 6, "المرحلة السادسة", "6 أشكال جميلة", "🎨"),
        Level(7, 7, "المرحلة السابعة", "7 عناصر مميزة", "☕"),
        Level(8, 8, "المرحلة الثامنة", "8 أشكال وأشياء رائعة", "🚀"),
        Level(9, 9, "المرحلة التاسعة", "جميع الأشكال الـ 9 الممتعة!", "👑"),
        Level(10, 9, "المرحلة العاشرة", "المرحلة الكبرى للأشكال والظلال!", "🏆")
    )

    val levels = animalLevels

    fun getLevelsForMode(mode: GameMode): List<Level> {
        return when (mode) {
            GameMode.ZOO_ANIMALS -> animalLevels
            GameMode.SHAPES_AND_OBJECTS -> shapeLevels
        }
    }

    fun getAnimalsForLevel(levelNumber: Int, mode: GameMode = GameMode.ZOO_ANIMALS): List<Animal> {
        val available = when (mode) {
            GameMode.ZOO_ANIMALS -> AnimalRepository.allAnimals
            GameMode.SHAPES_AND_OBJECTS -> AnimalRepository.allShapes
        }
        val currentLevels = getLevelsForMode(mode)
        val count = currentLevels.find { it.levelNumber == levelNumber }?.itemCount ?: 3
        
        // Pick distinct deterministic/shuffled item subsets for each level
        val seed = levelNumber * 13 + if (mode == GameMode.SHAPES_AND_OBJECTS) 999 else 0
        val shuffled = available.shuffled(java.util.Random(seed.toLong()))
        return shuffled.take(count.coerceAtMost(available.size))
    }
}
