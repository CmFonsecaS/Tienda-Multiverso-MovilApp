package com.example.tiendamultiverso.data

object UsuarioRepository {

    val usuarios = mutableListOf(

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

    fun validarLogin(
        usuario: String,
        password: String
    ): Usuario? {
        return usuarios.find {
            (
                    it.usuario.equals(usuario, ignoreCase = true) ||
                            it.email.equals(usuario, ignoreCase = true)
                    ) && it.password == password
        }
    }

    fun buscarPorEmail(email: String): Usuario? {
        return usuarios.find {
            it.email.equals(email, ignoreCase = true)
        }
    }

    fun registrarUsuario(usuario: Usuario): Boolean {

        val existe = usuarios.any {
            it.usuario.equals(usuario.usuario, ignoreCase = true) ||
                    it.email.equals(usuario.email, ignoreCase = true)
        }

        return if (!existe) {
            usuarios.add(usuario)
            true
        } else {
            false
        }
    }
}

