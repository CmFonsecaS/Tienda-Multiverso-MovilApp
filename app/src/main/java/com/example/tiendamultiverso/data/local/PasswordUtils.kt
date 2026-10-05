
package com.example.tiendamultiverso.data.local

import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import java.security.MessageDigest

object PasswordUtils {

    private const val ITERACIONES = 210_000
    private const val LONGITUD_HASH = 256
    private const val LONGITUD_SAL = 16

    fun generarSal(): String {
        val bytes = ByteArray(LONGITUD_SAL)
        SecureRandom().nextBytes(bytes)
        return Base64.getEncoder().encodeToString(bytes)
    }

    fun generarHash(password: String, sal: String): String {
        val salBytes = Base64.getDecoder().decode(sal)

        val especificacion = PBEKeySpec(
            password.toCharArray(),
            salBytes,
            ITERACIONES,
            LONGITUD_HASH
        )

        return try {
            val fabrica = SecretKeyFactory.getInstance(
                "PBKDF2WithHmacSHA256"
            )

            val hash = fabrica.generateSecret(
                especificacion
            ).encoded

            Base64.getEncoder().encodeToString(hash)

        } finally {
            especificacion.clearPassword()
        }
    }

    fun verificarPassword(
        password: String,
        sal: String,
        hashGuardado: String
    ): Boolean {
        val hashCalculado = generarHash(password, sal)

        return MessageDigest.isEqual(
            Base64.getDecoder().decode(hashCalculado),
            Base64.getDecoder().decode(hashGuardado)
        )
    }
}
