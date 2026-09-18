#!/bin/bash
cat << 'INNER_EOF' > replacement.txt
@Composable
fun AllergenIcon(
    allergen: String,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    val color = when (allergen) {
        "Crustaceans" -> Color(0xFFE53935)
        "Molluscs" -> Color(0xFF0288D1)
        "Fish" -> Color(0xFFF57C00)
        "Soya" -> Color(0xFF8BC34A)
        "Gluten" -> Color(0xFFFFB300)
        "Mustard" -> Color(0xFFFBC02D)
        "Sesame" -> Color(0xFF424242)
        "Celery" -> Color(0xFF7CB342)
        "Eggs" -> Color(0xFFFFB300)
        "Milk" -> Color(0xFF42A5F5)
        "Peanuts" -> Color(0xFFD84315)
        "Nuts" -> Color(0xFF8D6E63)
        "Sulphites" -> Color(0xFF00ACC1)
        "Lupin" -> Color(0xFF3949AB)
        else -> Color(0xFF10B981)
    }

    Box(
        modifier = modifier
            .size(size)
            .background(Color.White, CircleShape)
            .border(1.dp, color.copy(alpha = 0.5f), CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.6f)) {
            val w = this.size.width
            val h = this.size.height
            when (allergen) {
                "Gluten" -> {
                    // Wheat stalk
                    drawLine(color = color, start = Offset(w/2f, h*0.1f), end = Offset(w/2f, h*0.9f), strokeWidth = w*0.1f)
                    val xOffsets = listOf(0.7f, 0.3f)
                    for (i in 0..4) {
                        val yBase = h * (0.2f + i * 0.15f)
                        val xEnd = if (i % 2 == 0) w * xOffsets[0] else w * xOffsets[1]
                        drawLine(color = color, start = Offset(w/2f, yBase), end = Offset(xEnd, yBase - h*0.1f), strokeWidth = w*0.1f)
                    }
                }
                "Milk" -> {
                    // Bottle shape
                    val bottleWidth = w * 0.4f
                    val bottleHeight = h * 0.7f
                    val neckHeight = h * 0.2f
                    drawRoundRect(color = color, topLeft = Offset((w - bottleWidth)/2, h*0.25f), size = androidx.compose.ui.geometry.Size(bottleWidth, bottleHeight), cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f))
                    drawRect(color = color, topLeft = Offset((w - bottleWidth*0.5f)/2, h*0.1f), size = androidx.compose.ui.geometry.Size(bottleWidth*0.5f, neckHeight))
                    drawLine(color = Color.White, start = Offset(w*0.4f, h*0.4f), end = Offset(w*0.4f, h*0.8f), strokeWidth = w*0.05f)
                }
                "Fish" -> {
                    val fishPath = Path().apply {
                        moveTo(w*0.8f, h*0.5f)
                        quadraticTo(w*0.5f, h*0.1f, w*0.2f, h*0.5f)
                        quadraticTo(w*0.5f, h*0.9f, w*0.8f, h*0.5f)
                        lineTo(w, h*0.3f)
                        lineTo(w, h*0.7f)
                        close()
                    }
                    drawPath(fishPath, color = color)
                    drawCircle(color = Color.White, radius = w*0.05f, center = Offset(w*0.35f, h*0.45f))
                }
                "Eggs" -> {
                    // Two eggs
                    drawOval(color = Color(0xFFFFCC80), topLeft = Offset(w*0.1f, h*0.2f), size = androidx.compose.ui.geometry.Size(w*0.5f, h*0.7f))
                    drawOval(color = Color(0xFFFFF9C4), topLeft = Offset(w*0.3f, h*0.4f), size = androidx.compose.ui.geometry.Size(w*0.6f, h*0.5f))
                    drawCircle(color = Color(0xFFFFB300), radius = w*0.15f, center = Offset(w*0.6f, h*0.65f))
                }
                "Peanuts" -> {
                    // Figure-8 shape
                    drawCircle(color = color, radius = w*0.3f, center = Offset(w*0.35f, h*0.35f))
                    drawCircle(color = color, radius = w*0.3f, center = Offset(w*0.65f, h*0.65f))
                    drawLine(color = Color.White, start = Offset(w*0.3f, h*0.2f), end = Offset(w*0.4f, h*0.4f), strokeWidth = w*0.05f)
                    drawLine(color = Color.White, start = Offset(w*0.5f, h*0.5f), end = Offset(w*0.7f, h*0.7f), strokeWidth = w*0.05f)
                }
                "Nuts" -> {
                    // Almond shape
                    val nut = Path().apply {
                        moveTo(w*0.5f, h*0.1f)
                        quadraticTo(w*0.9f, h*0.5f, w*0.5f, h*0.9f)
                        quadraticTo(w*0.1f, h*0.5f, w*0.5f, h*0.1f)
                        close()
                    }
                    drawPath(nut, color = color)
                }
                "Crustaceans" -> {
                    // Crab shape
                    drawOval(color = color, topLeft = Offset(w*0.2f, h*0.4f), size = androidx.compose.ui.geometry.Size(w*0.6f, h*0.4f)) // body
                    // claws
                    drawCircle(color = color, radius = w*0.15f, center = Offset(w*0.2f, h*0.2f))
                    drawCircle(color = color, radius = w*0.15f, center = Offset(w*0.8f, h*0.2f))
                    drawCircle(color = Color.White, radius = w*0.08f, center = Offset(w*0.2f, h*0.2f))
                    drawCircle(color = Color.White, radius = w*0.08f, center = Offset(w*0.8f, h*0.2f))
                    // legs
                    drawLine(color = color, start = Offset(w*0.2f, h*0.6f), end = Offset(0f, h*0.6f), strokeWidth = w*0.1f)
                    drawLine(color = color, start = Offset(w*0.8f, h*0.6f), end = Offset(w, h*0.6f), strokeWidth = w*0.1f)
                }
                "Molluscs" -> {
                    // Scallop shell
                    val shell = Path().apply {
                        moveTo(w*0.5f, h)
                        lineTo(w*0.2f, h*0.3f)
                        quadraticTo(w*0.5f, -h*0.1f, w*0.8f, h*0.3f)
                        close()
                    }
                    drawPath(shell, color = color)
                    for (i in 1..4) {
                        drawLine(color = Color.White, start = Offset(w*0.5f, h), end = Offset(w*(0.2f + 0.15f*i), h*0.3f), strokeWidth = w*0.05f)
                    }
                }
                "Soya" -> {
                    // Pod with beans
                    val pod = Path().apply {
                        moveTo(w*0.1f, h*0.9f)
                        quadraticTo(w*0.1f, h*0.1f, w*0.9f, h*0.1f)
                        quadraticTo(w*0.9f, h*0.9f, w*0.1f, h*0.9f)
                        close()
                    }
                    drawPath(pod, color = color)
                    drawCircle(color = Color.White, radius = w*0.15f, center = Offset(w*0.35f, h*0.65f))
                    drawCircle(color = Color.White, radius = w*0.15f, center = Offset(w*0.65f, h*0.35f))
                    drawCircle(color = Color(0xFF7CB342), radius = w*0.1f, center = Offset(w*0.35f, h*0.65f))
                    drawCircle(color = Color(0xFF7CB342), radius = w*0.1f, center = Offset(w*0.65f, h*0.35f))
                }
                "Mustard" -> {
                    // Plant and yellow seeds
                    drawLine(color = Color(0xFF81C784), start = Offset(w*0.5f, h), end = Offset(w*0.5f, h*0.3f), strokeWidth = w*0.1f) // stem
                    drawCircle(color = Color(0xFF81C784), radius = w*0.2f, center = Offset(w*0.7f, h*0.6f)) // leaf
                    // yellow seeds/flowers
                    drawCircle(color = color, radius = w*0.15f, center = Offset(w*0.5f, h*0.2f))
                    drawCircle(color = color, radius = w*0.15f, center = Offset(w*0.3f, h*0.3f))
                    drawCircle(color = color, radius = w*0.15f, center = Offset(w*0.7f, h*0.3f))
                }
                "Sesame" -> {
                    // Sesame seeds - small teardrops
                    val seed1 = Path().apply { moveTo(w*0.3f, h*0.3f); quadraticTo(w*0.5f, h*0.3f, w*0.5f, h*0.6f); quadraticTo(w*0.3f, h*0.6f, w*0.3f, h*0.3f); close() }
                    drawPath(seed1, color = color) // black seed
                    val seed2 = Path().apply { moveTo(w*0.6f, h*0.5f); quadraticTo(w*0.8f, h*0.5f, w*0.8f, h*0.8f); quadraticTo(w*0.6f, h*0.8f, w*0.6f, h*0.5f); close() }
                    drawPath(seed2, color = Color(0xFFBDBDBD)) // white/beige seed
                    val seed3 = Path().apply { moveTo(w*0.7f, h*0.1f); quadraticTo(w*0.9f, h*0.1f, w*0.9f, h*0.4f); quadraticTo(w*0.7f, h*0.4f, w*0.7f, h*0.1f); close() }
                    drawPath(seed3, color = color) 
                }
                "Celery" -> {
                    // Celery stalks and leaves
                    drawRect(color = color, topLeft = Offset(w*0.3f, h*0.4f), size = androidx.compose.ui.geometry.Size(w*0.15f, h*0.6f))
                    drawRect(color = Color(0xFFAED581), topLeft = Offset(w*0.55f, h*0.3f), size = androidx.compose.ui.geometry.Size(w*0.15f, h*0.7f))
                    // leaves
                    drawCircle(color = color, radius = w*0.2f, center = Offset(w*0.375f, h*0.3f))
                    drawCircle(color = Color(0xFFAED581), radius = w*0.2f, center = Offset(w*0.625f, h*0.2f))
                }
                "Sulphites" -> {
                    // Flask shape
                    val flask = Path().apply {
                        moveTo(w*0.4f, h*0.1f)
                        lineTo(w*0.6f, h*0.1f)
                        lineTo(w*0.6f, h*0.4f)
                        lineTo(w*0.9f, h*0.9f)
                        lineTo(w*0.1f, h*0.9f)
                        lineTo(w*0.4f, h*0.4f)
                        close()
                    }
                    drawPath(flask, color = Color(0xFFE0F7FA)) // Glass
                    val liquid = Path().apply {
                        moveTo(w*0.25f, h*0.6f)
                        lineTo(w*0.75f, h*0.6f)
                        lineTo(w*0.85f, h*0.85f)
                        lineTo(w*0.15f, h*0.85f)
                        close()
                    }
                    drawPath(liquid, color = color)
                }
                "Lupin" -> {
                    // Blue elongated flower (Lupin)
                    drawLine(color = Color(0xFF81C784), start = Offset(w*0.5f, h), end = Offset(w*0.5f, h*0.1f), strokeWidth = w*0.08f) // stem
                    // flower petals along stem
                    for (i in 0..4) {
                        val y = h * (0.2f + i * 0.15f)
                        drawCircle(color = color, radius = w*0.15f, center = Offset(w*0.35f, y))
                        drawCircle(color = color, radius = w*0.15f, center = Offset(w*0.65f, y))
                        drawCircle(color = Color(0xFF5C6BC0), radius = w*0.1f, center = Offset(w*0.5f, y))
                    }
                }
                else -> {
                    drawCircle(color = color, radius = w * 0.4f, style = Stroke(width = w*0.1f))
                    drawCircle(color = color, radius = w * 0.2f)
                }
            }
        }
    }
}
INNER_EOF
sed -i '/@Composable/,/^}/!b;//!d;/@Composable/r replacement.txt' app/src/main/java/com/example/ui/theme/HotelStudioDesign.kt