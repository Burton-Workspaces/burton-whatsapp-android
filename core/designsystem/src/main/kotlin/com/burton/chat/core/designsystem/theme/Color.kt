package com.burton.chat.core.designsystem.theme

import androidx.compose.ui.graphics.Color

val Forest = Color(0xFF0B6E4F)
val ForestDark = Color(0xFF084C37)
val ForestContainer = Color(0xFFD4F0E3)
val Teal = Color(0xFF128C7E)
val ChatBackground = Color(0xFFE6F1EB)
val OutgoingBubble = Color(0xFFD1F4DE)
val IncomingBubble = Color(0xFFFFFFFF)
val UnreadBadge = Color(0xFF25A366)
val SurfaceMuted = Color(0xFFF4F7F5)
val OnForest = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF122017)
val TextSecondary = Color(0xFF5C6B62)

val AvatarPalette = listOf(
    Color(0xFF0B6E4F),
    Color(0xFF1B6CA8),
    Color(0xFFB45309),
    Color(0xFF7C3AED),
    Color(0xFFBE123C),
    Color(0xFF0F766E),
    Color(0xFF365314),
    Color(0xFF1E3A8A),
)

fun avatarColor(seed: Int): Color {
    val index = kotlin.math.abs(seed) % AvatarPalette.size
    return AvatarPalette[index]
}
