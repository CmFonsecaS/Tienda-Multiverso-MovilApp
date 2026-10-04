package com.example.tiendamultiverso.data.local

import android.content.Context

class SesionManager(context: Context) {

    private val preferencias = context.applicationContext
        .getSharedPreferences(
            "sesion_usuario",
            Context.MODE_PRIVATE
        )

    fun guardarSesion(usuario: String) {
        preferencias.edit()
            .putString("usuario_activo", usuario)
            .apply()
    }

    fun obtenerUsuarioActivo(): String? {
        return preferencias.getString(
            "usuario_activo",
            null
        )
    }

    fun cerrarSesion() {
        preferencias.edit()
            .remove("usuario_activo")
            .apply()
    }
}