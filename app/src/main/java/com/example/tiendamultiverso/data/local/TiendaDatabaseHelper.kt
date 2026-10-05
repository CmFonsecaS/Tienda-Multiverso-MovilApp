
package com.example.tiendamultiverso.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.tiendamultiverso.data.Figura
import com.example.tiendamultiverso.data.Usuario

class TiendaDatabaseHelper(
    context: Context,
    nombreBaseDatos: String = NOMBRE_BASE_DATOS
) : SQLiteOpenHelper(
    context,
    nombreBaseDatos,
    null,
    VERSION_BASE_DATOS
) {

    companion object {
        private const val NOMBRE_BASE_DATOS = "tienda_multiverso.db"
        private const val VERSION_BASE_DATOS = 2
    }

    override fun onCreate(db: SQLiteDatabase) {

        val crearTablaFiguras = """
            CREATE TABLE figuras (
                id INTEGER PRIMARY KEY,
                nombre TEXT NOT NULL,
                linea TEXT NOT NULL,
                categoria TEXT NOT NULL,
                precio INTEGER NOT NULL,
                stock INTEGER NOT NULL
            )
        """.trimIndent()

        db.execSQL(crearTablaFiguras)
        crearTablaUsuarios(db)
    }

    private fun crearTablaUsuarios(db: SQLiteDatabase) {

        val crearUsuarios = """
            CREATE TABLE IF NOT EXISTS usuarios (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre TEXT NOT NULL,
                apellido TEXT NOT NULL,
                usuario TEXT NOT NULL COLLATE NOCASE UNIQUE,
                email TEXT NOT NULL COLLATE NOCASE UNIQUE,
                password_hash TEXT NOT NULL,
                password_salt TEXT NOT NULL
            )
        """.trimIndent()

        db.execSQL(crearUsuarios)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (oldVersion < 2) {
            crearTablaUsuarios(db)
        }
    }

    fun insertarFigura(figura: Figura): Boolean {

        val valores = ContentValues().apply {
            put("id", figura.id)
            put("nombre", figura.nombre)
            put("linea", figura.linea)
            put("categoria", figura.categoria)
            put("precio", figura.precio)
            put("stock", figura.stock)
        }

        val resultado = writableDatabase.insert(
            "figuras",
            null,
            valores
        )

        return resultado != -1L
    }

    fun obtenerFiguras(): List<Figura> {

        val figuras = mutableListOf<Figura>()

        val cursor = readableDatabase.rawQuery(
            """
                SELECT id, nombre, linea, categoria, precio, stock
                FROM figuras
                ORDER BY id
            """.trimIndent(),
            null
        )

        cursor.use {
            while (it.moveToNext()) {
                figuras.add(
                    Figura(
                        id = it.getInt(0),
                        nombre = it.getString(1),
                        linea = it.getString(2),
                        categoria = it.getString(3),
                        precio = it.getInt(4),
                        stock = it.getInt(5)
                    )
                )
            }
        }

        return figuras
    }

    fun actualizarFigura(figura: Figura): Boolean {

        val valores = ContentValues().apply {
            put("nombre", figura.nombre)
            put("linea", figura.linea)
            put("categoria", figura.categoria)
            put("precio", figura.precio)
            put("stock", figura.stock)
        }

        val filasActualizadas = writableDatabase.update(
            "figuras",
            valores,
            "id = ?",
            arrayOf(figura.id.toString())
        )

        return filasActualizadas > 0
    }

    fun eliminarFigura(id: Int): Boolean {

        val filasEliminadas = writableDatabase.delete(
            "figuras",
            "id = ?",
            arrayOf(id.toString())
        )

        return filasEliminadas > 0
    }

    fun insertarUsuario(usuario: Usuario): Boolean {

        val sal = PasswordUtils.generarSal()

        val hash = PasswordUtils.generarHash(
            usuario.password,
            sal
        )

        val valores = ContentValues().apply {
            put("nombre", usuario.nombre)
            put("apellido", usuario.apellido)
            put("usuario", usuario.usuario)
            put("email", usuario.email)
            put("password_hash", hash)
            put("password_salt", sal)
        }

        val resultado = writableDatabase.insert(
            "usuarios",
            null,
            valores
        )

        return resultado != -1L
    }

    fun validarUsuario(
        identificador: String,
        password: String
    ): Usuario? {

        val cursor = readableDatabase.query(
            "usuarios",
            arrayOf(
                "nombre",
                "apellido",
                "usuario",
                "email",
                "password_hash",
                "password_salt"
            ),
            "usuario = ? COLLATE NOCASE OR email = ? COLLATE NOCASE",
            arrayOf(identificador, identificador),
            null,
            null,
            null,
            "1"
        )

        cursor.use {
            if (!it.moveToFirst()) {
                return null
            }

            val hashGuardado = it.getString(4)
            val salGuardada = it.getString(5)

            val passwordCorrecta = PasswordUtils.verificarPassword(
                password,
                salGuardada,
                hashGuardado
            )

            if (!passwordCorrecta) {
                return null
            }

            return Usuario(
                nombre = it.getString(0),
                apellido = it.getString(1),
                usuario = it.getString(2),
                email = it.getString(3),
                password = ""
            )
        }
    }
}
