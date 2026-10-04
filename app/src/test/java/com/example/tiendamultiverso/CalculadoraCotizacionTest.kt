
package com.example.tiendamultiverso

import com.example.tiendamultiverso.data.CalculadoraCotizacion
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CalculadoraCotizacionTest {

    @Test
    fun calcularSubtotalConDosFiguras() {

        val resultado =
            CalculadoraCotizacion.calcularSubtotal(
                precioUnitario = 29990,
                cantidad = 2
            )

        assertEquals(59980L, resultado)
    }

    @Test
    fun calcularSubtotalConUnaFigura() {

        val resultado =
            CalculadoraCotizacion.calcularSubtotal(
                precioUnitario = 34990,
                cantidad = 1
            )

        assertEquals(34990L, resultado)
    }

    @Test
    fun rechazarCantidadCero() {

        assertThrows(
            IllegalArgumentException::class.java
        ) {
            CalculadoraCotizacion.calcularSubtotal(
                precioUnitario = 29990,
                cantidad = 0
            )
        }
    }

    @Test
    fun rechazarPrecioNegativo() {

        assertThrows(
            IllegalArgumentException::class.java
        ) {
            CalculadoraCotizacion.calcularSubtotal(
                precioUnitario = -100,
                cantidad = 2
            )
        }
    }

    @Test
    fun calcularSubtotalConValoresGrandes() {

        val resultado =
            CalculadoraCotizacion.calcularSubtotal(
                precioUnitario = 1_500_000_000,
                cantidad = 2
            )

        assertEquals(3_000_000_000L, resultado)
    }
}
