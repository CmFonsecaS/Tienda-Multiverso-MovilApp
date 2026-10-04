package com.example.tiendamultiverso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.tiendamultiverso.data.Figura
import com.example.tiendamultiverso.data.FiguraRepository
import com.example.tiendamultiverso.data.local.TiendaDatabaseHelper
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdministrarFigurasScreen(
    onVolver: () -> Unit
) {

    val contexto = LocalContext.current.applicationContext
    val alcanceCorrutina = rememberCoroutineScope()

    var idSeleccionado by remember {
        mutableStateOf<Int?>(null)
    }

    var nombre by remember { mutableStateOf("") }
    var linea by remember {
        mutableStateOf("Marvel Legends")
    }
    var categoria by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    var procesando by remember {
        mutableStateOf(false)
    }

    var mostrarConfirmacion by remember {
        mutableStateOf(false)
    }

    fun limpiarFormulario() {
        idSeleccionado = null
        nombre = ""
        linea = "Marvel Legends"
        categoria = ""
        precio = ""
        stock = ""
    }

    fun cargarFigura(figura: Figura) {
        idSeleccionado = figura.id
        nombre = figura.nombre
        linea = figura.linea
        categoria = figura.categoria
        precio = figura.precio.toString()
        stock = figura.stock.toString()
        mensaje = "Editando ${figura.nombre}"
    }

    fun guardarFigura() {

        if (procesando) return

        val precioNumero = precio.toIntOrNull()
        val stockNumero = stock.toIntOrNull()

        if (
            nombre.isBlank() ||
            linea.isBlank() ||
            categoria.isBlank() ||
            precioNumero == null ||
            stockNumero == null ||
            precioNumero <= 0 ||
            stockNumero < 0
        ) {
            mensaje = "Revisa los datos ingresados."
            return
        }

        // Capturamos los valores del formulario
        // antes de ejecutar la corrutina.
        val figuraEnEdicion = idSeleccionado
        val nombreIngresado = nombre.trim()
        val lineaIngresada = linea.trim()
        val categoriaIngresada = categoria.trim()

        procesando = true
        mensaje = ""

        alcanceCorrutina.launch {

            try {

                val correcto = withContext(Dispatchers.IO) {

                    TiendaDatabaseHelper(contexto).use {
                            baseDatos ->

                        // Para figuras nuevas, calculamos
                        // el siguiente ID desde SQLite.
                        val nuevoId = figuraEnEdicion
                            ?: (
                                    (
                                            baseDatos.obtenerFiguras()
                                                .maxOfOrNull { it.id }
                                                ?: 0
                                            ) + 1
                                    )

                        val figura = Figura(
                            id = nuevoId,
                            nombre = nombreIngresado,
                            linea = lineaIngresada,
                            categoria = categoriaIngresada,
                            precio = precioNumero,
                            stock = stockNumero
                        )

                        if (figuraEnEdicion == null) {
                            baseDatos.insertarFigura(figura)
                        } else {
                            baseDatos.actualizarFigura(figura)
                        }
                    }
                }

                if (correcto) {

                    // El repositorio consulta SQLite en IO
                    // y actualiza Compose en Main.
                    FiguraRepository.inicializar(contexto)

                    limpiarFormulario()
                    mensaje = "Figura guardada correctamente."

                } else {

                    mensaje = "No se pudo guardar la figura."
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                mensaje = "Ocurrió un error al guardar."

            } finally {

                procesando = false
            }
        }
    }

    fun eliminarFigura() {

        if (procesando) return

        val id = idSeleccionado ?: return

        procesando = true
        mensaje = ""

        alcanceCorrutina.launch {

            try {

                val eliminado = withContext(Dispatchers.IO) {

                    TiendaDatabaseHelper(contexto).use {
                            baseDatos ->

                        baseDatos.eliminarFigura(id)
                    }
                }

                if (eliminado) {

                    FiguraRepository.inicializar(contexto)

                    limpiarFormulario()
                    mensaje = "Figura eliminada correctamente."

                } else {

                    mensaje = "No se encontró la figura."
                }

            } catch (e: CancellationException) {

                throw e

            } catch (e: Exception) {

                mensaje = "Ocurrió un error al eliminar."

            } finally {

                procesando = false
            }
        }
    }

    Scaffold(
        topBar = {

            TopAppBar(
                title = {
                    Text("Administrar figuras")
                },

                navigationIcon = {

                    TextButton(
                        onClick = onVolver,
                        enabled = !procesando
                    ) {

                        Text(
                            text = "‹ Volver",
                            color =
                                MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor =
                        MaterialTheme.colorScheme.primary,

                    titleContentColor =
                        MaterialTheme.colorScheme.onPrimary,

                    navigationIconContentColor =
                        MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),

            verticalArrangement =
                Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = if (idSeleccionado == null) {
                    "Agregar figura"
                } else {
                    "Modificar figura"
                },

                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !procesando
            )

            OutlinedTextField(
                value = linea,
                onValueChange = { linea = it },
                label = { Text("Línea") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !procesando
            )

            OutlinedTextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoría") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !procesando
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio (solo números)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !procesando
            )

            OutlinedTextField(
                value = stock,
                onValueChange = { stock = it },
                label = { Text("Stock (solo números)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                enabled = !procesando
            )

            Button(
                onClick = { guardarFigura() },
                enabled = !procesando,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    if (procesando) {
                        "Procesando..."
                    } else if (idSeleccionado == null) {
                        "Agregar figura"
                    } else {
                        "Guardar cambios"
                    }
                )
            }

            if (idSeleccionado != null) {

                OutlinedButton(
                    onClick = {
                        mostrarConfirmacion = true
                    },

                    enabled = !procesando,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Text(
                        text = "Eliminar figura",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                TextButton(
                    onClick = {
                        limpiarFormulario()
                        mensaje = ""
                    },
                    enabled = !procesando
                ) {

                    Text("Cancelar edición")
                }
            }

            if (mensaje.isNotEmpty()) {

                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            HorizontalDivider()

            Text(
                text = "Figuras registradas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            FiguraRepository.figuras.forEach { figura ->

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            text = figura.nombre,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Precio: $${figura.precio}"
                        )

                        Text(
                            text = "Stock: ${figura.stock}"
                        )
                    }

                    TextButton(
                        onClick = {
                            cargarFigura(figura)
                        },
                        enabled = !procesando
                    ) {

                        Text("Editar")
                    }
                }

                HorizontalDivider()
            }
        }
    }

    if (mostrarConfirmacion && idSeleccionado != null) {

        val nombreFigura = FiguraRepository.figuras
            .firstOrNull { it.id == idSeleccionado }
            ?.nombre ?: nombre

        AlertDialog(
            onDismissRequest = {
                if (!procesando) {
                    mostrarConfirmacion = false
                }
            },

            title = {
                Text("¿Eliminar figura?")
            },

            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar " +
                            "$nombreFigura? Esta acción no se puede deshacer."
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                        eliminarFigura()
                    },
                    enabled = !procesando
                ) {

                    Text(
                        text = "Sí, eliminar",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        mostrarConfirmacion = false
                    },
                    enabled = !procesando
                ) {

                    Text("Cancelar")
                }
            }
        )
    }
}