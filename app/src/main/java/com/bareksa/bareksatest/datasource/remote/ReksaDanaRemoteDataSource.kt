package com.bareksa.bareksatest.datasource.remote

import com.bareksa.bareksatest.model.ReksaDana

interface ReksaDanaRemoteDataSource {
    suspend fun getById(id: String): RemoteResource<ReksaDana>
}
