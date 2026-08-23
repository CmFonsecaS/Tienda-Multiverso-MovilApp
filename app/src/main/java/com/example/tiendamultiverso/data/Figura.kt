package com.example.tiendamultiverso.data

data class Figura(
    val id: Int,
    val nombre: String,
    val linea: String,
    val precio: Int,
    val stock: Int,
    val imagen: Int? = null
)

