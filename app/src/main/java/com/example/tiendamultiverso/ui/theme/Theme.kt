package com.example.tiendamultiverso.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(

    primary = MultiversoRed,
    onPrimary = MultiversoWhite,

    secondary = MultiversoDarkRed,
    onSecondary = MultiversoWhite,

    background = MultiversoBackground,
    onBackground = MultiversoBlack,

    surface = MultiversoSurface,
    onSurface = MultiversoBlack,

    error = MultiversoDarkRed
)

@Composable
fun TiendaMultiversoTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}