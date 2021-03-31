package com.bareksa.bareksatest.repository

import com.bareksa.bareksatest.datasource.remote.ImbalHasilRemoteDataSource
import com.bareksa.bareksatest.datasource.remote.RemoteResource
import com.bareksa.bareksatest.model.ImbalHasil
import com.bareksa.bareksatest.model.TimeUnit
import com.bareksa.bareksatest.ui.grafik.GrafikDataProvider
import com.bareksa.bareksatest.ui.grafik.GrafikFragment

class ImbalHasilRepository(private val remoteDataSource: ImbalHasilRemoteDataSource) :
    GrafikDataProvider {
    suspend fun getSinceLastYear(): RepositoryResource<List<ImbalHasil>> {
        return when (val res = remoteDataSource.getSince(1, TimeUnit.Year)) {
            is RemoteResource.Success -> RepositoryResource.Success(res.data)
            is RemoteResource.Failed -> RepositoryResource.Error(res.message)
            is RemoteResource.Error -> RepositoryResource.Error(res.message, res.cause)
        }
    }

    override suspend fun getGrafikDataSinceLastYear(): RepositoryResource<List<GrafikFragment.GrafikEntry>> {
        return when (val res = getSinceLastYear()) {
            is RepositoryResource.Error -> res
            is RepositoryResource.Success -> RepositoryResource.Success(res.data.map {
                GrafikFragment.GrafikEntry(
                    it.date,
                    it.value
                )
            })
        }
    }
}