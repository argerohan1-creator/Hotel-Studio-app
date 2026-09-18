package com.example.ui.screens

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class SignageTemplate(
    val name: String,
    val bgColor: Color,
    val borderColor: Color,
    val textColor: Color,
    val textSecondaryColor: Color,
    val accentColor: Color,
    val borderThickness: Dp,
    val cornerRadius: Dp = 0.dp,
    val isDoubleBorder: Boolean = false
)

val signageTemplates = listOf(
    SignageTemplate("Heritage Gold", Color(0xFFFFFDF9), Color(0xFFC5A059), Color(0xFF1A1A1A), Color(0xFF555555), Color(0xFFC5A059), 1.5.dp, 8.dp, true),
    SignageTemplate("Royal Palace", Color(0xFFFAF5ED), Color(0xFF8C734B), Color(0xFF2D2319), Color(0xFF6B583E), Color(0xFF8C734B), 2.dp, 4.dp, true),
    SignageTemplate("Contemporary Gold", Color(0xFFFFFFFF), Color(0xFFE2E8F0), Color(0xFF0F172A), Color(0xFF64748B), Color(0xFFD4AF37), 1.dp, 12.dp, false),
    SignageTemplate("Ivory Marble", Color(0xFFFDFBF7), Color(0xFFD4AF37), Color(0xFF333333), Color(0xFF666666), Color(0xFFD4AF37), 1.5.dp, 6.dp, true),
    SignageTemplate("Art Deco", Color(0xFF111827), Color(0xFFFBBF24), Color(0xFFFEF3C7), Color(0xFFFDE68A), Color(0xFFFBBF24), 2.dp, 0.dp, true),
    SignageTemplate("Signature Luxury", Color(0xFF000000), Color(0xFFD4AF37), Color(0xFFFFFFFF), Color(0xFFD4D4D8), Color(0xFFD4AF37), 1.5.dp, 4.dp, true),
    SignageTemplate("Fine Dining", Color(0xFFFFFFFF), Color(0xFFCBD5E1), Color(0xFF1E293B), Color(0xFF64748B), Color(0xFF059669), 1.5.dp, 8.dp, false),
    SignageTemplate("Crystal Elegance", Color(0xFFF8FAFC), Color(0xFF0284C7), Color(0xFF0F172A), Color(0xFF475569), Color(0xFF0284C7), 2.dp, 16.dp, false),
    SignageTemplate("Regal Black & Gold", Color(0xFF0A0A0A), Color(0xFFD4AF37), Color(0xFFFEF3C7), Color(0xFFD4D4D8), Color(0xFFD4AF37), 2.dp, 0.dp, true),
    SignageTemplate("Emerald Estate", Color(0xFFF0FDF4), Color(0xFF10B981), Color(0xFF064E3B), Color(0xFF059669), Color(0xFF10B981), 2.dp, 10.dp, true)
)
