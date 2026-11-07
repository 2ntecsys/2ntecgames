package com.example.data

import androidx.compose.ui.graphics.Color

enum class AnimalType {
    BIRD,
    CAT,
    FISH,
    ELEPHANT,
    ZEBRA,
    LION,
    GIRAFFE,
    DOG,
    BEAR,
    PENGUIN,
    SQUIRREL,

    // Shapes & Objects (أشكال وظلال)
    SQUARE,
    TRIANGLE,
    RECTANGLE,
    CIRCLE,
    TREE,
    CAR,
    AIRPLANE,
    BOTTLE,
    CUP
}

data class Animal(
    val id: String,
    val type: AnimalType,
    val arabicName: String,
    val emoji: String,
    val photoUrl: String,
    val soundText: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color
)

object AnimalRepository {
    val allAnimals = listOf(
        Animal(
            id = "bird",
            type = AnimalType.BIRD,
            arabicName = "عصفور",
            emoji = "🐦",
            photoUrl = "https://images.unsplash.com/photo-1555169062-013468b47731?w=500&auto=format&fit=crop&q=80",
            soundText = "تغريد العصفور الرائع!",
            primaryColor = Color(0xFF29B6F6),
            secondaryColor = Color(0xFF0277BD),
            accentColor = Color(0xFFE0F7FA)
        ),
        Animal(
            id = "cat",
            type = AnimalType.CAT,
            arabicName = "قطة",
            emoji = "🐱",
            photoUrl = "https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba?w=500&auto=format&fit=crop&q=80",
            soundText = "مواء القطة اللطيفة!",
            primaryColor = Color(0xFFFF8A65),
            secondaryColor = Color(0xFFD84315),
            accentColor = Color(0xFFFFCCBC)
        ),
        Animal(
            id = "fish",
            type = AnimalType.FISH,
            arabicName = "سمكة",
            emoji = "🐠",
            photoUrl = "https://images.unsplash.com/photo-1522069169874-c58ec4b76be5?w=500&auto=format&fit=crop&q=80",
            soundText = "السمكة الملونة في الماء!",
            primaryColor = Color(0xFF26C6DA),
            secondaryColor = Color(0xFF00838F),
            accentColor = Color(0xFFE0F7FA)
        ),
        Animal(
            id = "elephant",
            type = AnimalType.ELEPHANT,
            arabicName = "فيل",
            emoji = "🐘",
            photoUrl = "https://images.unsplash.com/photo-1557050543-4d5f4e07ef46?w=500&auto=format&fit=crop&q=80",
            soundText = "نهيم الفيل الضخم!",
            primaryColor = Color(0xFF90A4AE),
            secondaryColor = Color(0xFF37474F),
            accentColor = Color(0xFFECEFF1)
        ),
        Animal(
            id = "zebra",
            type = AnimalType.ZEBRA,
            arabicName = "حمار وحشي",
            emoji = "🦓",
            photoUrl = "https://images.unsplash.com/photo-1501706362039-c06b2d715385?w=500&auto=format&fit=crop&q=80",
            soundText = "الحمار الوحشي المخطط!",
            primaryColor = Color(0xFF424242),
            secondaryColor = Color(0xFF212121),
            accentColor = Color(0xFFF5F5F5)
        ),
        Animal(
            id = "lion",
            type = AnimalType.LION,
            arabicName = "أسد",
            emoji = "🦁",
            photoUrl = "https://images.unsplash.com/photo-1546182990-dffeafbe841d?w=500&auto=format&fit=crop&q=80",
            soundText = "زئير الأسد الشجاع!",
            primaryColor = Color(0xFFFFB300),
            secondaryColor = Color(0xFFE65100),
            accentColor = Color(0xFFFFECB3)
        ),
        Animal(
            id = "giraffe",
            type = AnimalType.GIRAFFE,
            arabicName = "زرافة",
            emoji = "🦒",
            photoUrl = "https://images.unsplash.com/photo-1547721064-da6cfb341d50?w=500&auto=format&fit=crop&q=80",
            soundText = "الزرافة الطويلة والجميلة!",
            primaryColor = Color(0xFFFFA726),
            secondaryColor = Color(0xFF8D6E63),
            accentColor = Color(0xFFFFE0B2)
        ),
        Animal(
            id = "dog",
            type = AnimalType.DOG,
            arabicName = "كلب",
            emoji = "🐶",
            photoUrl = "https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=500&auto=format&fit=crop&q=80",
            soundText = "نباح الكلب الوفي!",
            primaryColor = Color(0xFF8D6E63),
            secondaryColor = Color(0xFF4E342E),
            accentColor = Color(0xFFEFEBE9)
        ),
        Animal(
            id = "bear",
            type = AnimalType.BEAR,
            arabicName = "دب",
            emoji = "🐻",
            photoUrl = "https://images.unsplash.com/photo-1530595467537-0b5996c41f2d?w=500&auto=format&fit=crop&q=80",
            soundText = "الدب القوي!",
            primaryColor = Color(0xFF795548),
            secondaryColor = Color(0xFF3E2723),
            accentColor = Color(0xFFD7CCC8)
        ),
        Animal(
            id = "penguin",
            type = AnimalType.PENGUIN,
            arabicName = "بطريق",
            emoji = "🐧",
            photoUrl = "https://images.unsplash.com/photo-1598439210625-5067c578f3f6?w=500&auto=format&fit=crop&q=80",
            soundText = "البطريق اللطيف فوق الجليد!",
            primaryColor = Color(0xFF37474F),
            secondaryColor = Color(0xFFFF9800),
            accentColor = Color(0xFFE0F7FA)
        ),
        Animal(
            id = "squirrel",
            type = AnimalType.SQUIRREL,
            arabicName = "سنجاب",
            emoji = "🐿️",
            photoUrl = "https://images.unsplash.com/photo-1504006833117-8886a355efbf?w=500&auto=format&fit=crop&q=80",
            soundText = "السنجاب السريع والنشيط!",
            primaryColor = Color(0xFFFF7043),
            secondaryColor = Color(0xFFBF360C),
            accentColor = Color(0xFFFFCCBC)
        )
    )

