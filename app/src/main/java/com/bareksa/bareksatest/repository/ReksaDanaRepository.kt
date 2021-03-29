package com.bareksa.bareksatest.repository

import com.bareksa.bareksatest.datasource.remote.ReksaDanaRemoteDataSource
import com.bareksa.bareksatest.datasource.remote.RemoteResource
import com.bareksa.bareksatest.model.ReksaDana

class ReksaDanaRepository(private val remoteDataSource: ReksaDanaRemoteDataSource) {
    suspend fun getById(id: String) : RepositoryResource<ReksaDana> {
        return when(val res = remoteDataSource.getById(id)) {
            is RemoteResource.Success -> RepositoryResource.Success(res.data)
            is RemoteResource.Failed -> RepositoryResource.Error(res.message)
            is RemoteResource.Error -> RepositoryResource.Error(res.message)
        }
    }
}
