package com.burton.chat.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Forest,
    onPrimary = OnForest,
    primaryContainer = ForestContainer,
    onPrimaryContainer = ForestDark,
    secondary = Teal,
    onSecondary = OnForest,
    background = Color(0xFFF7FBF8),
    onBackground = TextPrimary,
    surface = Color(0xFFFFFFFF),
    onSurface = TextPrimary,
    surfaceVariant = SurfaceMuted,
    onSurfaceVariant = TextSecondary,
    outline = Color(0xFFC5D2CA),
)

@Composable
fun BurtonChatTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = BurtonTypography,
        content = content,
    )
}
