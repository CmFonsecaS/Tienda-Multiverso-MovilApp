package com.example.tiendamultiverso.ui.screens

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComunicacionAccesibleScreen(
    onVolver: () -> Unit = {}
) {

    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current

    var mensaje by remember {
        mutableStateOf("")
    }

    var mensajeGrande by remember {
        mutableStateOf("")
    }

    var ttsDisponible by remember {
        mutableStateOf(false)
    }

    var textToSpeech by remember {
        mutableStateOf<TextToSpeech?>(null)
    }

    val mensajesRapidos = listOf(
        "Necesito ayuda con esta figura.",
        "¿Cuál es el precio?",
        "¿Tiene stock disponible?",
        "¿Puede escribirme la respuesta?"
    )

    DisposableEffect(Unit) {

        var ttsLocal: TextToSpeech? = null

        ttsLocal = TextToSpeech(context) { estado ->

            if (estado == TextToSpeech.SUCCESS) {

                val resultado = ttsLocal?.setLanguage(
                    Locale.forLanguageTag("es-CL")
                )

                ttsDisponible =
                    resultado != TextToSpeech.LANG_MISSING_DATA &&
                            resultado != TextToSpeech.LANG_NOT_SUPPORTED

            } else {

                ttsDisponible = false
            }
        }

        textToSpeech = ttsLocal

        onDispose {

            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Comunicación accesible",
                        fontWeight = FontWeight.Bold
                    )
                },

                navigationIcon = {

                    TextButton(
                        onClick = onVolver
                    ) {

                        Text(
                            text = "‹",
                            color = Color.White,
                            fontSize = 32.sp
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(

                    containerColor =
                        MaterialTheme.colorScheme.primary,

                    titleContentColor =
                        MaterialTheme.colorScheme.onPrimary
                )
            )
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(20.dp)
        ) {

            Text(
                text = "Comunícate fácilmente",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text =
                    "Escribe un mensaje o selecciona una frase rápida " +
                            "para mostrarla a otra persona.",
                fontSize = 16.sp
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            OutlinedTextField(

                value = mensaje,

                onValueChange = {
                    mensaje = it
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(
                        minHeight = 120.dp
                    )
                    .semantics {

                        contentDescription =
                            "Campo para escribir un mensaje"
                    },

                label = {

                    Text(
                        text = "Escribe tu mensaje"
                    )
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Mensajes rápidos",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            mensajesRapidos.forEach { frase ->

                OutlinedButton(

                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(
                            minHeight = 52.dp
                        ),

                    onClick = {

                        mensaje = frase

                        haptic.performHapticFeedback(
                            HapticFeedbackType.LongPress
                        )
                    }
                ) {

                    Text(
                        text = frase
                    )
                }

                Spacer(
                    modifier = Modifier.height(8.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Button(

                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(
                        minHeight = 56.dp
                    ),

                onClick = {

                    if (mensaje.isNotBlank()) {

                        mensajeGrande =
                            mensaje.trim()

                        haptic.performHapticFeedback(
                            HapticFeedbackType.LongPress
                        )
                    }
                }
            ) {

                Text(
                    text = "Mostrar mensaje",
                    fontSize = 16.sp
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedButton(

                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(
                        minHeight = 56.dp
                    ),

                enabled =
                    mensaje.isNotBlank() &&
                            ttsDisponible,

                onClick = {

                    textToSpeech?.speak(
                        mensaje,
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "mensaje_accesible"
                    )

                    haptic.performHapticFeedback(
                        HapticFeedbackType.LongPress
                    )
                }
            ) {

                Text(
                    text = "Reproducir mensaje",
                    fontSize = 16.sp
                )
            }

            if (mensajeGrande.isNotEmpty()) {

                Spacer(
                    modifier = Modifier.height(28.dp)
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {

                            contentDescription =
                                "Mensaje de comunicación en tamaño grande"
                        }
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(24.dp),

                        verticalArrangement =
                            Arrangement.Center
                    ) {

                        Text(

                            text = mensajeGrande,

                            modifier =
                                Modifier.fillMaxWidth(),

                            fontSize = 30.sp,

                            fontWeight =
                                FontWeight.Bold,

                            textAlign =
                                TextAlign.Center,

                            color = Color.Black
                        )
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}