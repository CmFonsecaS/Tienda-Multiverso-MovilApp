package com.example.tiendamultiverso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamultiverso.data.Usuario
import com.example.tiendamultiverso.data.UsuarioRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onRegistroExitoso: () -> Unit = {},
    onVolverLogin: () -> Unit = {}
) {

    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmarPassword by remember { mutableStateOf("") }

    // Combo Box
    val paises = listOf(
        "Chile",
        "Argentina",
        "Perú",
        "Colombia",
        "México"
    )

    var paisSeleccionado by remember {
        mutableStateOf("Chile")
    }

    var menuExpandido by remember {
        mutableStateOf(false)
    }

    // Radio Buttons
    val tiposColeccionista = listOf(
        "Principiante",
        "Coleccionista",
        "Premium"
    )

    var tipoSeleccionado by remember {
        mutableStateOf("Principiante")
    }

    // Checkboxes de intereses
    var marvelLegends by remember {
        mutableStateOf(true)
    }

    var figurasRetro by remember {
        mutableStateOf(false)
    }

    var figurasExclusivas by remember {
        mutableStateOf(false)
    }

    var edicionesEspeciales by remember {
        mutableStateOf(false)
    }

    var aceptaTerminos by remember {
        mutableStateOf(false)
    }

    var mensaje by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp)
    ) {

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text = "Crear cuenta",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Únete a Tienda Multiverso"
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                mensaje = ""
            },
            label = {
                Text("Nombre")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = apellido,
            onValueChange = {
                apellido = it
                mensaje = ""
            },
            label = {
                Text("Apellido")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = usuario,
            onValueChange = {
                usuario = it
                mensaje = ""
            },
            label = {
                Text("Usuario")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                mensaje = ""
            },
            label = {
                Text("Correo electrónico")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                mensaje = ""
            },
            label = {
                Text("Contraseña")
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedTextField(
            value = confirmarPassword,
            onValueChange = {
                confirmarPassword = it
                mensaje = ""
            },
            label = {
                Text("Confirmar contraseña")
            },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "País",
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        ExposedDropdownMenuBox(
            expanded = menuExpandido,
            onExpandedChange = {
                menuExpandido = !menuExpandido
            }
        ) {

            OutlinedTextField(
                value = paisSeleccionado,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Selecciona tu país")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = menuExpandido
                    )
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = menuExpandido,
                onDismissRequest = {
                    menuExpandido = false
                }
            ) {

                paises.forEach { pais ->

                    DropdownMenuItem(
                        text = {
                            Text(pais)
                        },
                        onClick = {
                            paisSeleccionado = pais
                            menuExpandido = false
                        }
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Tipo de coleccionista",
            fontWeight = FontWeight.SemiBold
        )

        tiposColeccionista.forEach { tipo ->

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = tipoSeleccionado == tipo,
                    onClick = {
                        tipoSeleccionado = tipo
                    }
                )

                Text(
                    text = tipo
                )
            }
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "¿Qué figuras te interesan?",
            fontWeight = FontWeight.SemiBold
        )

        InteresCheckbox(
            texto = "Marvel Legends",
            checked = marvelLegends,
            onCheckedChange = {
                marvelLegends = it
            }
        )

        InteresCheckbox(
            texto = "Figuras retro",
            checked = figurasRetro,
            onCheckedChange = {
                figurasRetro = it
            }
        )

        InteresCheckbox(
            texto = "Figuras exclusivas",
            checked = figurasExclusivas,
            onCheckedChange = {
                figurasExclusivas = it
            }
        )

        InteresCheckbox(
            texto = "Ediciones especiales",
            checked = edicionesEspeciales,
            onCheckedChange = {
                edicionesEspeciales = it
            }
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Checkbox(
                checked = aceptaTerminos,
                onCheckedChange = {
                    aceptaTerminos = it
                    mensaje = ""
                }
            )

            Text(
                text = "Acepto los términos y condiciones"
            )
        }

        if (mensaje.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = mensaje
            )
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {

                if (
                    nombre.isBlank() ||
                    apellido.isBlank() ||
                    usuario.isBlank() ||
                    email.isBlank() ||
                    password.isBlank() ||
                    confirmarPassword.isBlank()
                ) {

                    mensaje = "Debes completar todos los campos."

                    return@Button
                }

                if (password != confirmarPassword) {

                    mensaje = "Las contraseñas no coinciden."

                    return@Button
                }

                if (!aceptaTerminos) {

                    mensaje =
                        "Debes aceptar los términos y condiciones."

                    return@Button
                }

                val nuevoUsuario = Usuario(
                    nombre = nombre.trim(),
                    apellido = apellido.trim(),
                    usuario = usuario.trim(),
                    email = email.trim(),
                    password = password
                )

                val registrado =
                    UsuarioRepository.registrarUsuario(
                        nuevoUsuario
                    )

                if (registrado) {

                    onRegistroExitoso()

                } else {

                    mensaje =
                        "El usuario o correo ya se encuentra registrado."
                }
            }
        ) {

            Text(
                text = "Crear cuenta"
            )
        }

        TextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onVolverLogin
        ) {

            Text(
                text = "Ya tengo una cuenta · Iniciar sesión"
            )
        }

        Spacer(
            modifier = Modifier.height(30.dp)
        )
    }
}


@Composable
private fun InteresCheckbox(
    texto: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )

        Text(
            text = texto
        )
    }
}

