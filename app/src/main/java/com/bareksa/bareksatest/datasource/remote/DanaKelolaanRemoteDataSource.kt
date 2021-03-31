package com.bareksa.bareksatest.datasource.remote

import com.bareksa.bareksatest.model.DanaKelolaan
import com.bareksa.bareksatest.model.TimeUnit

interface DanaKelolaanRemoteDataSource {
    suspend fun getDanaKelolaanByIdSince(id: String, since: Int, sinceTimeUnit: TimeUnit): RemoteResource<List<DanaKelolaan>>
}