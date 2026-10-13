package com.example.pdf.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PDFColors = darkColorScheme(
    primary = Color(0xFF25D4B0),
    onPrimary = Color(0xFF001E18),
    secondary = Color(0xFF8FE8D5),
    background = Color(0xFF090B0F),
    onBackground = Color(0xFFF3F5F7),
    surface = Color(0xFF12161C),
    onSurface = Color(0xFFF3F5F7),
    surfaceVariant = Color(0xFF1A2028),
    onSurfaceVariant = Color(0xFFB3BBC5),
    outline = Color(0xFF39414C)
)

@Composable
fun PDFTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = PDFColors, content = content)
}
