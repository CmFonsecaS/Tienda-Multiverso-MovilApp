
package com.example.tiendamultiverso.data

import android.content.Context
import com.example.tiendamultiverso.data.local.TiendaDatabaseHelper

object UsuarioRepository {

    private val usuariosIniciales = listOf(
        Usuario(
            nombre = "Cristian",
            apellido = "Fonseca",
            usuario = "cristian",
            email = "cristian@multiverso.cl",
            password = "123456"
        ),
        Usuario(
            nombre = "Peter",
            apellido = "Parker",
            usuario = "peter",
            email = "peter@multiverso.cl",
            password = "spider123"
        ),
        Usuario(
            nombre = "Logan",
            apellido = "Howlett",
            usuario = "logan",
            email = "logan@multiverso.cl",
            password = "wolverine123"
        ),
        Usuario(
            nombre = "Wade",
            apellido = "Wilson",
            usuario = "wade",
            email = "wade@multiverso.cl",
            password = "deadpool123"
        ),
        Usuario(
            nombre = "Tony",
            apellido = "Stark",
            usuario = "tony",
            email = "tony@multiverso.cl",
            password = "ironman123"
        )
    )

    fun inicializar(context: Context) {

        val preferencias = context.applicationContext
            .getSharedPreferences(
                "usuarios_config",
                Context.MODE_PRIVATE
            )

        if (
            preferencias.getBoolean(
                "usuarios_inicializados",
                false
            )
        ) {
            return
        }

        TiendaDatabaseHelper(context.applicationContext).use {
                baseDatos ->

            val usuariosRegistrados = mutableSetOf<String>()

            baseDatos.readableDatabase.rawQuery(
                "SELECT usuario FROM usuarios",
                null
            ).use { cursor ->

                while (cursor.moveToNext()) {
                    usuariosRegistrados.add(
                        cursor.getString(0).lowercase()
                    )
                }
            }

            usuariosIniciales.forEach { usuario ->

                if (
                    usuario.usuario.lowercase()
                    !in usuariosRegistrados
                ) {
                    baseDatos.insertarUsuario(usuario)
                }
            }

            val cargaCompleta =
                baseDatos.readableDatabase.rawQuery(
                    "SELECT usuario FROM usuarios",
                    null
                ).use { cursor ->

                    val nombresGuardados =
                        mutableSetOf<String>()

                    while (cursor.moveToNext()) {
                        nombresGuardados.add(
                            cursor.getString(0).lowercase()
                        )
                    }

                    usuariosIniciales.all { usuario ->
                        usuario.usuario.lowercase() in
                                nombresGuardados
                    }
                }

            if (cargaCompleta) {
                preferencias.edit()
                    .putBoolean(
                        "usuarios_inicializados",
                        true
                    )
                    .apply()
            }
        }
    }

    fun validarLogin(
        context: Context,
        usuario: String,
        password: String
    ): Usuario? {

        TiendaDatabaseHelper(context.applicationContext).use {
                baseDatos ->

            return baseDatos.validarUsuario(
                usuario.trim(),
                password
            )
        }
    }

    fun buscarPorEmail(
        context: Context,
        email: String
    ): Usuario? {

        TiendaDatabaseHelper(context.applicationContext).use {
                baseDatos ->

            baseDatos.readableDatabase.query(
                "usuarios",
                arrayOf(
                    "nombre",
                    "apellido",
                    "usuario",
                    "email"
                ),
                "email = ? COLLATE NOCASE",
                arrayOf(email.trim()),
                null,
                null,
                null
            ).use { cursor ->

                return if (cursor.moveToFirst()) {
                    Usuario(
                        nombre = cursor.getString(0),
                        apellido = cursor.getString(1),
                        usuario = cursor.getString(2),
                        email = cursor.getString(3),
                        password = ""
                    )
                } else {
                    null
                }
            }
        }
    }

    fun buscarPorUsuario(
        context: Context,
        identificador: String
    ): Usuario? {

        TiendaDatabaseHelper(context.applicationContext).use {
                baseDatos ->

            baseDatos.readableDatabase.query(
                "usuarios",
                arrayOf(
                    "nombre",
                    "apellido",
                    "usuario",
                    "email"
                ),
                "usuario = ? COLLATE NOCASE",
                arrayOf(identificador.trim()),
                null,
                null,
                null
            ).use { cursor ->

                return if (cursor.moveToFirst()) {
                    Usuario(
                        nombre = cursor.getString(0),
                        apellido = cursor.getString(1),
                        usuario = cursor.getString(2),
                        email = cursor.getString(3),
                        password = ""
                    )
                } else {
                    null
                }
            }
        }
    }

    fun registrarUsuario(
        context: Context,
        usuario: Usuario
    ): Boolean {

        TiendaDatabaseHelper(context.applicationContext).use {
                baseDatos ->

            return baseDatos.insertarUsuario(usuario)
        }
    }
}
