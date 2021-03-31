package com.bareksa.bareksatest

import com.bareksa.bareksatest.model.JenisReksaDana
import com.bareksa.bareksatest.model.ReksaDana
import com.bareksa.bareksatest.model.TingkatResiko
import java.util.*

fun generateReksaDanaDummies(size: Int) = List(size) {
    val index = it+1
    val drawable = when (it % 3) {
        0 -> R.drawable.bni
        1 -> R.drawable.ciptadana
        else -> R.drawable.ascend
    }
    ReksaDana(
        id = "$index",
        image = drawable,
        nama = "Reksa Dana $index",
        jenis = JenisReksaDana.values()[index % JenisReksaDana.values().size],
        imbalHasil = "$index% / $index thn",
        danaKelolaan = "$index Miliar",
        minPembelian = "$index Juta",
        jangkaWaktuTahun = 5 * index,
        tingkatResiko = TingkatResiko.Tinggi,
        tanggalPeluncuran = Date(500000000L * index)
    )
}