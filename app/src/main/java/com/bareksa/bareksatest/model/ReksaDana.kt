package com.bareksa.bareksatest.model

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import java.util.*

data class ReksaDana(
    val id: String,
    @ColorRes val color: Int,
    @DrawableRes val image: Int,
    val nama: String,
    val jenis: JenisReksaDana,
    val imbalHasil: ImbalHasil,
    val danaKelolaan: String,
    val minPembelian: String,
    val jangkaWaktuTahun: Int,
    val tingkatResiko: TingkatResiko,
    val tanggalPeluncuran: Date
)
