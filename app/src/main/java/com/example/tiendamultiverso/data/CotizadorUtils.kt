package com.example.tiendamultiverso.data

import java.util.Locale

/*
 * FUNCIÓN DE EXTENSIÓN
 *
 * Permite mostrar un precio Int en formato chileno.
 *
 * Ejemplo:
 * 29990.formatoPrecio()
 * devuelve:
 * $29.990
 */
fun Int.formatoPrecio(): String {

    val localeChile = Locale.forLanguageTag("es-CL")

    return "$${
        String.format(
            localeChile,
            "%,d",
            this
        )
    }"
}


/*
 * FUNCIÓN DE ORDEN SUPERIOR + INLINE
 *
 * Recibe una Figura, una cantidad y además
 * recibe otra función como parámetro.
 *
 * La lambda enviada decide cómo calcular
 * el resultado.
 */
inline fun calcularCotizacion(
    figura: Figura,
    cantidad: Int,
    operacion: (Figura, Int) -> Int
): Int {

    return operacion(
        figura,
        cantidad
    )
}


/*
 * FUNCIÓN DE ORDEN SUPERIOR
 *
 * Recibe una colección de figuras y
 * una función lambda que indica qué
 * elementos deben conservarse.
 */
fun filtrarFiguras(
    figuras: List<Figura>,
    condicion: (Figura) -> Boolean
): List<Figura> {

    return figuras.filter(condicion)
}