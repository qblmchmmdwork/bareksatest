package com.bareksa.bareksatest.datasource.remote

import com.bareksa.bareksatest.model.ImbalHasil
import com.bareksa.bareksatest.model.TimeUnit

interface ImbalHasilRemoteDataSource {

    suspend fun getImbalHasilByIdSince(id: String, since: Int, sinceTimeUnit: TimeUnit): RemoteResource<List<ImbalHasil>>
}