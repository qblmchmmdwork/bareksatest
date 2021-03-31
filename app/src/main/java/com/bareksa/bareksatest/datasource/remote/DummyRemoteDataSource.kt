package com.bareksa.bareksatest.datasource.remote

import com.bareksa.bareksatest.R
import com.bareksa.bareksatest.model.*
import kotlinx.coroutines.delay
import java.util.*
import kotlin.math.pow

class DummyRemoteDataSource : ReksaDanaRemoteDataSource, ImbalHasilRemoteDataSource,
    DanaKelolaanRemoteDataSource {
    private val reksaDanaDummy = listOf(
        ReksaDana(
            id = BNI_ID,
            image = R.drawable.bni,
            nama = "BNI-AM Inspiring Equity Fund",
            jenis = JenisReksaDana.Saham,
            imbalHasil = "5,50% / 5 thn",
            danaKelolaan = "3,64 Miliar",
            minPembelian = "1 Juta",
            jangkaWaktuTahun = 5,
            tingkatResiko = TingkatResiko.Tinggi,
            tanggalPeluncuran = Date(1429117200000)
        ),
        ReksaDana(
            id = CIPTA_DANA_ID,
            image = R.drawable.ciptadana,
            nama = "Cipta Dana Cash",
            jenis = JenisReksaDana.PasarUang,
            imbalHasil = "6,29% / 5 thn",
            danaKelolaan = "215,97 Miliar",
            minPembelian = "100 Ribu",
            jangkaWaktuTahun = 1,
            tingkatResiko = TingkatResiko.Rendah,
            tanggalPeluncuran = Date(1452704400000)
        ),
        ReksaDana(
            id = ASCEND_ID,
            image = R.drawable.ascend,
            nama = "Ascend Reksa Dana Saham Equity Fund",
            jenis = JenisReksaDana.Saham,
            imbalHasil = "7,17% / 5 thn",
            danaKelolaan = "3,89 Triliun",
            minPembelian = "100 Ribu",
            jangkaWaktuTahun = 5,
            tingkatResiko = TingkatResiko.Tinggi,
            tanggalPeluncuran = Date(1171904400000)
        ),
    )
    private val bniImbalHasilDummy = List(100) {
        ImbalHasil( (it.toFloat() / 99f) * 40, Date(5_000_000_000 * it))
    }
    private val ciptaDanaImbalHasilDummy = List(100) {
        ImbalHasil( (it.toFloat() / 99f) * 25, Date(5_000_000_000 * it))
    }
    private val ascendImbalHasilDummy = List(100) {
        ImbalHasil( (it.toFloat() / 99f) * 15, Date(5_000_000_000 * it))
    }
    private val bniDanaKelolaanDummy = List(100) {
        DanaKelolaan( (it.toFloat().pow(2) / 99f.pow(2)) * 50, Date(5_000_000_000 * it))
    }
    private val ciptaDanaDanaKelolaanDummy = List(100) {
        DanaKelolaan( (it.toFloat() / 99f) * 60, Date(5_000_000_000 * it))
    }
    private val ascendDanaKelolaanDummy = List(100) {
        DanaKelolaan( (it.toFloat().pow(3) / 99f.pow(3)) * 70, Date(5_000_000_000 * it))
    }

    override suspend fun getReksaDanaById(id: String): RemoteResource<ReksaDana> {
        delay(1000) // simulate loading
        val reksaDana = reksaDanaDummy.find { it.id == id } ?: return RemoteResource.Failed(
            404,
            "Entity not found"
        )
        return RemoteResource.Success(reksaDana)
    }

    override suspend fun getImbalHasilByIdSince(
        id: String,
        since: Int,
        sinceTimeUnit: TimeUnit
    ): RemoteResource<List<ImbalHasil>> {
        delay(1000) // simulate loading
        return when (id) {
            BNI_ID -> RemoteResource.Success(bniImbalHasilDummy)
            CIPTA_DANA_ID -> RemoteResource.Success(ciptaDanaImbalHasilDummy)
            ASCEND_ID -> RemoteResource.Success(ascendImbalHasilDummy)
            else -> RemoteResource.Failed(404, "Entity not found")
        }
    }

    override suspend fun getDanaKelolaanByIdSince(
        id: String,
        since: Int,
        sinceTimeUnit: TimeUnit
    ): RemoteResource<List<DanaKelolaan>> {
        delay(1000) // simulate loading
        return when (id) {
            BNI_ID -> RemoteResource.Success(bniDanaKelolaanDummy)
            CIPTA_DANA_ID -> RemoteResource.Success(ciptaDanaDanaKelolaanDummy)
            ASCEND_ID -> RemoteResource.Success(ascendDanaKelolaanDummy)
            else -> RemoteResource.Failed(404, "Entity not found")
        }
    }

    companion object {
        const val BNI_ID = "BNI"
        const val CIPTA_DANA_ID = "CIPTADANA"
        const val ASCEND_ID = "ASCEND"
    }
}