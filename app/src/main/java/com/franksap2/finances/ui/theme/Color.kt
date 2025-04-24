package com.franksap2.finances.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val Red500 = Color(0xFFFF5001)
val Red300 = Color(0xFFCB6367)
val Green500 = Color(0xFF38bc5b)
val Green300 = Color(0xFF63C095)

val Grey500 = Color(0xFFF5F5F5)
val Grey700 = Color(0xFF303030)

val Colors.positive get() = Green500
val Colors.positiveVariant get() = Green300
val Colors.negative get() = Red500
val Colors.negativeVariant get() = Red300

val Colors.surfaceVariant get() = if (isLight) Grey500 else Grey700

val Colors.onSurfaceVariant: Color
    get() = if (isLight) Color(0xFF202020) else Color(0xFFE0E0E0)

