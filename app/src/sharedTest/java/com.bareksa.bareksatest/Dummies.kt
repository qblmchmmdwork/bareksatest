package com.bareksa.bareksatest

import com.bareksa.bareksatest.model.ImbalHasil
import com.bareksa.bareksatest.model.JenisReksaDana
import com.bareksa.bareksatest.model.ReksaDana
import com.bareksa.bareksatest.model.TingkatResiko
import java.util.*

fun generateReksaDanaDummies(size: Int) = List(size) {
    ReksaDana(
        id = "$it",
        nama = "Reksa Dana $it",
        jenis = JenisReksaDana.Saham,
        imbalHasil = ImbalHasil(
            nilai = 5.50,
            perTahun = 5
        ),
        danaKelolaan = 3_640_000_000,
        minPembelian = 1_000_000,
        jangkaWaktuTahun = 5,
        tingkatResiko = TingkatResiko.Tinggi,
        tanggalPeluncuran = Date()
    )
}