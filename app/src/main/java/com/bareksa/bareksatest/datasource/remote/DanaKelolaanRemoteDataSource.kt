package com.bareksa.bareksatest.datasource.remote

import com.bareksa.bareksatest.model.DanaKelolaan
import com.bareksa.bareksatest.model.TimeUnit

interface DanaKelolaanRemoteDataSource {
    suspend fun getSince(value: Int, unit: TimeUnit): RemoteResource<List<DanaKelolaan>>
}