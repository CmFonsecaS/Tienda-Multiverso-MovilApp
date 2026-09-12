package com.example.tiendamultiverso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamultiverso.data.Figura
import com.example.tiendamultiverso.data.FiguraRepository
import com.example.tiendamultiverso.data.calcularCotizacion
import com.example.tiendamultiverso.data.filtrarFiguras
import com.example.tiendamultiverso.data.formatoPrecio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CotizarScreen(
    onVolver: () -> Unit = {}
) {

    var filtroActual by remember {
        mutableStateOf("Todas")
    }

    var figuraSeleccionada by remember {
        mutableStateOf<Figura?>(null)
    }

    var cantidadTexto by remember {
        mutableStateOf("")
    }

    var total by remember {
        mutableStateOf<Int?>(null)
    }

    var mensajeError by remember {
        mutableStateOf("")
    }

    val categorias = listOf(
        "Todas",
        "Héroe",
        "Villano",
        "Antihéroe"
    )

    /*
     * LAMBDA + FILTER SOBRE COLECCIONES
     */
    val figurasFiltradas = when (filtroActual) {

        "Héroe" -> {

            filtrarFiguras(
                FiguraRepository.figuras
            ) { figura ->

                figura.categoria == "Héroe"
            }
        }

        "Villano" -> {

            filtrarFiguras(
                FiguraRepository.figuras
            ) { figura ->

                figura.categoria == "Villano"
            }
        }

        "Antihéroe" -> {

            filtrarFiguras(
                FiguraRepository.figuras
            ) { figura ->

                figura.categoria == "Antihéroe"
            }
        }

        else -> {

            FiguraRepository.figuras
        }
    }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = "Cotizar figuras",
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
                text = "Cotiza tus figuras",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text =
                    "Selecciona una categoría, elige una figura y calcula el valor según la cantidad."
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "Filtrar figuras",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                categorias.forEach { categoria ->

                    FilterChip(

                        selected =
                            filtroActual == categoria,

                        onClick = {

                            filtroActual = categoria
                            figuraSeleccionada = null
                            total = null
                            cantidadTexto = ""
                            mensajeError = ""
                        },

                        label = {

                            Text(
                                text = categoria
                            )
                        }
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            figurasFiltradas.forEach { figura ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = 6.dp
                        )
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = figura.nombre,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text =
                                "${figura.linea} · ${figura.categoria}"
                        )

                        Text(
                            text =
                                "Precio: ${figura.precio.formatoPrecio()}"
                        )

                        Text(
                            text =
                                "Stock: ${figura.stock}"
                        )

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )

                        OutlinedButton(

                            modifier =
                                Modifier.sizeIn(
                                    minHeight = 48.dp
                                ),

                            onClick = {

                                figuraSeleccionada =
                                    figura

                                total = null

                                mensajeError = ""
                            }
                        ) {

                            Text(
                                text = "Seleccionar"
                            )
                        }
                    }
                }
            }

            figuraSeleccionada?.let { figura ->

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

                Text(
                    text =
                        "Figura seleccionada: ${figura.nombre}",
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text =
                        "Precio unitario: ${figura.precio.formatoPrecio()}"
                )

                Text(
                    text =
                        "Stock disponible: ${figura.stock}"
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                OutlinedTextField(

                    value = cantidadTexto,

                    onValueChange = {

                        cantidadTexto = it
                        mensajeError = ""
                        total = null
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {

                        Text(
                            text = "Cantidad"
                        )
                    },

                    singleLine = true,

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Number
                        )
                )

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

                        /*
                         * GESTIÓN DE EXCEPCIONES
                         */
                        try {

                            val cantidad =
                                cantidadTexto.toInt()

                            if (cantidad <= 0) {

                                throw IllegalArgumentException(
                                    "La cantidad debe ser mayor que cero."
                                )
                            }

                            if (
                                cantidad >
                                figura.stock
                            ) {

                                throw IllegalArgumentException(
                                    "La cantidad supera el stock disponible."
                                )
                            }

                            /*
                             * FUNCIÓN DE ORDEN SUPERIOR + INLINE
                             *
                             * La lambda define la operación
                             * utilizada para calcular el total.
                             */
                            total =
                                calcularCotizacion(
                                    figura,
                                    cantidad
                                ) { item, unidades ->

                                    item.precio * unidades
                                }

                            mensajeError = ""

                        } catch (
                            e: NumberFormatException
                        ) {

                            mensajeError =
                                "Ingresa una cantidad válida."

                            total = null

                        } catch (
                            e: IllegalArgumentException
                        ) {

                            mensajeError =
                                e.message
                                    ?: "No se pudo calcular la cotización."

                            total = null
                        }
                    }
                ) {

                    Text(
                        text = "Calcular cotización"
                    )
                }

                if (
                    mensajeError.isNotEmpty()
                ) {

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    Text(
                        text = mensajeError,
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }

                total?.let { valorTotal ->

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    Card(
                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier =
                                Modifier.padding(18.dp)
                        ) {

                            Text(
                                text = "Total",
                                fontSize = 16.sp
                            )

                            Text(
                                text =
                                    valorTotal.formatoPrecio(),
                                fontSize = 28.sp,
                                fontWeight =
                                    FontWeight.Bold,
                                color =
                                    MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onVolver
            ) {

                Text(
                    text = "Volver al Home"
                )
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}