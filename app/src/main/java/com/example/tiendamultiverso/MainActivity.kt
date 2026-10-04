
package com.example.tiendamultiverso

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tiendamultiverso.data.FiguraRepository
import com.example.tiendamultiverso.data.Usuario
import com.example.tiendamultiverso.data.UsuarioRepository
import com.example.tiendamultiverso.data.local.SesionManager
import com.example.tiendamultiverso.ui.screens.AdministrarFigurasScreen
import com.example.tiendamultiverso.ui.screens.ComunicacionAccesibleScreen
import com.example.tiendamultiverso.ui.screens.CotizarScreen
import com.example.tiendamultiverso.ui.screens.HomeScreen
import com.example.tiendamultiverso.ui.screens.LoginScreen
import com.example.tiendamultiverso.ui.screens.RecuperarPasswordScreen
import com.example.tiendamultiverso.ui.screens.RegistroScreen
import com.example.tiendamultiverso.ui.screens.SplashScreen
import com.example.tiendamultiverso.ui.theme.TiendaMultiversoTheme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

enum class Pantalla {
    SPLASH,
    LOGIN,
    REGISTRO,
    RECUPERAR_PASSWORD,
    HOME,
    COTIZAR,
    COMUNICACION_ACCESIBLE,
    ADMINISTRAR_FIGURAS
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val contexto = applicationContext
        val sesionManager = SesionManager(contexto)

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

                var errorInicializacion by remember {
                    mutableStateOf(false)
                }

                var intentoInicializacion by remember {
                    mutableIntStateOf(0)
                }

                when (pantallaActual) {

                    Pantalla.SPLASH -> {

                        LaunchedEffect(intentoInicializacion) {

                            errorInicializacion = false

                            try {

                                delay(2.seconds)

                                // Inicializar usuarios en segundo plano.
                                withContext(Dispatchers.IO) {
                                    UsuarioRepository.inicializar(
                                        contexto
                                    )
                                }

                                // FiguraRepository administra
                                // internamente Dispatchers.IO
                                // y actualiza Compose en Main.
                                FiguraRepository.inicializar(
                                    contexto
                                )

                                // Recuperar la sesión desde SQLite.
                                val identificador =
                                    sesionManager.obtenerUsuarioActivo()

                                val usuarioGuardado =
                                    if (identificador != null) {

                                        withContext(Dispatchers.IO) {
                                            UsuarioRepository
                                                .buscarPorUsuario(
                                                    context = contexto,
                                                    identificador =
                                                        identificador
                                                )
                                        }

                                    } else {
                                        null
                                    }

                                if (usuarioGuardado != null) {

                                    usuarioActual = usuarioGuardado
                                    pantallaActual = Pantalla.HOME

                                } else {

                                    if (identificador != null) {
                                        sesionManager.cerrarSesion()
                                    }

                                    usuarioActual = null
                                    pantallaActual = Pantalla.LOGIN
                                }

                            } catch (e: CancellationException) {

                                throw e

                            } catch (e: Exception) {

                                errorInicializacion = true
                            }
                        }

                        if (errorInicializacion) {

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),

                                verticalArrangement =
                                    Arrangement.Center,

                                horizontalAlignment =
                                    Alignment.CenterHorizontally
                            ) {

                                Text(
                                    text = "No se pudieron cargar los datos."
                                )

                                Button(
                                    onClick = {
                                        intentoInicializacion++
                                    }
                                ) {
                                    Text("Reintentar")
                                }
                            }

                        } else {

                            SplashScreen()
                        }
                    }

                    Pantalla.LOGIN -> {

                        LoginScreen(
                            onLoginCorrecto = { usuario ->

                                usuarioPendiente = usuario

                                reproducirDoblePitido()

                                mostrarDialogoLogin = true
                            },

                            onRegistroClick = {
                                pantallaActual = Pantalla.REGISTRO
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
                                pantallaActual = Pantalla.LOGIN
                            }
                        )
                    }

                    Pantalla.RECUPERAR_PASSWORD -> {

                        RecuperarPasswordScreen(
                            onVolverLogin = {
                                pantallaActual = Pantalla.LOGIN
                            }
                        )
                    }

                    Pantalla.HOME -> {

                        usuarioActual?.let { usuario ->

                            HomeScreen(
                                usuario = usuario,

                                onCerrarSesion = {

                                    sesionManager.cerrarSesion()

                                    usuarioActual = null
                                    usuarioPendiente = null

                                    pantallaActual = Pantalla.LOGIN
                                },

                                onCotizarClick = {
                                    pantallaActual = Pantalla.COTIZAR
                                },

                                onComunicacionClick = {
                                    pantallaActual =
                                        Pantalla.COMUNICACION_ACCESIBLE
                                },

                                onAdministrarClick = {
                                    pantallaActual =
                                        Pantalla.ADMINISTRAR_FIGURAS
                                }
                            )
                        }
                    }

                    Pantalla.COTIZAR -> {

                        CotizarScreen(
                            onVolver = {
                                pantallaActual = Pantalla.HOME
                            }
                        )
                    }

                    Pantalla.COMUNICACION_ACCESIBLE -> {

                        ComunicacionAccesibleScreen(
                            onVolver = {
                                pantallaActual = Pantalla.HOME
                            }
                        )
                    }

                    Pantalla.ADMINISTRAR_FIGURAS -> {

                        AdministrarFigurasScreen(
                            onVolver = {
                                pantallaActual = Pantalla.HOME
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
                            mostrarDialogoLogin = false
                            usuarioPendiente = null
                        },

                        title = {
                            Text("¡Bienvenido al Multiverso!")
                        },

                        text = {
                            Text(
                                "Inicio de sesión correcto.\n\n" +
                                        "Hola, ${usuarioPendiente!!.nombre}."
                            )
                        },

                        confirmButton = {

                            TextButton(
                                onClick = {

                                    val usuario = usuarioPendiente

                                    if (usuario != null) {

                                        sesionManager.guardarSesion(
                                            usuario.usuario
                                        )

                                        usuarioActual = usuario
                                        pantallaActual = Pantalla.HOME
                                    }

                                    usuarioPendiente = null
                                    mostrarDialogoLogin = false
                                }
                            ) {
                                Text("Continuar")
                            }
                        }
                    )
                }

                if (mostrarDialogoRegistro) {

                    AlertDialog(
                        onDismissRequest = {},

                        title = {
                            Text("¡Cuenta creada!")
                        },

                        text = {
                            Text(
                                "Tu usuario fue registrado correctamente. " +
                                        "Ahora puedes iniciar sesión."
                            )
                        },

                        confirmButton = {

                            TextButton(
                                onClick = {
                                    mostrarDialogoRegistro = false
                                    pantallaActual = Pantalla.LOGIN
                                }
                            ) {
                                Text("Ir al Login")
                            }
                        }
                    )
                }
            }
        }
    }

    private fun reproducirDoblePitido() {

        val toneGenerator = ToneGenerator(
            AudioManager.STREAM_NOTIFICATION,
            100
        )

        val handler = Handler(
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
