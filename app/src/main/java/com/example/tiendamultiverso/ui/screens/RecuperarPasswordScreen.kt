package com.example.tiendamultiverso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamultiverso.data.UsuarioRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun RecuperarPasswordScreen(
    onVolverLogin: () -> Unit = {}
) {

    val contexto = LocalContext.current
    val alcanceCorrutina = rememberCoroutineScope()

    var email by remember {
        mutableStateOf("")
    }

    var mensajeError by remember {
        mutableStateOf("")
    }

    var mostrarDialogo by remember {
        mutableStateOf(false)
    }

    var nombreUsuario by remember {
        mutableStateOf("")
    }

    var cargando by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Recuperar contraseña",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Ingresa el correo asociado a tu cuenta."
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        OutlinedTextField(
            value = email,

            onValueChange = {
                email = it
                mensajeError = ""
            },

            enabled = !cargando,
            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Correo electrónico")
            },

            singleLine = true,

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (mensajeError.isNotEmpty()) {

            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = !cargando,

            onClick = {

                if (email.isBlank()) {
                    mensajeError =
                        "Debes ingresar tu correo electrónico."
                    return@Button
                }

                val correoIngresado = email.trim()

                cargando = true
                mensajeError = ""

                alcanceCorrutina.launch {
                    try {
                        val usuarioEncontrado =
                            withContext(Dispatchers.IO) {
                                UsuarioRepository.buscarPorEmail(
                                    context = contexto,
                                    email = correoIngresado
                                )
                            }

                        if (usuarioEncontrado != null) {
                            nombreUsuario = usuarioEncontrado.nombre
                            mensajeError = ""
                            mostrarDialogo = true
                        } else {
                            mensajeError =
                                "No existe una cuenta registrada con ese correo."
                        }

                    } catch (e: CancellationException) {
                        throw e

                    } catch (e: Exception) {
                        mensajeError =
                            "No se pudo consultar la cuenta. Intenta nuevamente."

                    } finally {
                        cargando = false
                    }
                }
            }
        ) {

            Text(
                text = if (cargando) {
                    "Buscando cuenta..."
                } else {
                    "Recuperar contraseña"
                }
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        TextButton(
            onClick = onVolverLogin,
            enabled = !cargando
        ) {
            Text("Volver al inicio de sesión")
        }
    }

    if (mostrarDialogo) {

        AlertDialog(
            onDismissRequest = {
                mostrarDialogo = false
            },

            title = {
                Text("Cuenta encontrada")
            },

            text = {
                Text(
                    text =
                        "Hola, $nombreUsuario.\n\n" +
                                "Se ha simulado el envío de instrucciones " +
                                "para recuperar tu contraseña."
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        mostrarDialogo = false
                        onVolverLogin()
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}
