package com.example.tiendamultiverso.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamultiverso.data.Usuario
import com.example.tiendamultiverso.data.UsuarioRepository
import androidx.compose.foundation.layout.padding

@Composable
fun LoginScreen(
    onLoginCorrecto: (Usuario) -> Unit = {},
    onRegistroClick: () -> Unit = {},
    onRecuperarPasswordClick: () -> Unit = {}
) {

    var usuario by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var mensajeError by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),

        horizontalAlignment = Alignment.CenterHorizontally,

        verticalArrangement = Arrangement.Center
    ) {

        /*
         * LOGO
         * Círculo rojo con una M blanca.
         * No necesita imágenes ni archivos drawable.
         */
        Box(
            modifier = Modifier
                .size(92.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ),

            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "M",
                color = Color.White,
                fontSize = 48.sp,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Text(
            text = "Tienda Multiverso",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Tu universo de figuras comienza aquí",
            fontSize = 16.sp
        )

        Spacer(
            modifier = Modifier.height(32.dp)
        )

        OutlinedTextField(
            value = usuario,

            onValueChange = {

                usuario = it

                mensajeError = ""
            },

            modifier = Modifier.fillMaxWidth(),

            label = {

                Text(
                    text = "Usuario o correo"
                )
            },

            singleLine = true,

            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = password,

            onValueChange = {

                password = it

                mensajeError = ""
            },

            modifier = Modifier.fillMaxWidth(),

            label = {

                Text(
                    text = "Contraseña"
                )
            },

            singleLine = true,

            visualTransformation =
                PasswordVisualTransformation(),

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Password
                )
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        if (mensajeError.isNotEmpty()) {

            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        Button(
            modifier = Modifier.fillMaxWidth(),

            onClick = {

                if (
                    usuario.isBlank() ||
                    password.isBlank()
                ) {

                    mensajeError =
                        "Debes completar usuario y contraseña"

                    return@Button
                }

                val usuarioEncontrado =
                    UsuarioRepository.validarLogin(

                        usuario =
                            usuario.trim(),

                        password =
                            password
                    )

                if (usuarioEncontrado != null) {

                    mensajeError = ""

                    onLoginCorrecto(
                        usuarioEncontrado
                    )

                } else {

                    mensajeError =
                        "Usuario o contraseña incorrectos"
                }
            }
        ) {

            Text(
                text = "Iniciar sesión"
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        TextButton(
            onClick =
                onRecuperarPasswordClick
        ) {

            Text(
                text = "¿Olvidaste tu contraseña?"
            )
        }

        TextButton(
            onClick =
                onRegistroClick
        ) {

            Text(
                text = "¿No tienes cuenta? Regístrate"
            )
        }
    }
}