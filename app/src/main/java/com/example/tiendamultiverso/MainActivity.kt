package com.example.tiendamultiverso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.tiendamultiverso.data.Usuario
import com.example.tiendamultiverso.ui.screens.HomeScreen
import com.example.tiendamultiverso.ui.screens.LoginScreen
import com.example.tiendamultiverso.ui.screens.RecuperarPasswordScreen
import com.example.tiendamultiverso.ui.screens.RegistroScreen
import com.example.tiendamultiverso.ui.theme.TiendaMultiversoTheme

enum class Pantalla {
    LOGIN,
    REGISTRO,
    RECUPERAR_PASSWORD,
    HOME
}

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            TiendaMultiversoTheme {

                var pantallaActual by remember {
                    mutableStateOf(Pantalla.LOGIN)
                }

                var usuarioActual by remember {
                    mutableStateOf<Usuario?>(null)
                }

                var usuarioPendiente by remember {
                    mutableStateOf<Usuario?>(null)
                }

                var mostrarDialogoLogin by remember {
                    mutableStateOf(false)
                }

                var mostrarDialogoRegistro by remember {
                    mutableStateOf(false)
                }

                when (pantallaActual) {

                    Pantalla.LOGIN -> {

                        LoginScreen(

                            onLoginCorrecto = { usuario ->

                                usuarioPendiente = usuario

                                mostrarDialogoLogin = true
                            },

                            onRegistroClick = {

                                pantallaActual =
                                    Pantalla.REGISTRO
                            },

                            onRecuperarPasswordClick = {

                                pantallaActual =
                                    Pantalla.RECUPERAR_PASSWORD
                            }
                        )
                    }

                    Pantalla.REGISTRO -> {

                        RegistroScreen(

                            onRegistroExitoso = {

                                mostrarDialogoRegistro = true
                            },

                            onVolverLogin = {

                                pantallaActual =
                                    Pantalla.LOGIN
                            }
                        )
                    }

                    Pantalla.RECUPERAR_PASSWORD -> {

                        RecuperarPasswordScreen(

                            onVolverLogin = {

                                pantallaActual =
                                    Pantalla.LOGIN
                            }
                        )
                    }

                    Pantalla.HOME -> {

                        usuarioActual?.let { usuario ->

                            HomeScreen(
                                usuario = usuario,

                                onCerrarSesion = {

                                    usuarioActual = null

                                    pantallaActual =
                                        Pantalla.LOGIN
                                }
                            )
                        }
                    }
                }

                if (
                    mostrarDialogoLogin &&
                    usuarioPendiente != null
                ) {

                    AlertDialog(

                        onDismissRequest = {
                            mostrarDialogoLogin = false
                        },

                        title = {

                            Text(
                                text =
                                    "¡Bienvenido al Multiverso!"
                            )
                        },

                        text = {

                            Text(
                                text =
                                    "Inicio de sesión correcto.\n\n" +
                                            "Hola, ${usuarioPendiente!!.nombre}."
                            )
                        },

                        confirmButton = {

                            TextButton(

                                onClick = {

                                    usuarioActual =
                                        usuarioPendiente

                                    usuarioPendiente =
                                        null

                                    mostrarDialogoLogin =
                                        false

                                    pantallaActual =
                                        Pantalla.HOME
                                }
                            ) {

                                Text(
                                    text = "Continuar"
                                )
                            }
                        }
                    )
                }

                if (mostrarDialogoRegistro) {

                    AlertDialog(

                        onDismissRequest = {},

                        title = {

                            Text(
                                text = "¡Cuenta creada!"
                            )
                        },

                        text = {

                            Text(
                                text =
                                    "Tu usuario fue registrado " +
                                            "correctamente. Ahora puedes " +
                                            "iniciar sesión."
                            )
                        },

                        confirmButton = {

                            TextButton(

                                onClick = {

                                    mostrarDialogoRegistro =
                                        false

                                    pantallaActual =
                                        Pantalla.LOGIN
                                }
                            ) {

                                Text(
                                    text = "Ir al Login"
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}