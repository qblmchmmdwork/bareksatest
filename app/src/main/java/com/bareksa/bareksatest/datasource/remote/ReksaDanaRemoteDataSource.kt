package com.bareksa.bareksatest.datasource.remote

import com.bareksa.bareksatest.model.ReksaDana

interface ReksaDanaRemoteDataSource {
    suspend fun getReksaDanaById(id: String): RemoteResource<ReksaDana>
}
