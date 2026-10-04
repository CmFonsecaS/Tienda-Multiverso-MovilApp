package com.example.tiendamultiverso.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import com.example.tiendamultiverso.data.local.TiendaDatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FiguraRepository {

    private val figurasIniciales = listOf(

        Figura(
            id = 1,
            nombre = "Spider-Man",
            linea = "Marvel Legends",
            categoria = "Héroe",
            precio = 29990,
            stock = 8
        ),

        Figura(
            id = 2,
            nombre = "Venom",
            linea = "Marvel Legends",
            categoria = "Villano",
            precio = 34990,
            stock = 5
        ),

        Figura(
            id = 3,
            nombre = "Wolverine",
            linea = "Marvel Legends",
            categoria = "Héroe",
            precio = 32990,
            stock = 6
        ),

        Figura(
            id = 4,
            nombre = "Deadpool",
            linea = "Marvel Legends",
            categoria = "Antihéroe",
            precio = 31990,
            stock = 4
        ),

        Figura(
            id = 5,
            nombre = "Iron Man",
            linea = "Marvel Legends",
            categoria = "Héroe",
            precio = 36990,
            stock = 7
        ),

        Figura(
            id = 6,
            nombre = "Hulk",
            linea = "Marvel Legends",
            categoria = "Héroe",
            precio = 39990,
            stock = 3
        )
    )

    val figuras = mutableStateListOf<Figura>().apply {
        addAll(figurasIniciales)
    }

    suspend fun inicializar(context: Context) {

        val contexto = context.applicationContext

        // Consultamos y actualizamos SQLite en segundo plano.
        val figurasGuardadas = withContext(Dispatchers.IO) {

            val preferencias = contexto.getSharedPreferences(
                "catalogo_config",
                Context.MODE_PRIVATE
            )

            TiendaDatabaseHelper(contexto).use { baseDatos ->

                if (
                    !preferencias.getBoolean(
                        "catalogo_inicializado",
                        false
                    )
                ) {

                    val guardadas =
                        baseDatos.obtenerFiguras()

                    val idsExistentes =
                        guardadas.map { it.id }.toSet()

                    figurasIniciales.forEach { figura ->

                        if (figura.id !in idsExistentes) {
                            baseDatos.insertarFigura(figura)
                        }
                    }

                    val resultado =
                        baseDatos.obtenerFiguras()

                    val idsFinales =
                        resultado.map { it.id }.toSet()

                    if (
                        figurasIniciales.all {
                            it.id in idsFinales
                        }
                    ) {

                        preferencias.edit()
                            .putBoolean(
                                "catalogo_inicializado",
                                true
                            )
                            .apply()
                    }
                }

                // Devolvemos una lista normal.
                baseDatos.obtenerFiguras()
            }
        }

        // Actualizamos el estado observable en Main.
        withContext(Dispatchers.Main) {

            figuras.clear()
            figuras.addAll(figurasGuardadas)
        }
    }
}