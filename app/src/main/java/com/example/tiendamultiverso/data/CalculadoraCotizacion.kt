package com.example.tiendamultiverso.data

object CalculadoraCotizacion {

    fun calcularSubtotal(
        precioUnitario: Int,
        cantidad: Int
    ): Long {

        require(precioUnitario >= 0) {
            "El precio no puede ser negativo"
        }

        require(cantidad > 0) {
            "La cantidad debe ser mayor que cero"
        }

        return precioUnitario.toLong() * cantidad
    }
}
