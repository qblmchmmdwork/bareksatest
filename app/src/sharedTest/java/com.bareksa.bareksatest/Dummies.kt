package com.bareksa.bareksatest

import com.bareksa.bareksatest.model.ImbalHasil
import com.bareksa.bareksatest.model.JenisReksaDana
import com.bareksa.bareksatest.model.ReksaDana
import com.bareksa.bareksatest.model.TingkatResiko
import java.util.*

fun generateReksaDanaDummies(size: Int) = List(size) {
    val index = it+1
    val (color, drawable) = when (it % 3) {
        0 -> R.color.green_50 to R.drawable.bni
        1 -> R.color.violet_50 to R.drawable.ciptadana
        else -> R.color.navy_50 to R.drawable.ascend
    }
    ReksaDana(
        id = "$index",
        color = color,
        image = drawable,
        nama = "Reksa Dana $index",
        jenis = JenisReksaDana.values()[index % JenisReksaDana.values().size],
        imbalHasil = ImbalHasil(
            nilai = 5.50 * index,
            perTahun = index
        ),
        danaKelolaan = "$index Miliar",
        minPembelian = "$index Juta",
        jangkaWaktuTahun = 5 * index,
        tingkatResiko = TingkatResiko.Tinggi,
        tanggalPeluncuran = Date(500000000L * index)
    )
}