package com.bareksa.bareksatest.repository

import com.bareksa.bareksatest.datasource.remote.DanaKelolaanRemoteDataSource
import com.bareksa.bareksatest.datasource.remote.RemoteResource
import com.bareksa.bareksatest.model.DanaKelolaan
import com.bareksa.bareksatest.model.TimeUnit
import com.bareksa.bareksatest.ui.grafik.GrafikDataProvider
import com.bareksa.bareksatest.ui.grafik.GrafikFragment

class DanaKelolaanRepository(private val remoteDataSource: DanaKelolaanRemoteDataSource) :
    GrafikDataProvider {
    suspend fun getSinceLastYear() : RepositoryResource<List<DanaKelolaan>> {
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