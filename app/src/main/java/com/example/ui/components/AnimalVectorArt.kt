package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.example.data.Animal
import com.example.data.AnimalType
import kotlin.math.cos
import kotlin.math.sin

/**
 * Renders a full-body 3D-shaded standalone animal illustration (without any background)
 * or its 100% pixel-identical silhouette shadow when [isShadow] is true.
 *
 * All outer silhouette shapes use unified drawing helpers so the colored 3D animal
 * and its shadow share the exact same geometry, proportions, and curves.
 */
@Composable
fun AnimalVectorArt(
    animal: Animal,
    isShadow: Boolean,
    modifier: Modifier = Modifier,
    shadowColor: Color = Color(0xFF0F172A)
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val cx = w / 2f
        val cy = h / 2f

        when (animal.type) {
            AnimalType.BIRD -> draw3DBird(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.CAT -> draw3DCat(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.FISH -> draw3DFish(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.ELEPHANT -> draw3DElephant(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.ZEBRA -> draw3DZebra(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.LION -> draw3DLion(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.GIRAFFE -> draw3DGiraffe(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.DOG -> draw3DDog(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.BEAR -> draw3DBear(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.PENGUIN -> draw3DPenguin(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.SQUIRREL -> draw3DSquirrel(cx, cy, w, h, isShadow, shadowColor)

            // Shapes & Objects
            AnimalType.SQUARE -> draw3DSquare(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.TRIANGLE -> draw3DTriangle(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.RECTANGLE -> draw3DRectangle(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.CIRCLE -> draw3DCircle(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.TREE -> draw3DTree(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.CAR -> draw3DCar(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.AIRPLANE -> draw3DAirplane(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.BOTTLE -> draw3DBottle(cx, cy, w, h, isShadow, shadowColor)
            AnimalType.CUP -> draw3DCup(cx, cy, w, h, isShadow, shadowColor)
        }
    }
}

private fun DrawScope.drawPartCircle(
    center: Offset,
    radius: Float,
    isShadow: Boolean,
    shadowColor: Color,
    lightColor: Color,
    baseColor: Color,
    darkColor: Color
) {
    if (isShadow) {
        drawCircle(color = shadowColor, radius = radius, center = center)
    } else {
        val brush = Brush.radialGradient(
            colors = listOf(lightColor, baseColor, darkColor),
            center = Offset(center.x - radius * 0.3f, center.y - radius * 0.3f),
            radius = (radius * 1.35f).coerceAtLeast(1f)
        )
        drawCircle(brush = brush, radius = radius, center = center)
    }
}

private fun DrawScope.drawPartOval(
    topLeft: Offset,
    size: Size,
    isShadow: Boolean,
    shadowColor: Color,
    lightColor: Color,
    baseColor: Color,
    darkColor: Color
) {
    if (isShadow) {
        drawOval(color = shadowColor, topLeft = topLeft, size = size)
    } else {
        val center = Offset(topLeft.x + size.width * 0.38f, topLeft.y + size.height * 0.35f)
        val radius = (size.width.coerceAtLeast(size.height) * 0.75f).coerceAtLeast(1f)
        val brush = Brush.radialGradient(
            colors = listOf(lightColor, baseColor, darkColor),
            center = center,
            radius = radius
        )
        drawOval(brush = brush, topLeft = topLeft, size = size)
    }
}

private fun DrawScope.drawPartRoundRect(
    topLeft: Offset,
    size: Size,
    cornerRadius: CornerRadius,
    isShadow: Boolean,
    shadowColor: Color,
    lightColor: Color,
    baseColor: Color,
    darkColor: Color
) {
    if (isShadow) {
        drawRoundRect(
            color = shadowColor,
            topLeft = topLeft,
            size = size,
            cornerRadius = cornerRadius
        )
    } else {
        val brush = Brush.linearGradient(
            colors = listOf(lightColor, baseColor, darkColor),
            start = topLeft,
            end = Offset(topLeft.x + size.width, topLeft.y + size.height)
        )
        drawRoundRect(
            brush = brush,
            topLeft = topLeft,
            size = size,
            cornerRadius = cornerRadius
        )
    }
}

private fun DrawScope.drawPartPath(
    path: Path,
    gradCenter: Offset,
    gradRadius: Float,
    isShadow: Boolean,
    shadowColor: Color,
    lightColor: Color,
    baseColor: Color,
    darkColor: Color
) {
    if (isShadow) {
        drawPath(path = path, color = shadowColor)
    } else {
        val brush = Brush.radialGradient(
            colors = listOf(lightColor, baseColor, darkColor),
            center = gradCenter,
            radius = gradRadius.coerceAtLeast(1f)
        )
        drawPath(path = path, brush = brush)
    }
}

private fun DrawScope.drawPartLine(
    start: Offset,
    end: Offset,
    strokeWidth: Float,
    isShadow: Boolean,
    shadowColor: Color,
    color: Color
) {
    drawLine(
        color = if (isShadow) shadowColor else color,
        start = start,
        end = end,
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawEye3D(center: Offset, radius: Float) {
    drawCircle(color = Color.White, radius = radius, center = center)
    drawCircle(color = Color(0xFF1E293B), radius = radius * 0.65f, center = Offset(center.x + radius * 0.08f, center.y))
    drawCircle(color = Color.White, radius = radius * 0.26f, center = Offset(center.x - radius * 0.18f, center.y - radius * 0.22f))
}

// 1. عصفور (BIRD) - Full 3D blue bird with tail feathers, crest, wing, beak, and legs
private fun DrawScope.draw3DBird(cx: Float, cy: Float, w: Float, h: Float, isShadow: Boolean, shadowColor: Color) {
    val r = w.coerceAtMost(h) * 0.34f

    // Legs & Feet
    drawPartLine(Offset(cx - r * 0.2f, cy + r * 0.7f), Offset(cx - r * 0.25f, cy + r * 1.18f), r * 0.1f, isShadow, shadowColor, Color(0xFFF57C00))
    drawPartLine(Offset(cx + r * 0.15f, cy + r * 0.7f), Offset(cx + r * 0.2f, cy + r * 1.18f), r * 0.1f, isShadow, shadowColor, Color(0xFFF57C00))

    // Fanned Tail Feathers (Left)
    val tailPath = Path().apply {
        moveTo(cx - r * 0.65f, cy + r * 0.05f)
        lineTo(cx - r * 1.38f, cy - r * 0.45f)
        lineTo(cx - r * 1.25f, cy - r * 0.05f)
        lineTo(cx - r * 1.42f, cy + r * 0.28f)
        lineTo(cx - r * 0.65f, cy + r * 0.35f)
        close()
    }
    drawPartPath(tailPath, Offset(cx - r, cy), r * 0.8f, isShadow, shadowColor, Color(0xFF4FC3F7), Color(0xFF0288D1), Color(0xFF01579B))

    // Top Crest Feathers
    val crestPath = Path().apply {
        moveTo(cx, cy - r * 0.75f)
        quadraticTo(cx - r * 0.25f, cy - r * 1.35f, cx - r * 0.48f, cy - r * 1.15f)
        quadraticTo(cx - r * 0.2f, cy - r * 0.9f, cx - r * 0.1f, cy - r * 0.75f)
        close()
    }
    drawPartPath(crestPath, Offset(cx - r * 0.2f, cy - r), r * 0.5f, isShadow, shadowColor, Color(0xFF81D4FA), Color(0xFF039BE5), Color(0xFF0277BD))

    // Plump Bird Body & Head
    drawPartOval(
        topLeft = Offset(cx - r * 0.82f, cy - r * 0.55f),
        size = Size(r * 1.55f, r * 1.4f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFF81D4FA),
        baseColor = Color(0xFF29B6F6),
        darkColor = Color(0xFF0277BD)
    )
    drawPartCircle(
        center = Offset(cx + r * 0.22f, cy - r * 0.32f),
        radius = r * 0.58f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFB3E5FC),
        baseColor = Color(0xFF29B6F6),
        darkColor = Color(0xFF0288D1)
    )

    // Raised Wing
    val wingPath = Path().apply {
        moveTo(cx - r * 0.2f, cy - r * 0.2f)
        cubicTo(cx - r * 0.75f, cy - r * 0.95f, cx - r * 1.05f, cy - r * 0.25f, cx - r * 0.55f, cy + r * 0.35f)
        quadraticTo(cx - r * 0.15f, cy + r * 0.3f, cx - r * 0.2f, cy - r * 0.2f)
        close()
    }
    drawPartPath(wingPath, Offset(cx - r * 0.5f, cy - r * 0.2f), r * 0.8f, isShadow, shadowColor, Color(0xFF4FC3F7), Color(0xFF039BE5), Color(0xFF01579B))

    // Open Beak (Right)
    val beakPath = Path().apply {
        moveTo(cx + r * 0.68f, cy - r * 0.48f)
        lineTo(cx + r * 1.35f, cy - r * 0.28f)
        lineTo(cx + r * 0.68f, cy - r * 0.1f)
        close()
    }
    drawPartPath(beakPath, Offset(cx + r * 0.9f, cy - r * 0.3f), r * 0.5f, isShadow, shadowColor, Color(0xFFFFD54F), Color(0xFFFF9800), Color(0xFFE65100))

    if (!isShadow) {
        // Belly Highlight
        drawOval(
            color = Color(0xFFE1F5FE),
            topLeft = Offset(cx - r * 0.35f, cy + r * 0.05f),
            size = Size(r * 0.85f, r * 0.68f)
        )
        drawEye3D(Offset(cx + r * 0.38f, cy - r * 0.42f), r * 0.16f)
        drawCircle(color = Color(0xFFFF8A80).copy(alpha = 0.7f), radius = r * 0.12f, center = Offset(cx + r * 0.25f, cy - r * 0.18f))
    }
}

// 2. قطة (CAT) - Full 3D sitting cat with pointed ears, whiskers, paws, and curved tail
private fun DrawScope.draw3DCat(cx: Float, cy: Float, w: Float, h: Float, isShadow: Boolean, shadowColor: Color) {
    val r = w.coerceAtMost(h) * 0.32f

    // Curved Cat Tail (Right side)
    val tailPath = Path().apply {
        moveTo(cx + r * 0.45f, cy + r * 0.85f)
        cubicTo(cx + r * 1.35f, cy + r * 0.85f, cx + r * 1.45f, cy - r * 0.25f, cx + r * 0.95f, cy - r * 0.45f)
        cubicTo(cx + r * 0.75f, cy - r * 0.52f, cx + r * 0.72f, cy - r * 0.22f, cx + r * 0.88f, cy - r * 0.15f)
        cubicTo(cx + r * 1.12f, cy - r * 0.02f, cx + r * 1.0f, cy + r * 0.58f, cx + r * 0.4f, cy + r * 0.62f)
        close()
    }
    drawPartPath(tailPath, Offset(cx + r * 0.9f, cy + r * 0.2f), r, isShadow, shadowColor, Color(0xFFFFAB91), Color(0xFFFF7043), Color(0xFFD84315))

    // Sitting Body
    drawPartOval(
        topLeft = Offset(cx - r * 0.68f, cy + r * 0.05f),
        size = Size(r * 1.36f, r * 1.18f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFFFCCBC),
        baseColor = Color(0xFFFF8A65),
        darkColor = Color(0xFFD84315)
    )

    // Front Paws
    drawPartOval(
        topLeft = Offset(cx - r * 0.52f, cy + r * 0.98f),
        size = Size(r * 0.44f, r * 0.32f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color.White,
        baseColor = Color(0xFFFFCCBC),
        darkColor = Color(0xFFFF8A65)
    )
    drawPartOval(
        topLeft = Offset(cx + r * 0.08f, cy + r * 0.98f),
        size = Size(r * 0.44f, r * 0.32f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color.White,
        baseColor = Color(0xFFFFCCBC),
        darkColor = Color(0xFFFF8A65)
    )

    // Pointed Ears
    val leftEar = Path().apply {
        moveTo(cx - r * 0.78f, cy - r * 0.35f)
        lineTo(cx - r * 0.92f, cy - r * 1.32f)
        lineTo(cx - r * 0.18f, cy - r * 0.92f)
        close()
    }
    val rightEar = Path().apply {
        moveTo(cx + r * 0.78f, cy - r * 0.35f)
        lineTo(cx + r * 0.92f, cy - r * 1.32f)
        lineTo(cx + r * 0.18f, cy - r * 0.92f)
        close()
    }
    drawPartPath(leftEar, Offset(cx - r * 0.6f, cy - r * 0.9f), r * 0.7f, isShadow, shadowColor, Color(0xFFFFAB91), Color(0xFFFF7043), Color(0xFFBF360C))
    drawPartPath(rightEar, Offset(cx + r * 0.6f, cy - r * 0.9f), r * 0.7f, isShadow, shadowColor, Color(0xFFFFAB91), Color(0xFFFF7043), Color(0xFFBF360C))

    // 3D Head
    drawPartCircle(
        center = Offset(cx, cy - r * 0.32f),
        radius = r * 0.82f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFFFCCBC),
        baseColor = Color(0xFFFF8A65),
        darkColor = Color(0xFFE64A19)
    )

    // Side Whiskers (Stick out past head silhouette!)
    drawPartLine(Offset(cx - r * 0.55f, cy - r * 0.25f), Offset(cx - r * 1.18f, cy - r * 0.38f), r * 0.06f, isShadow, shadowColor, Color(0xFF5D4037))
    drawPartLine(Offset(cx - r * 0.55f, cy - r * 0.12f), Offset(cx - r * 1.18f, cy - r * 0.12f), r * 0.06f, isShadow, shadowColor, Color(0xFF5D4037))
    drawPartLine(Offset(cx + r * 0.55f, cy - r * 0.25f), Offset(cx + r * 1.18f, cy - r * 0.38f), r * 0.06f, isShadow, shadowColor, Color(0xFF5D4037))
    drawPartLine(Offset(cx + r * 0.55f, cy - r * 0.12f), Offset(cx + r * 1.18f, cy - r * 0.12f), r * 0.06f, isShadow, shadowColor, Color(0xFF5D4037))

    if (!isShadow) {
        // Chest Patch
        drawOval(color = Color(0xFFFFF3E0), topLeft = Offset(cx - r * 0.38f, cy + r * 0.32f), size = Size(r * 0.76f, r * 0.65f))
        // Inner Ears
        val innerLeft = Path().apply {
            moveTo(cx - r * 0.68f, cy - r * 0.48f)
            lineTo(cx - r * 0.78f, cy - r * 1.12f)
            lineTo(cx - r * 0.3f, cy - r * 0.85f)
            close()
        }
        val innerRight = Path().apply {
            moveTo(cx + r * 0.68f, cy - r * 0.48f)
            lineTo(cx + r * 0.78f, cy - r * 1.12f)
            lineTo(cx + r * 0.3f, cy - r * 0.85f)
            close()
        }
        drawPath(innerLeft, color = Color(0xFFF48FB1))
        drawPath(innerRight, color = Color(0xFFF48FB1))

        // Eyes & Pink Nose
        drawEye3D(Offset(cx - r * 0.32f, cy - r * 0.42f), r * 0.17f)
        drawEye3D(Offset(cx + r * 0.32f, cy - r * 0.42f), r * 0.17f)
        drawCircle(color = Color(0xFFEC407A), radius = r * 0.11f, center = Offset(cx, cy - r * 0.2f))
    }
}

// 3. سمكة (FISH) - Full 3D tropical fish with dorsal fin, ventral fins, and forked tail fin
private fun DrawScope.draw3DFish(cx: Float, cy: Float, w: Float, h: Float, isShadow: Boolean, shadowColor: Color) {
    val r = w.coerceAtMost(h) * 0.34f

    // Top Dorsal Fin
    val dorsalFin = Path().apply {
        moveTo(cx - r * 0.35f, cy - r * 0.45f)
        quadraticTo(cx - r * 0.1f, cy - r * 1.25f, cx + r * 0.45f, cy - r * 0.5f)
        close()
    }
    drawPartPath(dorsalFin, Offset(cx, cy - r * 0.8f), r * 0.7f, isShadow, shadowColor, Color(0xFFFFD54F), Color(0xFFFF9800), Color(0xFFE65100))

    // Bottom Ventral Fin
    val ventralFin = Path().apply {
        moveTo(cx - r * 0.3f, cy + r * 0.45f)
        quadraticTo(cx - r * 0.05f, cy + r * 1.18f, cx + r * 0.38f, cy + r * 0.48f)
        close()
    }
    drawPartPath(ventralFin, Offset(cx, cy + r * 0.8f), r * 0.7f, isShadow, shadowColor, Color(0xFFFFD54F), Color(0xFFFF9800), Color(0xFFE65100))

    // Forked Tail Fin (Left)
    val tailFin = Path().apply {
        moveTo(cx - r * 0.72f, cy)
        lineTo(cx - r * 1.45f, cy - r * 0.78f)
        lineTo(cx - r * 1.15f, cy)
        lineTo(cx - r * 1.45f, cy + r * 0.78f)
        close()
    }
    drawPartPath(tailFin, Offset(cx - r * 1.1f, cy), r * 0.8f, isShadow, shadowColor, Color(0xFFFFD54F), Color(0xFFFF9800), Color(0xFFE65100))

    // Streamlined 3D Fish Body
    val bodyPath = Path().apply {
        moveTo(cx - r * 0.88f, cy)
        cubicTo(cx - r * 0.45f, cy - r * 0.92f, cx + r * 0.75f, cy - r * 0.88f, cx + r * 1.28f, cy)
        cubicTo(cx + r * 0.75f, cy + r * 0.88f, cx - r * 0.45f, cy + r * 0.92f, cx - r * 0.88f, cy)
        close()
    }
    drawPartPath(bodyPath, Offset(cx + r * 0.2f, cy - r * 0.2f), r * 1.4f, isShadow, shadowColor, Color(0xFF80DEEA), Color(0xFF00BCD4), Color(0xFF006064))

    if (!isShadow) {
        // Decorative Tropical Stripes inside body
        drawLine(color = Color(0xFFFFCA28), start = Offset(cx - r * 0.15f, cy - r * 0.55f), end = Offset(cx - r * 0.15f, cy + r * 0.55f), strokeWidth = r * 0.14f, cap = StrokeCap.Round)
        drawLine(color = Color(0xFFFFCA28), start = Offset(cx + r * 0.22f, cy - r * 0.55f), end = Offset(cx + r * 0.22f, cy + r * 0.55f), strokeWidth = r * 0.14f, cap = StrokeCap.Round)
        // Side Pectoral Fin
        val sideFin = Path().apply {
            moveTo(cx + r * 0.35f, cy + r * 0.1f)
            lineTo(cx + r * 0.02f, cy + r * 0.35f)
            lineTo(cx + r * 0.08f, cy - r * 0.05f)
            close()
        }
        drawPath(sideFin, color = Color(0xFFFFB300))
        drawEye3D(Offset(cx + r * 0.72f, cy - r * 0.15f), r * 0.18f)
    }
}

// 4. فيل (ELEPHANT) - Full 3D elephant with large ears, curved trunk, sturdy body, and legs
private fun DrawScope.draw3DElephant(cx: Float, cy: Float, w: Float, h: Float, isShadow: Boolean, shadowColor: Color) {
    val r = w.coerceAtMost(h) * 0.31f

    // Sturdy Elephant Body & Legs
    drawPartRoundRect(
        topLeft = Offset(cx - r * 0.65f, cy + r * 0.55f),
        size = Size(r * 0.38f, r * 0.72f),
        cornerRadius = CornerRadius(r * 0.15f, r * 0.15f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFB0BEC5),
        baseColor = Color(0xFF78909C),
        darkColor = Color(0xFF455A64)
    )
    drawPartRoundRect(
        topLeft = Offset(cx + r * 0.27f, cy + r * 0.55f),
        size = Size(r * 0.38f, r * 0.72f),
        cornerRadius = CornerRadius(r * 0.15f, r * 0.15f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFB0BEC5),
        baseColor = Color(0xFF78909C),
        darkColor = Color(0xFF455A64)
    )
    drawPartOval(
        topLeft = Offset(cx - r * 0.78f, cy + r * 0.02f),
        size = Size(r * 1.56f, r * 0.95f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFCFD8DC),
        baseColor = Color(0xFF90A4AE),
        darkColor = Color(0xFF546E7A)
    )

    // Giant Floppy Ears
    drawPartCircle(
        center = Offset(cx - r * 0.88f, cy - r * 0.35f),
        radius = r * 0.68f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFCFD8DC),
        baseColor = Color(0xFF78909C),
        darkColor = Color(0xFF455A64)
    )
    drawPartCircle(
        center = Offset(cx + r * 0.88f, cy - r * 0.35f),
        radius = r * 0.68f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFCFD8DC),
        baseColor = Color(0xFF78909C),
        darkColor = Color(0xFF455A64)
    )

    // 3D Head
    drawPartCircle(
        center = Offset(cx, cy - r * 0.35f),
        radius = r * 0.78f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFECEFF1),
        baseColor = Color(0xFF90A4AE),
        darkColor = Color(0xFF546E7A)
    )

    // Raised Curved Trunk (Curves clearly to the right so silhouette is unmistakable)
    val trunkPath = Path().apply {
        moveTo(cx - r * 0.18f, cy - r * 0.18f)
        cubicTo(cx - r * 0.18f, cy + r * 0.88f, cx + r * 0.95f, cy + r * 0.85f, cx + r * 1.15f, cy + r * 0.15f)
        lineTo(cx + r * 0.88f, cy + r * 0.12f)
        cubicTo(cx + r * 0.75f, cy + r * 0.55f, cx + r * 0.12f, cy + r * 0.55f, cx + r * 0.15f, cy - r * 0.18f)
        close()
    }
    drawPartPath(trunkPath, Offset(cx + r * 0.4f, cy + r * 0.3f), r, isShadow, shadowColor, Color(0xFFECEFF1), Color(0xFF90A4AE), Color(0xFF546E7A))

    if (!isShadow) {
        // Pink Inner Ears
        drawCircle(color = Color(0xFFF8BBD0), radius = r * 0.42f, center = Offset(cx - r * 0.92f, cy - r * 0.35f))
        drawCircle(color = Color(0xFFF8BBD0), radius = r * 0.42f, center = Offset(cx + r * 0.92f, cy - r * 0.35f))
        // Eyes
        drawEye3D(Offset(cx - r * 0.32f, cy - r * 0.48f), r * 0.15f)
        drawEye3D(Offset(cx + r * 0.32f, cy - r * 0.48f), r * 0.15f)
    }
}

// 5. حمار وحشي (ZEBRA) - Full 3D standing zebra with legs, neck, spiky mane, ears, tail, and stripes
private fun DrawScope.draw3DZebra(cx: Float, cy: Float, w: Float, h: Float, isShadow: Boolean, shadowColor: Color) {
    val r = w.coerceAtMost(h) * 0.32f

    // Four Legs
    drawPartRoundRect(Offset(cx - r * 0.75f, cy + r * 0.45f), Size(r * 0.24f, r * 0.82f), CornerRadius(r * 0.1f, r * 0.1f), isShadow, shadowColor, Color.White, Color(0xFFE0E0E0), Color(0xFF424242))
    drawPartRoundRect(Offset(cx - r * 0.42f, cy + r * 0.45f), Size(r * 0.24f, r * 0.82f), CornerRadius(r * 0.1f, r * 0.1f), isShadow, shadowColor, Color.White, Color(0xFFE0E0E0), Color(0xFF424242))
    drawPartRoundRect(Offset(cx + r * 0.15f, cy + r * 0.45f), Size(r * 0.24f, r * 0.82f), CornerRadius(r * 0.1f, r * 0.1f), isShadow, shadowColor, Color.White, Color(0xFFE0E0E0), Color(0xFF424242))
    drawPartRoundRect(Offset(cx + r * 0.48f, cy + r * 0.45f), Size(r * 0.24f, r * 0.82f), CornerRadius(r * 0.1f, r * 0.1f), isShadow, shadowColor, Color.White, Color(0xFFE0E0E0), Color(0xFF424242))

    // Tail (Left)
    drawPartLine(Offset(cx - r * 0.78f, cy + r * 0.1f), Offset(cx - r * 1.25f, cy + r * 0.55f), r * 0.12f, isShadow, shadowColor, Color(0xFF212121))

    // Spiky Mane Ridge along neck
    val manePath = Path().apply {
        moveTo(cx + r * 0.05f, cy - r * 0.05f)
        lineTo(cx - r * 0.05f, cy - r * 0.85f)
        lineTo(cx + r * 0.25f, cy - r * 1.15f)
        lineTo(cx + r * 0.35f, cy - r * 0.15f)
        close()
    }
    drawPartPath(manePath, Offset(cx + r * 0.1f, cy - r * 0.6f), r * 0.7f, isShadow, shadowColor, Color(0xFF616161), Color(0xFF212121), Color.Black)

    // Ears
    drawPartOval(Offset(cx + r * 0.15f, cy - r * 1.35f), Size(r * 0.22f, r * 0.45f), isShadow, shadowColor, Color.White, Color(0xFFE0E0E0), Color(0xFF757575))
    drawPartOval(Offset(cx + r * 0.42f, cy - r * 1.35f), Size(r * 0.22f, r * 0.45f), isShadow, shadowColor, Color.White, Color(0xFFE0E0E0), Color(0xFF757575))

    // Horse-like Body
    drawPartRoundRect(
        topLeft = Offset(cx - r * 0.85f, cy - r * 0.12f),
        size = Size(r * 1.65f, r * 0.78f),
        cornerRadius = CornerRadius(r * 0.38f, r * 0.38f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color.White,
        baseColor = Color(0xFFF5F5F5),
        darkColor = Color(0xFFBDBDBD)
    )

    // Neck & Head
    val neckHeadPath = Path().apply {
        moveTo(cx + r * 0.15f, cy + r * 0.15f)
        lineTo(cx + r * 0.15f, cy - r * 0.95f)
        lineTo(cx + r * 0.95f, cy - r * 0.75f)
        lineTo(cx + r * 1.18f, cy - r * 0.45f)
        lineTo(cx + r * 0.72f, cy - r * 0.32f)
        lineTo(cx + r * 0.68f, cy + r * 0.15f)
        close()
    }
    drawPartPath(neckHeadPath, Offset(cx + r * 0.5f, cy - r * 0.5f), r, isShadow, shadowColor, Color.White, Color(0xFFF5F5F5), Color(0xFFBDBDBD))

    // Dark Snout tip
    drawPartCircle(
        center = Offset(cx + r * 1.02f, cy - r * 0.52f),
        radius = r * 0.22f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFF616161),
        baseColor = Color(0xFF212121),
        darkColor = Color.Black
    )

    if (!isShadow) {
        // Bold Zebra Black Stripes strictly inside body & neck
        drawLine(color = Color(0xFF212121), start = Offset(cx - r * 0.55f, cy - r * 0.08f), end = Offset(cx - r * 0.55f, cy + r * 0.55f), strokeWidth = r * 0.13f, cap = StrokeCap.Round)
        drawLine(color = Color(0xFF212121), start = Offset(cx - r * 0.22f, cy - r * 0.08f), end = Offset(cx - r * 0.22f, cy + r * 0.55f), strokeWidth = r * 0.13f, cap = StrokeCap.Round)
        drawLine(color = Color(0xFF212121), start = Offset(cx + r * 0.1f, cy - r * 0.08f), end = Offset(cx + r * 0.1f, cy + r * 0.55f), strokeWidth = r * 0.13f, cap = StrokeCap.Round)
        drawLine(color = Color(0xFF212121), start = Offset(cx + r * 0.42f, cy - r * 0.08f), end = Offset(cx + r * 0.42f, cy + r * 0.55f), strokeWidth = r * 0.13f, cap = StrokeCap.Round)
        drawLine(color = Color(0xFF212121), start = Offset(cx + r * 0.22f, cy - r * 0.45f), end = Offset(cx + r * 0.58f, cy - r * 0.35f), strokeWidth = r * 0.11f, cap = StrokeCap.Round)
        drawLine(color = Color(0xFF212121), start = Offset(cx + r * 0.22f, cy - r * 0.68f), end = Offset(cx + r * 0.62f, cy - r * 0.58f), strokeWidth = r * 0.11f, cap = StrokeCap.Round)
        drawEye3D(Offset(cx + r * 0.6f, cy - r * 0.72f), r * 0.13f)
    }
}

// 6. أسد (LION) - Full 3D lion with scalloped flower-like mane, sitting body, paws, and tufted tail
private fun DrawScope.draw3DLion(cx: Float, cy: Float, w: Float, h: Float, isShadow: Boolean, shadowColor: Color) {
    val r = w.coerceAtMost(h) * 0.31f

    // Tufted Lion Tail (Right)
    val tailCurve = Path().apply {
        moveTo(cx + r * 0.45f, cy + r * 0.85f)
        quadraticTo(cx + r * 1.25f, cy + r * 0.75f, cx + r * 1.18f, cy + r * 0.15f)
        lineTo(cx + r * 1.02f, cy + r * 0.15f)
        quadraticTo(cx + r * 1.05f, cy + r * 0.62f, cx + r * 0.4f, cy + r * 0.68f)
        close()
    }
    drawPartPath(tailCurve, Offset(cx + r * 0.9f, cy + r * 0.5f), r * 0.7f, isShadow, shadowColor, Color(0xFFFFD54F), Color(0xFFFFB300), Color(0xFFE65100))
    drawPartCircle(Offset(cx + r * 1.12f, cy + r * 0.08f), r * 0.22f, isShadow, shadowColor, Color(0xFFFF8A65), Color(0xFFD84315), Color(0xFFBF360C))

    // Sitting Body & Paws
    drawPartOval(
        topLeft = Offset(cx - r * 0.68f, cy + r * 0.18f),
        size = Size(r * 1.36f, r * 1.08f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFFFE082),
        baseColor = Color(0xFFFFB300),
        darkColor = Color(0xFFE65100)
    )
    drawPartCircle(Offset(cx - r * 0.35f, cy + r * 1.15f), r * 0.24f, isShadow, shadowColor, Color(0xFFFFECB3), Color(0xFFFFCA28), Color(0xFFFF8F00))
    drawPartCircle(Offset(cx + r * 0.35f, cy + r * 1.15f), r * 0.24f, isShadow, shadowColor, Color(0xFFFFECB3), Color(0xFFFFCA28), Color(0xFFFF8F00))

    // Scalloped 3D Mane Lobes around head (makes Lion silhouette unmistakable!)
    val headCy = cy - r * 0.28f
    for (i in 0 until 12) {
        val angle = i * (2 * Math.PI / 12)
        val lobeX = cx + (r * 0.78f * cos(angle)).toFloat()
        val lobeY = headCy + (r * 0.78f * sin(angle)).toFloat()
        drawPartCircle(
            center = Offset(lobeX, lobeY),
            radius = r * 0.36f,
            isShadow = isShadow,
            shadowColor = shadowColor,
            lightColor = Color(0xFFFF8A65),
            baseColor = Color(0xFFE65100),
            darkColor = Color(0xFFBF360C)
        )
    }
    drawPartCircle(
        center = Offset(cx, headCy),
        radius = r * 0.92f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFFF8A65),
        baseColor = Color(0xFFE65100),
        darkColor = Color(0xFFBF360C)
    )

    // Ears
    drawPartCircle(Offset(cx - r * 0.68f, headCy - r * 0.68f), r * 0.28f, isShadow, shadowColor, Color(0xFFFFE082), Color(0xFFFFB300), Color(0xFFE65100))
    drawPartCircle(Offset(cx + r * 0.68f, headCy - r * 0.68f), r * 0.28f, isShadow, shadowColor, Color(0xFFFFE082), Color(0xFFFFB300), Color(0xFFE65100))

    // 3D Face
    drawPartCircle(
        center = Offset(cx, headCy),
        radius = r * 0.74f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFFFF59D),
        baseColor = Color(0xFFFFCA28),
        darkColor = Color(0xFFFF8F00)
    )

    if (!isShadow) {
        // Snout & Nose
        drawCircle(color = Color(0xFFFFF8E1), radius = r * 0.34f, center = Offset(cx, headCy + r * 0.2f))
        drawCircle(color = Color(0xFF3E2723), radius = r * 0.14f, center = Offset(cx, headCy + r * 0.1f))
        drawEye3D(Offset(cx - r * 0.28f, headCy - r * 0.15f), r * 0.15f)
        drawEye3D(Offset(cx + r * 0.28f, headCy - r * 0.15f), r * 0.15f)
    }
}

// 7. زرافة (GIRAFFE) - Full 3D giraffe with long neck, ossicones (horns), side ears, body, and legs
private fun DrawScope.draw3DGiraffe(cx: Float, cy: Float, w: Float, h: Float, isShadow: Boolean, shadowColor: Color) {
    val r = w.coerceAtMost(h) * 0.31f

    // Four Long Legs
    drawPartRoundRect(Offset(cx - r * 0.72f, cy + r * 0.55f), Size(r * 0.2f, r * 0.85f), CornerRadius(r * 0.08f, r * 0.08f), isShadow, shadowColor, Color(0xFFFFE082), Color(0xFFFFB300), Color(0xFF8D6E63))
    drawPartRoundRect(Offset(cx - r * 0.42f, cy + r * 0.55f), Size(r * 0.2f, r * 0.85f), CornerRadius(r * 0.08f, r * 0.08f), isShadow, shadowColor, Color(0xFFFFE082), Color(0xFFFFB300), Color(0xFF8D6E63))
    drawPartRoundRect(Offset(cx + r * 0.05f, cy + r * 0.55f), Size(r * 0.2f, r * 0.85f), CornerRadius(r * 0.08f, r * 0.08f), isShadow, shadowColor, Color(0xFFFFE082), Color(0xFFFFB300), Color(0xFF8D6E63))
    drawPartRoundRect(Offset(cx + r * 0.35f, cy + r * 0.55f), Size(r * 0.2f, r * 0.85f), CornerRadius(r * 0.08f, r * 0.08f), isShadow, shadowColor, Color(0xFFFFE082), Color(0xFFFFB300), Color(0xFF8D6E63))

    // Giraffe Body
    drawPartRoundRect(
        topLeft = Offset(cx - r * 0.8f, cy + r * 0.08f),
        size = Size(r * 1.42f, r * 0.68f),
        cornerRadius = CornerRadius(r * 0.32f, r * 0.32f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFFFE082),
        baseColor = Color(0xFFFFB300),
        darkColor = Color(0xFFF57C00)
    )

    // Tall Long Neck
    drawPartRoundRect(
        topLeft = Offset(cx + r * 0.08f, cy - r * 0.95f),
        size = Size(r * 0.44f, r * 1.25f),
        cornerRadius = CornerRadius(r * 0.2f, r * 0.2f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFFFE082),
        baseColor = Color(0xFFFFB300),
        darkColor = Color(0xFFF57C00)
    )

    // Two Ossicones (Horns) on Top of Head
    val headCx = cx + r * 0.35f
    val headCy = cy - r * 0.98f
    drawPartLine(Offset(headCx - r * 0.16f, headCy - r * 0.2f), Offset(headCx - r * 0.24f, headCy - r * 0.65f), r * 0.1f, isShadow, shadowColor, Color(0xFF6D4C41))
    drawPartLine(Offset(headCx + r * 0.16f, headCy - r * 0.2f), Offset(headCx + r * 0.24f, headCy - r * 0.65f), r * 0.1f, isShadow, shadowColor, Color(0xFF6D4C41))
    drawPartCircle(Offset(headCx - r * 0.24f, headCy - r * 0.68f), r * 0.14f, isShadow, shadowColor, Color(0xFF8D6E63), Color(0xFF5D4037), Color(0xFF3E2723))
    drawPartCircle(Offset(headCx + r * 0.24f, headCy - r * 0.68f), r * 0.14f, isShadow, shadowColor, Color(0xFF8D6E63), Color(0xFF5D4037), Color(0xFF3E2723))

    // Side Ears
    drawPartOval(Offset(headCx - r * 0.78f, headCy - r * 0.25f), Size(r * 0.48f, r * 0.24f), isShadow, shadowColor, Color(0xFFFFE082), Color(0xFFFFB300), Color(0xFFF57C00))
    drawPartOval(Offset(headCx + r * 0.3f, headCy - r * 0.25f), Size(r * 0.48f, r * 0.24f), isShadow, shadowColor, Color(0xFFFFE082), Color(0xFFFFB300), Color(0xFFF57C00))

    // Head & Muzzle
    drawPartCircle(Offset(headCx, headCy), r * 0.44f, isShadow, shadowColor, Color(0xFFFFE082), Color(0xFFFFB300), Color(0xFFF57C00))
    drawPartOval(Offset(headCx + r * 0.05f, headCy - r * 0.12f), Size(r * 0.58f, r * 0.38f), isShadow, shadowColor, Color(0xFFFFF8E1), Color(0xFFFFCC80), Color(0xFFFFA726))

    if (!isShadow) {
        // Brown Spots strictly inside body & neck
        drawCircle(color = Color(0xFF8D6E63), radius = r * 0.14f, center = Offset(cx - r * 0.45f, cy + r * 0.35f))
        drawCircle(color = Color(0xFF8D6E63), radius = r * 0.16f, center = Offset(cx - r * 0.05f, cy + r * 0.42f))
        drawCircle(color = Color(0xFF8D6E63), radius = r * 0.12f, center = Offset(cx + r * 0.28f, cy + r * 0.32f))
        drawCircle(color = Color(0xFF8D6E63), radius = r * 0.11f, center = Offset(cx + r * 0.28f, cy - r * 0.25f))
        drawCircle(color = Color(0xFF8D6E63), radius = r * 0.11f, center = Offset(cx + r * 0.32f, cy - r * 0.58f))
        drawEye3D(Offset(headCx + r * 0.08f, headCy - r * 0.08f), r * 0.11f)
    }
}

// 8. كلب (DOG) - Full 3D puppy with long floppy ears, sitting body, wagging tail, and paws
private fun DrawScope.draw3DDog(cx: Float, cy: Float, w: Float, h: Float, isShadow: Boolean, shadowColor: Color) {
    val r = w.coerceAtMost(h) * 0.32f

    // Wagging Tail (Left)
    val tailPath = Path().apply {
        moveTo(cx - r * 0.5f, cy + r * 0.72f)
        quadraticTo(cx - r * 1.28f, cy + r * 0.55f, cx - r * 1.15f, cy + r * 0.05f)
        quadraticTo(cx - r * 0.95f, cy + r * 0.35f, cx - r * 0.42f, cy + r * 0.5f)
        close()
    }
    drawPartPath(tailPath, Offset(cx - r * 0.9f, cy + r * 0.4f), r * 0.6f, isShadow, shadowColor, Color(0xFFD7CCC8), Color(0xFF8D6E63), Color(0xFF4E342E))

    // Sitting Puppy Body & Paws
    drawPartOval(
        topLeft = Offset(cx - r * 0.68f, cy + r * 0.08f),
        size = Size(r * 1.36f, r * 1.12f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFD7CCC8),
        baseColor = Color(0xFF8D6E63),
        darkColor = Color(0xFF4E342E)
    )
    drawPartOval(Offset(cx - r * 0.55f, cy + r * 0.98f), Size(r * 0.46f, r * 0.32f), isShadow, shadowColor, Color.White, Color(0xFFD7CCC8), Color(0xFF8D6E63))
    drawPartOval(Offset(cx + r * 0.09f, cy + r * 0.98f), Size(r * 0.46f, r * 0.32f), isShadow, shadowColor, Color.White, Color(0xFFD7CCC8), Color(0xFF8D6E63))

    // Long Hanging Floppy Ears
    drawPartOval(
        topLeft = Offset(cx - r * 1.25f, cy - r * 0.65f),
        size = Size(r * 0.58f, r * 1.1f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFF6D4C41),
        baseColor = Color(0xFF4E342E),
        darkColor = Color(0xFF271815)
    )
    drawPartOval(
        topLeft = Offset(cx + r * 0.67f, cy - r * 0.65f),
        size = Size(r * 0.58f, r * 1.1f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFF6D4C41),
        baseColor = Color(0xFF4E342E),
        darkColor = Color(0xFF271815)
    )

    // 3D Head
    drawPartCircle(
        center = Offset(cx, cy - r * 0.35f),
        radius = r * 0.82f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFD7CCC8),
        baseColor = Color(0xFF8D6E63),
        darkColor = Color(0xFF5D4037)
    )

    if (!isShadow) {
        // White Belly & Snout
        drawOval(color = Color(0xFFEFEBE9), topLeft = Offset(cx - r * 0.38f, cy + r * 0.35f), size = Size(r * 0.76f, r * 0.65f))
        drawOval(color = Color(0xFFFFF8E1), topLeft = Offset(cx - r * 0.42f, cy - r * 0.25f), size = Size(r * 0.84f, r * 0.58f))
        // Pink Tongue & Nose
        drawOval(color = Color(0xFFF06292), topLeft = Offset(cx - r * 0.14f, cy + r * 0.12f), size = Size(r * 0.28f, r * 0.28f))
        drawCircle(color = Color(0xFF212121), radius = r * 0.15f, center = Offset(cx, cy - r * 0.12f))
        drawEye3D(Offset(cx - r * 0.32f, cy - r * 0.48f), r * 0.16f)
        drawEye3D(Offset(cx + r * 0.32f, cy - r * 0.48f), r * 0.16f)
    }
}

// 9. دب (BEAR) - Full 3D teddy/brown bear with round ears, outstretched arms, and chubby legs
private fun DrawScope.draw3DBear(cx: Float, cy: Float, w: Float, h: Float, isShadow: Boolean, shadowColor: Color) {
    val r = w.coerceAtMost(h) * 0.31f

    // Outstretched Arms
    drawPartOval(Offset(cx - r * 1.25f, cy + r * 0.05f), Size(r * 0.65f, r * 0.45f), isShadow, shadowColor, Color(0xFFA1887F), Color(0xFF6D4C41), Color(0xFF3E2723))
    drawPartOval(Offset(cx + r * 0.6f, cy + r * 0.05f), Size(r * 0.65f, r * 0.45f), isShadow, shadowColor, Color(0xFFA1887F), Color(0xFF6D4C41), Color(0xFF3E2723))

    // Chubby Legs/Feet
    drawPartCircle(Offset(cx - r * 0.52f, cy + r * 1.02f), r * 0.34f, isShadow, shadowColor, Color(0xFFA1887F), Color(0xFF5D4037), Color(0xFF3E2723))
    drawPartCircle(Offset(cx + r * 0.52f, cy + r * 1.02f), r * 0.34f, isShadow, shadowColor, Color(0xFFA1887F), Color(0xFF5D4037), Color(0xFF3E2723))

    // Plump Bear Belly
    drawPartCircle(
        center = Offset(cx, cy + r * 0.48f),
        radius = r * 0.78f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFA1887F),
        baseColor = Color(0xFF6D4C41),
        darkColor = Color(0xFF3E2723)
    )

    // Round Bear Ears
    drawPartCircle(Offset(cx - r * 0.68f, cy - r * 0.98f), r * 0.36f, isShadow, shadowColor, Color(0xFFA1887F), Color(0xFF5D4037), Color(0xFF3E2723))
    drawPartCircle(Offset(cx + r * 0.68f, cy - r * 0.98f), r * 0.36f, isShadow, shadowColor, Color(0xFFA1887F), Color(0xFF5D4037), Color(0xFF3E2723))

    // 3D Bear Head
    drawPartCircle(
        center = Offset(cx, cy - r * 0.42f),
        radius = r * 0.78f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFA1887F),
        baseColor = Color(0xFF6D4C41),
        darkColor = Color(0xFF3E2723)
    )

    if (!isShadow) {
        // Inner Ears, Belly Patch, Muzzle
        drawCircle(color = Color(0xFFD7CCC8), radius = r * 0.18f, center = Offset(cx - r * 0.68f, cy - r * 0.98f))
        drawCircle(color = Color(0xFFD7CCC8), radius = r * 0.18f, center = Offset(cx + r * 0.68f, cy - r * 0.98f))
        drawCircle(color = Color(0xFFD7CCC8), radius = r * 0.48f, center = Offset(cx, cy + r * 0.52f))
        drawCircle(color = Color(0xFFD7CCC8), radius = r * 0.36f, center = Offset(cx, cy - r * 0.25f))
        drawCircle(color = Color(0xFF212121), radius = r * 0.14f, center = Offset(cx, cy - r * 0.32f))
        drawEye3D(Offset(cx - r * 0.3f, cy - r * 0.55f), r * 0.15f)
        drawEye3D(Offset(cx + r * 0.3f, cy - r * 0.55f), r * 0.15f)
    }
}

// 10. بطريق (PENGUIN) - Full 3D penguin with outstretched flippers, webbed feet, and oval body
private fun DrawScope.draw3DPenguin(cx: Float, cy: Float, w: Float, h: Float, isShadow: Boolean, shadowColor: Color) {
    val r = w.coerceAtMost(h) * 0.33f

    // Orange Webbed Feet at Bottom
    drawPartOval(Offset(cx - r * 0.62f, cy + r * 0.95f), Size(r * 0.52f, r * 0.28f), isShadow, shadowColor, Color(0xFFFFD54F), Color(0xFFFF9800), Color(0xFFE65100))
    drawPartOval(Offset(cx + r * 0.1f, cy + r * 0.95f), Size(r * 0.52f, r * 0.28f), isShadow, shadowColor, Color(0xFFFFD54F), Color(0xFFFF9800), Color(0xFFE65100))

    // Outstretched Flippers/Wings (Left & Right)
    val leftFlipper = Path().apply {
        moveTo(cx - r * 0.65f, cy - r * 0.25f)
        quadraticTo(cx - r * 1.38f, cy + r * 0.15f, cx - r * 1.15f, cy + r * 0.65f)
        quadraticTo(cx - r * 0.8f, cy + r * 0.5f, cx - r * 0.65f, cy + r * 0.25f)
        close()
    }
    val rightFlipper = Path().apply {
        moveTo(cx + r * 0.65f, cy - r * 0.25f)
        quadraticTo(cx + r * 1.38f, cy + r * 0.15f, cx + r * 1.15f, cy + r * 0.65f)
        quadraticTo(cx + r * 0.8f, cy + r * 0.5f, cx + r * 0.65f, cy + r * 0.25f)
        close()
    }
    drawPartPath(leftFlipper, Offset(cx - r, cy + r * 0.2f), r * 0.6f, isShadow, shadowColor, Color(0xFF546E7A), Color(0xFF263238), Color(0xFF0F172A))
    drawPartPath(rightFlipper, Offset(cx + r, cy + r * 0.2f), r * 0.6f, isShadow, shadowColor, Color(0xFF546E7A), Color(0xFF263238), Color(0xFF0F172A))

    // Main Penguin Body & Head
    drawPartOval(
        topLeft = Offset(cx - r * 0.78f, cy - r * 0.45f),
        size = Size(r * 1.56f, r * 1.55f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFF546E7A),
        baseColor = Color(0xFF263238),
        darkColor = Color(0xFF0F172A)
    )
    drawPartCircle(
        center = Offset(cx, cy - r * 0.52f),
        radius = r * 0.68f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFF546E7A),
        baseColor = Color(0xFF263238),
        darkColor = Color(0xFF0F172A)
    )

    if (!isShadow) {
        // White Belly & Face Patch
        drawOval(color = Color.White, topLeft = Offset(cx - r * 0.55f, cy - r * 0.15f), size = Size(r * 1.1f, r * 1.12f))
        drawEye3D(Offset(cx - r * 0.26f, cy - r * 0.58f), r * 0.15f)
        drawEye3D(Offset(cx + r * 0.26f, cy - r * 0.58f), r * 0.15f)
        // Orange Beak
        val beak = Path().apply {
            moveTo(cx - r * 0.22f, cy - r * 0.42f)
            lineTo(cx + r * 0.22f, cy - r * 0.42f)
            lineTo(cx, cy - r * 0.14f)
            close()
        }
        drawPath(beak, color = Color(0xFFFF9800))
    }
}

// 11. سنجاب (SQUIRREL) - Full 3D squirrel with giant S-curved bushy tail, tufted ears, and paws
private fun DrawScope.draw3DSquirrel(cx: Float, cy: Float, w: Float, h: Float, isShadow: Boolean, shadowColor: Color) {
    val r = w.coerceAtMost(h) * 0.32f

    // Giant Fluffy Bushy Tail on the Right
    val bushyTail = Path().apply {
        moveTo(cx - r * 0.05f, cy + r * 0.85f)
        cubicTo(cx + r * 1.45f, cy + r * 1.05f, cx + r * 1.55f, cy - r * 1.15f, cx + r * 0.55f, cy - r * 1.15f)
        cubicTo(cx + r * 0.05f, cy - r * 1.15f, cx + r * 0.12f, cy - r * 0.35f, cx + r * 0.42f, cy - r * 0.15f)
        cubicTo(cx + r * 0.55f, cy + r * 0.1f, cx + r * 0.2f, cy + r * 0.55f, cx - r * 0.05f, cy + r * 0.55f)
        close()
    }
    drawPartPath(bushyTail, Offset(cx + r * 0.7f, cy - r * 0.2f), r * 1.2f, isShadow, shadowColor, Color(0xFFFFAB91), Color(0xFFD84315), Color(0xFF870000))

    // Feet
    drawPartOval(Offset(cx - r * 0.82f, cy + r * 0.88f), Size(r * 0.58f, r * 0.28f), isShadow, shadowColor, Color(0xFFFFAB91), Color(0xFFE64A19), Color(0xFFBF360C))

    // Sitting Squirrel Body
    drawPartOval(
        topLeft = Offset(cx - r * 0.78f, cy + r * 0.02f),
        size = Size(r * 1.05f, r * 1.02f),
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFFFCCBC),
        baseColor = Color(0xFFFF7043),
        darkColor = Color(0xFFD84315)
    )

    // Pointed Squirrel Ears
    drawPartOval(Offset(cx - r * 0.62f, cy - r * 1.15f), Size(r * 0.26f, r * 0.52f), isShadow, shadowColor, Color(0xFFFFAB91), Color(0xFFE64A19), Color(0xFFBF360C))
    drawPartOval(Offset(cx - r * 0.25f, cy - r * 1.15f), Size(r * 0.26f, r * 0.52f), isShadow, shadowColor, Color(0xFFFFAB91), Color(0xFFE64A19), Color(0xFFBF360C))

    // Squirrel Head
    drawPartCircle(
        center = Offset(cx - r * 0.38f, cy - r * 0.42f),
        radius = r * 0.56f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFFFCCBC),
        baseColor = Color(0xFFFF7043),
        darkColor = Color(0xFFD84315)
    )

    // Paws & Acorn in Front (Left)
    drawPartCircle(
        center = Offset(cx - r * 0.88f, cy + r * 0.22f),
        radius = r * 0.24f,
        isShadow = isShadow,
        shadowColor = shadowColor,
        lightColor = Color(0xFFD7CCC8),
        baseColor = Color(0xFF8D6E63),
        darkColor = Color(0xFF4E342E)
    )

    if (!isShadow) {
        // Cream Belly & Eye
        drawOval(color = Color(0xFFFFF3E0), topLeft = Offset(cx - r * 0.65f, cy + r * 0.2f), size = Size(r * 0.58f, r * 0.65f))
        drawEye3D(Offset(cx - r * 0.52f, cy - r * 0.48f), r * 0.15f)
        drawCircle(color = Color(0xFF3E2723), radius = r * 0.09f, center = Offset(cx - r * 0.88f, cy - r * 0.35f))
    }
}

// ---------------------------- SHAPES & OBJECTS ----------------------------

private fun DrawScope.draw3DSquare(
    cx: Float, cy: Float, w: Float, h: Float,
    isShadow: Boolean, shadowColor: Color
) {
    val size = minOf(w, h) * 0.65f
    val left = cx - size / 2f
    val top = cy - size / 2f
    val cr = CornerRadius(size * 0.18f, size * 0.18f)

    if (isShadow) {
        drawRoundRect(color = shadowColor, topLeft = Offset(left, top), size = Size(size, size), cornerRadius = cr)
    } else {
        val brush = Brush.linearGradient(
            colors = listOf(Color(0xFFFF8A65), Color(0xFFE64A19), Color(0xFFBF360C)),
            start = Offset(left, top),
            end = Offset(left + size, top + size)
        )
        drawRoundRect(brush = brush, topLeft = Offset(left, top), size = Size(size, size), cornerRadius = cr)
        // Glossy bevel highlight
        drawRoundRect(
            color = Color.White.copy(alpha = 0.35f),
            topLeft = Offset(left + size * 0.08f, top + size * 0.08f),
            size = Size(size * 0.84f, size * 0.22f),
            cornerRadius = CornerRadius(size * 0.1f, size * 0.1f)
        )
    }
}

private fun DrawScope.draw3DTriangle(
    cx: Float, cy: Float, w: Float, h: Float,
    isShadow: Boolean, shadowColor: Color
) {
    val path = Path().apply {
        moveTo(cx, cy - h * 0.36f)
        lineTo(cx + w * 0.36f, cy + h * 0.32f)
        lineTo(cx - w * 0.36f, cy + h * 0.32f)
        close()
    }

    if (isShadow) {
        drawPath(path, color = shadowColor)
    } else {
        val brush = Brush.verticalGradient(
            colors = listOf(Color(0xFFE1BEE7), Color(0xFFAB47BC), Color(0xFF6A1B9A)),
            startY = cy - h * 0.36f,
            endY = cy + h * 0.32f
        )
        drawPath(path, brush = brush)
        // Glossy highlight line
        drawCircle(color = Color.White.copy(alpha = 0.45f), radius = minOf(w, h) * 0.08f, center = Offset(cx, cy - h * 0.12f))
    }
}

private fun DrawScope.draw3DRectangle(
    cx: Float, cy: Float, w: Float, h: Float,
    isShadow: Boolean, shadowColor: Color
) {
    val rw = w * 0.75f
    val rh = h * 0.48f
    val left = cx - rw / 2f
    val top = cy - rh / 2f
    val cr = CornerRadius(rh * 0.22f, rh * 0.22f)

    if (isShadow) {
        drawRoundRect(color = shadowColor, topLeft = Offset(left, top), size = Size(rw, rh), cornerRadius = cr)
    } else {
        val brush = Brush.linearGradient(
            colors = listOf(Color(0xFFA5D6A7), Color(0xFF4CAF50), Color(0xFF1B5E20)),
            start = Offset(left, top),
            end = Offset(left + rw, top + rh)
        )
        drawRoundRect(brush = brush, topLeft = Offset(left, top), size = Size(rw, rh), cornerRadius = cr)
        // Highlight
        drawRoundRect(
            color = Color.White.copy(alpha = 0.35f),
            topLeft = Offset(left + rw * 0.08f, top + rh * 0.1f),
            size = Size(rw * 0.84f, rh * 0.26f),
            cornerRadius = CornerRadius(rh * 0.1f, rh * 0.1f)
        )
    }
}

private fun DrawScope.draw3DCircle(
    cx: Float, cy: Float, w: Float, h: Float,
    isShadow: Boolean, shadowColor: Color
) {
    val radius = minOf(w, h) * 0.35f

    if (isShadow) {
        drawCircle(color = shadowColor, center = Offset(cx, cy), radius = radius)
    } else {
        val brush = Brush.radialGradient(
            colors = listOf(Color(0xFFFF8A80), Color(0xFFF44336), Color(0xFFB71C1C)),
            center = Offset(cx - radius * 0.35f, cy - radius * 0.35f),
            radius = radius * 1.35f
        )
        drawCircle(brush = brush, center = Offset(cx, cy), radius = radius)
        drawCircle(color = Color.White.copy(alpha = 0.45f), radius = radius * 0.25f, center = Offset(cx - radius * 0.35f, cy - radius * 0.35f))
    }
}

private fun DrawScope.draw3DTree(
    cx: Float, cy: Float, w: Float, h: Float,
    isShadow: Boolean, shadowColor: Color
) {
    val trunkLeft = cx - w * 0.08f
    val trunkTop = cy + h * 0.06f
    val trunkWidth = w * 0.16f
    val trunkHeight = h * 0.32f
    val trunkCr = CornerRadius(8f, 8f)

    if (isShadow) {
        drawRoundRect(color = shadowColor, topLeft = Offset(trunkLeft, trunkTop), size = Size(trunkWidth, trunkHeight), cornerRadius = trunkCr)
        drawCircle(color = shadowColor, center = Offset(cx, cy - h * 0.16f), radius = w * 0.28f)
        drawCircle(color = shadowColor, center = Offset(cx - w * 0.18f, cy - h * 0.06f), radius = w * 0.22f)
        drawCircle(color = shadowColor, center = Offset(cx + w * 0.18f, cy - h * 0.06f), radius = w * 0.22f)
    } else {
        // Brown trunk
        val trunkBrush = Brush.verticalGradient(
            colors = listOf(Color(0xFFA1887F), Color(0xFF6D4C41), Color(0xFF3E2723)),
            startY = trunkTop, endY = trunkTop + trunkHeight
        )
        drawRoundRect(brush = trunkBrush, topLeft = Offset(trunkLeft, trunkTop), size = Size(trunkWidth, trunkHeight), cornerRadius = trunkCr)

        // Green foliage
        val foliageBrush = Brush.radialGradient(
            colors = listOf(Color(0xFF81C784), Color(0xFF388E3C), Color(0xFF1B5E20)),
            center = Offset(cx - w * 0.1f, cy - h * 0.2f),
            radius = w * 0.45f
        )
        drawCircle(brush = foliageBrush, center = Offset(cx - w * 0.18f, cy - h * 0.06f), radius = w * 0.22f)
        drawCircle(brush = foliageBrush, center = Offset(cx + w * 0.18f, cy - h * 0.06f), radius = w * 0.22f)
        drawCircle(brush = foliageBrush, center = Offset(cx, cy - h * 0.16f), radius = w * 0.28f)

        // Highlight
        drawCircle(color = Color.White.copy(alpha = 0.3f), radius = w * 0.08f, center = Offset(cx - w * 0.12f, cy - h * 0.24f))
    }
}

private fun DrawScope.draw3DCar(
    cx: Float, cy: Float, w: Float, h: Float,
    isShadow: Boolean, shadowColor: Color
) {
    val bodyLeft = cx - w * 0.38f
    val bodyTop = cy - h * 0.02f
    val bodyW = w * 0.76f
    val bodyH = h * 0.26f
    val bodyCr = CornerRadius(16f, 16f)

    val roofLeft = cx - w * 0.22f
    val roofTop = cy - h * 0.24f
    val roofW = w * 0.44f
    val roofH = h * 0.24f
    val roofCr = CornerRadius(20f, 20f)

    val wheelR = w * 0.11f
    val w1Center = Offset(cx - w * 0.22f, cy + h * 0.22f)
    val w2Center = Offset(cx + w * 0.22f, cy + h * 0.22f)

    if (isShadow) {
        drawCircle(color = shadowColor, center = w1Center, radius = wheelR)
        drawCircle(color = shadowColor, center = w2Center, radius = wheelR)
        drawRoundRect(color = shadowColor, topLeft = Offset(bodyLeft, bodyTop), size = Size(bodyW, bodyH), cornerRadius = bodyCr)
        drawRoundRect(color = shadowColor, topLeft = Offset(roofLeft, roofTop), size = Size(roofW, roofH), cornerRadius = roofCr)
    } else {
        // Wheels
        drawCircle(color = Color(0xFF212121), center = w1Center, radius = wheelR)
        drawCircle(color = Color(0xFF212121), center = w2Center, radius = wheelR)
        drawCircle(color = Color(0xFFB0BEC5), center = w1Center, radius = wheelR * 0.5f)
        drawCircle(color = Color(0xFFB0BEC5), center = w2Center, radius = wheelR * 0.5f)

        // Blue Body
        val bodyBrush = Brush.linearGradient(
            colors = listOf(Color(0xFF64B5F6), Color(0xFF1E88E5), Color(0xFF0D47A1)),
            start = Offset(bodyLeft, bodyTop), end = Offset(bodyLeft + bodyW, bodyTop + bodyH)
        )
        drawRoundRect(brush = bodyBrush, topLeft = Offset(roofLeft, roofTop), size = Size(roofW, roofH), cornerRadius = roofCr)
        drawRoundRect(brush = bodyBrush, topLeft = Offset(bodyLeft, bodyTop), size = Size(bodyW, bodyH), cornerRadius = bodyCr)

        // Window
        drawRoundRect(
            color = Color(0xFFE0F7FA),
            topLeft = Offset(roofLeft + roofW * 0.15f, roofTop + roofH * 0.15f),
            size = Size(roofW * 0.70f, roofH * 0.70f),
            cornerRadius = CornerRadius(10f, 10f)
        )
        // Headlight
        drawCircle(color = Color(0xFFFFEB3B), radius = bodyH * 0.22f, center = Offset(bodyLeft + bodyW - 8f, bodyTop + bodyH * 0.4f))
    }
}

private fun DrawScope.draw3DAirplane(
    cx: Float, cy: Float, w: Float, h: Float,
    isShadow: Boolean, shadowColor: Color
) {
    val fuseLeft = cx - w * 0.38f
    val fuseTop = cy - h * 0.08f
    val fuseW = w * 0.76f
    val fuseH = h * 0.16f
    val fuseCr = CornerRadius(fuseH * 0.5f, fuseH * 0.5f)

    val wingsPath = Path().apply {
        moveTo(cx - w * 0.08f, cy - h * 0.36f)
        lineTo(cx + w * 0.12f, cy - h * 0.08f)
        lineTo(cx + w * 0.12f, cy + h * 0.08f)
        lineTo(cx - w * 0.08f, cy + h * 0.36f)
        lineTo(cx - w * 0.18f, cy + h * 0.24f)
        lineTo(cx - w * 0.02f, cy)
        lineTo(cx - w * 0.18f, cy - h * 0.24f)
        close()
    }

    val tailPath = Path().apply {
        moveTo(cx - w * 0.38f, cy - h * 0.08f)
        lineTo(cx - w * 0.32f, cy - h * 0.28f)
        lineTo(cx - w * 0.20f, cy - h * 0.08f)
        close()
    }

    if (isShadow) {
        drawPath(wingsPath, color = shadowColor)
        drawPath(tailPath, color = shadowColor)
        drawRoundRect(color = shadowColor, topLeft = Offset(fuseLeft, fuseTop), size = Size(fuseW, fuseH), cornerRadius = fuseCr)
    } else {
        // Red Wings
        drawPath(wingsPath, color = Color(0xFFE53935))
        drawPath(tailPath, color = Color(0xFFC62828))

        // Yellow Fuselage
        val fuseBrush = Brush.verticalGradient(
            colors = listOf(Color(0xFFFFF59D), Color(0xFFFFD54F), Color(0xFFFF8F00)),
            startY = fuseTop, endY = fuseTop + fuseH
        )
        drawRoundRect(brush = fuseBrush, topLeft = Offset(fuseLeft, fuseTop), size = Size(fuseW, fuseH), cornerRadius = fuseCr)

        // Cockpit Glass
        drawCircle(color = Color(0xFF0288D1), radius = fuseH * 0.35f, center = Offset(fuseLeft + fuseW - fuseH * 0.6f, cy))
    }
}

private fun DrawScope.draw3DBottle(
    cx: Float, cy: Float, w: Float, h: Float,
    isShadow: Boolean, shadowColor: Color
) {
    val bodyLeft = cx - w * 0.18f
    val bodyTop = cy - h * 0.08f
    val bodyW = w * 0.36f
    val bodyH = h * 0.44f
    val bodyCr = CornerRadius(20f, 20f)

    val neckLeft = cx - w * 0.09f
    val neckTop = cy - h * 0.30f
    val neckW = w * 0.18f
    val neckH = h * 0.24f
    val neckCr = CornerRadius(10f, 10f)

    val capLeft = cx - w * 0.11f
    val capTop = cy - h * 0.36f
    val capW = w * 0.22f
    val capH = h * 0.08f
    val capCr = CornerRadius(8f, 8f)

    if (isShadow) {
        drawRoundRect(color = shadowColor, topLeft = Offset(bodyLeft, bodyTop), size = Size(bodyW, bodyH), cornerRadius = bodyCr)
        drawRoundRect(color = shadowColor, topLeft = Offset(neckLeft, neckTop), size = Size(neckW, neckH), cornerRadius = neckCr)
        drawRoundRect(color = shadowColor, topLeft = Offset(capLeft, capTop), size = Size(capW, capH), cornerRadius = capCr)
    } else {
        // Teal Glass Bottle Body & Neck
        val glassBrush = Brush.linearGradient(
            colors = listOf(Color(0xFF80DEEA), Color(0xFF00ACC1), Color(0xFF006064)),
            start = Offset(bodyLeft, bodyTop), end = Offset(bodyLeft + bodyW, bodyTop + bodyH)
        )
        drawRoundRect(brush = glassBrush, topLeft = Offset(bodyLeft, bodyTop), size = Size(bodyW, bodyH), cornerRadius = bodyCr)
        drawRoundRect(brush = glassBrush, topLeft = Offset(neckLeft, neckTop), size = Size(neckW, neckH), cornerRadius = neckCr)

        // Cap
        drawRoundRect(color = Color(0xFFE53935), topLeft = Offset(capLeft, capTop), size = Size(capW, capH), cornerRadius = capCr)

        // Label on front
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(bodyLeft + bodyW * 0.15f, bodyTop + bodyH * 0.25f),
            size = Size(bodyW * 0.70f, bodyH * 0.40f),
            cornerRadius = CornerRadius(8f, 8f)
        )
    }
}

private fun DrawScope.draw3DCup(
    cx: Float, cy: Float, w: Float, h: Float,
    isShadow: Boolean, shadowColor: Color
) {
    val bodyLeft = cx - w * 0.24f
    val bodyTop = cy - h * 0.22f
    val bodyW = w * 0.48f
    val bodyH = h * 0.52f
    val bodyCr = CornerRadius(20f, 20f)

    val handlePath = Path().apply {
        addOval(androidx.compose.ui.geometry.Rect(cx + w * 0.12f, cy - h * 0.12f, cx + w * 0.36f, cy + h * 0.18f))
    }

    if (isShadow) {
        drawPath(handlePath, color = shadowColor)
        drawRoundRect(color = shadowColor, topLeft = Offset(bodyLeft, bodyTop), size = Size(bodyW, bodyH), cornerRadius = bodyCr)
    } else {
        // Pink Handle
        drawPath(handlePath, color = Color(0xFFD81B60))

        // Pink Cup Body
        val cupBrush = Brush.linearGradient(
            colors = listOf(Color(0xFFF48FB1), Color(0xFFD81B60), Color(0xFF880E4F)),
            start = Offset(bodyLeft, bodyTop), end = Offset(bodyLeft + bodyW, bodyTop + bodyH)
        )
        drawRoundRect(brush = cupBrush, topLeft = Offset(bodyLeft, bodyTop), size = Size(bodyW, bodyH), cornerRadius = bodyCr)

        // Steam lines
        val steamColor = Color(0xFF90A4AE).copy(alpha = 0.6f)
        drawCircle(color = steamColor, radius = 8f, center = Offset(cx - 16f, bodyTop - 20f))
        drawCircle(color = steamColor, radius = 8f, center = Offset(cx + 16f, bodyTop - 28f))
    }
}
