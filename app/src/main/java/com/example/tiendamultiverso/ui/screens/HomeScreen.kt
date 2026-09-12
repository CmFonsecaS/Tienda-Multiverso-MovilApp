package com.example.tiendamultiverso.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiendamultiverso.data.FiguraRepository
import com.example.tiendamultiverso.data.Usuario
import com.example.tiendamultiverso.ui.components.FiguraCard
import com.example.tiendamultiverso.ui.components.TablaInventario
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    usuario: Usuario,
    onCerrarSesion: () -> Unit = {},
    onCotizarClick: () -> Unit = {},
    onComunicacionClick: () -> Unit = {}
) {

    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed
    )

    val scope = rememberCoroutineScope()

    var mostrarMenuOpciones by remember {
        mutableStateOf(false)
    }

    var mostrarPerfil by remember {
        mutableStateOf(false)
    }

    var mostrarAcercaDe by remember {
        mutableStateOf(false)
    }

    ModalNavigationDrawer(

        drawerState = drawerState,

        drawerContent = {

            ModalDrawerSheet(
                modifier = Modifier
                    .width(280.dp)
                    .fillMaxHeight()
            ) {

                Spacer(
                    modifier = Modifier.height(32.dp)
                )

                Text(
                    text = "Tienda Multiverso",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(
                        horizontal = 20.dp
                    )
                )

                Spacer(
                    modifier = Modifier.height(6.dp)
                )

                Text(
                    text = "${usuario.nombre} ${usuario.apellido}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(
                        horizontal = 20.dp
                    )
                )

                Text(
                    text = usuario.email,
                    fontSize = 13.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(
                        horizontal = 20.dp
                    )
                )

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                HorizontalDivider()

                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                TextButton(
                    modifier = Modifier.fillMaxWidth(),

                    onClick = {

                        scope.launch {
                            drawerState.close()
                        }
                    }
                ) {

                    Text(
                        text = "Catálogo",
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }


                TextButton(
                    modifier = Modifier.fillMaxWidth(),

                    onClick = {

                        scope.launch {
                            drawerState.close()
                        }

                        onCotizarClick()
                    }
                ) {

                    Text(
                        text = "Cotizar figuras",
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

    ) {

        Scaffold(

            topBar = {

                TopAppBar(

                    title = {

                        Text(
                            text = "Tienda Multiverso",
                            fontWeight = FontWeight.Bold
                        )
                    },

                    navigationIcon = {

                        TextButton(
                            onClick = {

                                scope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {

                            Text(
                                text = "☰",
                                color = Color.White,
                                fontSize = 26.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },

                    actions = {

                        Column {

                            TextButton(
                                onClick = {
                                    mostrarMenuOpciones = true
                                }
                            ) {

                                Text(
                                    text = "⋮",
                                    color = Color.White,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            DropdownMenu(
                                expanded = mostrarMenuOpciones,

                                onDismissRequest = {
                                    mostrarMenuOpciones = false
                                }
                            ) {

                                DropdownMenuItem(

                                    text = {
                                        Text("Mi perfil")
                                    },

                                    onClick = {

                                        mostrarMenuOpciones = false
                                        mostrarPerfil = true
                                    }
                                )

                                DropdownMenuItem(

                                    text = {
                                        Text("Acerca de")
                                    },

                                    onClick = {

                                        mostrarMenuOpciones = false
                                        mostrarAcercaDe = true
                                    }
                                )

                                DropdownMenuItem(

                                    text = {

                                        Text(
                                            text = "Cerrar sesión",
                                            color =
                                                MaterialTheme.colorScheme.primary
                                        )
                                    },

                                    onClick = {

                                        mostrarMenuOpciones = false
                                        onCerrarSesion()
                                    }
                                )
                            }
                        }
                    },

                    colors =
                        TopAppBarDefaults.topAppBarColors(

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
                    .padding(horizontal = 20.dp)
            ) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "Hola",
                            fontSize = 14.sp
                        )

                        Text(
                            text =
                                "${usuario.nombre} ${usuario.apellido}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Catálogo de figuras",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                ) {

                    items(
                        items = FiguraRepository.figuras,
                        key = { figura ->
                            figura.id
                        }
                    ) { figura ->

                        FiguraCard(
                            figura = figura,
                            modifier = Modifier.padding(6.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text(
                    text = "Resumen de inventario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                TablaInventario(
                    figuras =
                        FiguraRepository.figuras.take(3)
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )


                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(
                            minHeight = 56.dp
                        ),

                    onClick = onComunicacionClick
                ) {

                    Text(
                        text = "Comunicación accesible",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onCerrarSesion
                ) {

                    Text(
                        text = "Cerrar sesión"
                    )
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )
            }
        }
    }


    if (mostrarPerfil) {

        AlertDialog(

            onDismissRequest = {
                mostrarPerfil = false
            },

            title = {
                Text(
                    text = "Mi perfil"
                )
            },

            text = {

                Column {

                    Text(
                        text =
                            "Nombre: ${usuario.nombre} ${usuario.apellido}"
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Usuario: ${usuario.usuario}"
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Correo: ${usuario.email}"
                    )
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        mostrarPerfil = false
                    }
                ) {

                    Text(
                        text = "Cerrar"
                    )
                }
            }
        )
    }


    if (mostrarAcercaDe) {

        AlertDialog(

            onDismissRequest = {
                mostrarAcercaDe = false
            },

            title = {
                Text(
                    text = "Acerca de"
                )
            },

            text = {

                Text(
                    text =
                        "Tienda Multiverso\n\n" +
                                "Un espacio creado para fans del universo Marvel.\n\n" +
                                "Aquí podrás descubrir figuras de tus personajes favoritos, " +
                                "cotizar productos y realizar compras de forma cómoda y simple."
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        mostrarAcercaDe = false
                    }
                ) {

                    Text(
                        text = "Aceptar"
                    )
                }
            }
        )
    }
}