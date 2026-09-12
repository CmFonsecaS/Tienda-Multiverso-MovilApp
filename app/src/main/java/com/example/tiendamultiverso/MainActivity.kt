package com.example.tiendamultiverso

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.tiendamultiverso.data.Usuario
import com.example.tiendamultiverso.ui.screens.ComunicacionAccesibleScreen
import com.example.tiendamultiverso.ui.screens.CotizarScreen
import com.example.tiendamultiverso.ui.screens.HomeScreen
import com.example.tiendamultiverso.ui.screens.LoginScreen
import com.example.tiendamultiverso.ui.screens.RecuperarPasswordScreen
import com.example.tiendamultiverso.ui.screens.RegistroScreen
import com.example.tiendamultiverso.ui.screens.SplashScreen
import com.example.tiendamultiverso.ui.theme.TiendaMultiversoTheme
import kotlinx.coroutines.delay

enum class Pantalla {

    SPLASH,

    LOGIN,

    REGISTRO,

    RECUPERAR_PASSWORD,

    HOME,

    COTIZAR,

    COMUNICACION_ACCESIBLE
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
                    mutableStateOf(Pantalla.SPLASH)
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


                    Pantalla.SPLASH -> {

                        LaunchedEffect(Unit) {

                            delay(2000)

                            pantallaActual =
                                Pantalla.LOGIN
                        }

                        SplashScreen()
                    }


                    Pantalla.LOGIN -> {

                        LoginScreen(

                            onLoginCorrecto = { usuario ->

                                usuarioPendiente =
                                    usuario


                                reproducirDoblePitido()

                                mostrarDialogoLogin =
                                    true
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

                                mostrarDialogoRegistro =
                                    true
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

                                    usuarioActual =
                                        null

                                    pantallaActual =
                                        Pantalla.LOGIN
                                },

                                onCotizarClick = {

                                    pantallaActual =
                                        Pantalla.COTIZAR
                                },

                                onComunicacionClick = {

                                    pantallaActual =
                                        Pantalla.COMUNICACION_ACCESIBLE
                                }
                            )
                        }
                    }


                    Pantalla.COTIZAR -> {

                        CotizarScreen(

                            onVolver = {

                                pantallaActual =
                                    Pantalla.HOME
                            }
                        )
                    }


                    Pantalla.COMUNICACION_ACCESIBLE -> {

                        ComunicacionAccesibleScreen(

                            onVolver = {

                                pantallaActual =
                                    Pantalla.HOME
                            }
                        )
                    }
                }


                if (
                    mostrarDialogoLogin &&
                    usuarioPendiente != null
                ) {

                    AlertDialog(

                        onDismissRequest = {

                            mostrarDialogoLogin =
                                false
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
                                text =
                                    "¡Cuenta creada!"
                            )
                        },

                        text = {

                            Text(
                                text =
                                    "Tu usuario fue registrado correctamente. " +
                                            "Ahora puedes iniciar sesión."
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


    private fun reproducirDoblePitido() {

        val toneGenerator =
            ToneGenerator(
                AudioManager.STREAM_NOTIFICATION,
                100
            )

        val handler =
            Handler(
                Looper.getMainLooper()
            )

        toneGenerator.startTone(
            ToneGenerator.TONE_PROP_BEEP,
            180
        )

        handler.postDelayed(
            {

                toneGenerator.startTone(
                    ToneGenerator.TONE_PROP_BEEP,
                    180
                )

            },
            320
        )

        handler.postDelayed(
            {

                toneGenerator.release()

            },
            700
        )
    }
}