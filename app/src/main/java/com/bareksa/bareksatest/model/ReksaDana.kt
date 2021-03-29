package com.bareksa.bareksatest.model

import java.util.*

data class ReksaDana(
    val id: String,
    val nama: String,
    val jenis: JenisReksaDana,
    val imbalHasil: ImbalHasil,
    val danaKelolaan: Long,
    val minPembelian: Int,
    val jangkaWaktuTahun: Int,
    val tingkatResiko: TingkatResiko,
    val tanggalPeluncuran: Date
)
