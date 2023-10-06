package com.example.translator_fwathces.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val Gray = Color(0xFF8D8D8D)
val Black = Color(0xFF2F2F2F)
val Grayish = Color(0xFFD9D9D9)
val White = Color(0xFFF7F7F7)

internal val wearColorPalette: Colors = Colors(
    primary = Black,
    primaryVariant = Gray,
    secondary = Gray,
    secondaryVariant = Grayish,
    error = White,
    onPrimary = White,
    onSecondary = White,
    onError = Color.Black
)