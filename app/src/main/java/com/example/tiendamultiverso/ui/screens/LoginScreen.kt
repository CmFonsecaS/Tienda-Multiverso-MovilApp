package com.example.tiendamultiverso.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamultiverso.R
import com.example.tiendamultiverso.data.Usuario
import com.example.tiendamultiverso.data.UsuarioRepository

@Composable
fun LoginScreen(
    onLoginCorrecto: (Usuario) -> Unit,
    onRegistroClick: () -> Unit,
    onRecuperarPasswordClick: () -> Unit
) {

    var usuarioCorreo by remember {
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
            .verticalScroll(
                rememberScrollState()
            )
            .padding(horizontal = 30.dp),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(110.dp)
        )

        /*
         * LOGO TIENDA MULTIVERSO
         */
        Image(
            painter = painterResource(
                id = R.drawable.icono_multiverso
            ),

            contentDescription = "Logo de Tienda Multiverso",

            modifier = Modifier.size(150.dp),

            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Tienda Multiverso",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "Tu universo de figuras comienza aquí",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(
            modifier = Modifier.height(36.dp)
        )

        OutlinedTextField(
            value = usuarioCorreo,

            onValueChange = {
                usuarioCorreo = it
                mensajeError = ""
            },

            label = {
                Text(
                    text = "Usuario o correo"
                )
            },

            singleLine = true,

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        OutlinedTextField(
            value = password,

            onValueChange = {
                password = it
                mensajeError = ""
            },

            label = {
                Text(
                    text = "Contraseña"
                )
            },

            singleLine = true,

            visualTransformation =
                PasswordVisualTransformation(),

            modifier = Modifier.fillMaxWidth()
        )

        if (mensajeError.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),

            onClick = {

                val usuarioEncontrado =
                    UsuarioRepository.validarLogin(
                        usuario = usuarioCorreo.trim(),
                        password = password
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
                text = "Iniciar sesión",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        TextButton(
            onClick = onRecuperarPasswordClick
        ) {

            Text(
                text = "¿Olvidaste tu contraseña?"
            )
        }

        TextButton(
            onClick = onRegistroClick
        ) {

            Text(
                text = "¿No tienes cuenta? Regístrate"
            )
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )
    }
}