    val allShapes = listOf(
        Animal(
            id = "square",
            type = AnimalType.SQUARE,
            arabicName = "مربع",
            emoji = "🟧",
            photoUrl = "",
            soundText = "المربع الرائع المتساوي الأضلاع!",
            primaryColor = Color(0xFFFF7043),
            secondaryColor = Color(0xFFD84315),
            accentColor = Color(0xFFFFCCBC)
        ),
        Animal(
            id = "triangle",
            type = AnimalType.TRIANGLE,
            arabicName = "مثلث",
            emoji = "🔺",
            photoUrl = "",
            soundText = "المثلث الملون بثلاثة أضلاع!",
            primaryColor = Color(0xFFAB47BC),
            secondaryColor = Color(0xFF6A1B9A),
            accentColor = Color(0xFFE1BEE7)
        ),
        Animal(
            id = "rectangle",
            type = AnimalType.RECTANGLE,
            arabicName = "مستطيل",
            emoji = "🟩",
            photoUrl = "",
            soundText = "المستطيل الميز والجميل!",
            primaryColor = Color(0xFF66BB6A),
            secondaryColor = Color(0xFF2E7D32),
            accentColor = Color(0xFFC8E6C9)
        ),
        Animal(
            id = "circle",
            type = AnimalType.CIRCLE,
            arabicName = "دائرة",
            emoji = "🔴",
            photoUrl = "",
            soundText = "الدائرة المستديرة اللطيفة!",
            primaryColor = Color(0xFFEF5350),
            secondaryColor = Color(0xFFC62828),
            accentColor = Color(0xFFFFCDD2)
        ),
        Animal(
            id = "tree",
            type = AnimalType.TREE,
            arabicName = "شجرة",
            emoji = "🌳",
            photoUrl = "",
            soundText = "الشجرة الخضراء المورقة!",
            primaryColor = Color(0xFF26A69A),
            secondaryColor = Color(0xFF00695C),
            accentColor = Color(0xFFB2DFDB)
        ),
        Animal(
            id = "car",
            type = AnimalType.CAR,
            arabicName = "سيارة",
            emoji = "🚗",
            photoUrl = "",
            soundText = "السيارة السريعة والجميلة!",
            primaryColor = Color(0xFF42A5F5),
            secondaryColor = Color(0xFF1565C0),
            accentColor = Color(0xFFBBDEFB)
        ),
        Animal(
            id = "airplane",
            type = AnimalType.AIRPLANE,
            arabicName = "طيارة",
            emoji = "✈️",
            photoUrl = "",
            soundText = "الطيارة التي تحلق في السماء!",
            primaryColor = Color(0xFFFFCA28),
            secondaryColor = Color(0xFFF57F17),
            accentColor = Color(0xFFFFF9C4)
        ),
        Animal(
            id = "bottle",
            type = AnimalType.BOTTLE,
            arabicName = "زجاجة",
            emoji = "🍾",
            photoUrl = "",
            soundText = "الزجاجة العجيبة!",
            primaryColor = Color(0xFF26C6DA),
            secondaryColor = Color(0xFF00838F),
            accentColor = Color(0xFFB2EBF2)
        ),
        Animal(
            id = "cup",
            type = AnimalType.CUP,
            arabicName = "كوب",
            emoji = "☕",
            photoUrl = "",
            soundText = "الكوب الدافئ والممتع!",
            primaryColor = Color(0xFFEC407A),
            secondaryColor = Color(0xFFAD1457),
            accentColor = Color(0xFFF8BBD0)
        )
    )
}
