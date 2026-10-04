package com.example.tiendamultiverso

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.tiendamultiverso.data.Figura
import com.example.tiendamultiverso.data.local.TiendaDatabaseHelper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import com.example.tiendamultiverso.data.Usuario
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull

@RunWith(AndroidJUnit4::class)
class TiendaDatabaseInstrumentedTest {

    @Test
    fun comprobarCrudFiguras() {

        val contexto =
            InstrumentationRegistry.getInstrumentation().targetContext

        val nombreBaseDatos = "tienda_multiverso_prueba.db"

        contexto.deleteDatabase(nombreBaseDatos)

        val databaseHelper = TiendaDatabaseHelper(
            contexto,
            nombreBaseDatos
        )

        try {
            // CREATE: registrar una figura
            val figura = Figura(
                id = 100,
                nombre = "Spider-Man Test",
                linea = "Marvel Legends",
                categoria = "Héroe",
                precio = 29990,
                stock = 5
            )

            assertTrue(databaseHelper.insertarFigura(figura))

            // READ: consultar la figura
            val figurasGuardadas = databaseHelper.obtenerFiguras()

            assertEquals(1, figurasGuardadas.size)
            assertEquals("Spider-Man Test", figurasGuardadas.first().nombre)
            assertEquals(29990, figurasGuardadas.first().precio)

            // UPDATE: modificar precio y stock
            val figuraActualizada = figura.copy(
                precio = 34990,
                stock = 8
            )

            assertTrue(databaseHelper.actualizarFigura(figuraActualizada))

            val figurasActualizadas = databaseHelper.obtenerFiguras()

            assertEquals(34990, figurasActualizadas.first().precio)
            assertEquals(8, figurasActualizadas.first().stock)

            // DELETE: eliminar la figura
            assertTrue(databaseHelper.eliminarFigura(figura.id))

            val figurasFinales = databaseHelper.obtenerFiguras()

            assertTrue(figurasFinales.isEmpty())

        } finally {
            databaseHelper.close()
            contexto.deleteDatabase(nombreBaseDatos)
        }
    }

    @Test
    fun comprobarPersistenciaFigura() {

        val contexto =
            InstrumentationRegistry.getInstrumentation().targetContext

        val nombreBaseDatos = "tienda_persistencia_prueba.db"

        contexto.deleteDatabase(nombreBaseDatos)

        val figura = Figura(
            id = 200,
            nombre = "Iron Man Test",
            linea = "Marvel Legends",
            categoria = "Héroe",
            precio = 36990,
            stock = 5
        )

        val primeraConexion = TiendaDatabaseHelper(
            contexto,
            nombreBaseDatos
        )

        try {
            assertTrue(primeraConexion.insertarFigura(figura))

            val figuraModificada = figura.copy(stock = 10)

            assertTrue(
                primeraConexion.actualizarFigura(figuraModificada)
            )
        } finally {
            primeraConexion.close()
        }

        val segundaConexion = TiendaDatabaseHelper(
            contexto,
            nombreBaseDatos
        )

        try {
            val figurasGuardadas = segundaConexion.obtenerFiguras()

            assertEquals(1, figurasGuardadas.size)
            assertEquals(200, figurasGuardadas.first().id)
            assertEquals(10, figurasGuardadas.first().stock)

        } finally {
            segundaConexion.close()
            contexto.deleteDatabase(nombreBaseDatos)
        }
    }


    @Test
    fun comprobarRegistroYLoginUsuario() {

        val contexto =
            InstrumentationRegistry.getInstrumentation().targetContext

        val nombreBaseDatos = "tienda_usuarios_prueba.db"

        contexto.deleteDatabase(nombreBaseDatos)

        val baseDatos = TiendaDatabaseHelper(
            contexto,
            nombreBaseDatos
        )

        try {
            val usuarioPrueba = Usuario(
                nombre = "Carlos",
                apellido = "Prueba",
                usuario = "carlos_test",
                email = "carlos_test@example.com",
                password = "ClaveTemporal123!"
            )

            assertTrue(baseDatos.insertarUsuario(usuarioPrueba))

            val usuarioEncontrado = baseDatos.validarUsuario(
                "carlos_test",
                "ClaveTemporal123!"
            )

            assertNotNull(usuarioEncontrado)
            assertEquals("Carlos", usuarioEncontrado?.nombre)
            assertEquals("carlos_test", usuarioEncontrado?.usuario)

            val usuarioPorCorreo = baseDatos.validarUsuario(
                "carlos_test@example.com",
                "ClaveTemporal123!"
            )

            assertNotNull(usuarioPorCorreo)

            val loginIncorrecto = baseDatos.validarUsuario(
                "carlos_test",
                "ClaveIncorrecta"
            )

            assertNull(loginIncorrecto)

            val cursor = baseDatos.readableDatabase.rawQuery(
                "SELECT password_hash, password_salt FROM usuarios WHERE usuario = ?",
                arrayOf("carlos_test")
            )

            cursor.use {
                assertTrue(it.moveToFirst())

                val hashGuardado = it.getString(0)
                val salGuardada = it.getString(1)

                assertTrue(hashGuardado.isNotBlank())
                assertTrue(salGuardada.isNotBlank())
                assertTrue(hashGuardado != usuarioPrueba.password)
            }

        } finally {
            baseDatos.close()
            contexto.deleteDatabase(nombreBaseDatos)
        }
    }

}