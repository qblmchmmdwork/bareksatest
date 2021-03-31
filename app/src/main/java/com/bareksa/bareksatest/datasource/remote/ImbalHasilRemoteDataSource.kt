package com.bareksa.bareksatest.datasource.remote

import com.bareksa.bareksatest.model.ImbalHasil
import com.bareksa.bareksatest.model.TimeUnit

interface ImbalHasilRemoteDataSource {

    suspend fun getSince(value: Int, unit: TimeUnit): RemoteResource<List<ImbalHasil>>
}