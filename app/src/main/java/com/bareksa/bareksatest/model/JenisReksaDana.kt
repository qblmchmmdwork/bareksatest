package com.bareksa.bareksatest.model

enum class JenisReksaDana {
    Saham,
    PasarUang;

    override fun toString(): String {
        return when(this) {
            Saham -> "Saham"
            PasarUang -> "Pasar Uang"
        }
    }
}