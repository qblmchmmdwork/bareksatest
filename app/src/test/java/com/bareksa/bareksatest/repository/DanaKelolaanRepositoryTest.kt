package com.bareksa.bareksatest.repository

import com.bareksa.bareksatest.datasource.remote.DanaKelolaanRemoteDataSource
import com.bareksa.bareksatest.datasource.remote.RemoteResource
import com.bareksa.bareksatest.model.DanaKelolaan
import com.bareksa.bareksatest.model.TimeUnit
import com.bareksa.bareksatest.ui.grafik.GrafikFragment
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException
import java.util.*


class DanaKelolaanRepositoryTest {
    private lateinit var remoteDataSourceMock: DanaKelolaanRemoteDataSource
    private lateinit var sut: DanaKelolaanRepository

    @Before
    fun setup() {
        remoteDataSourceMock = mockk(relaxed = true)
        sut = DanaKelolaanRepository(remoteDataSourceMock)
    }

    @Test
    fun `getSinceLastYear success, should return correct data`() {
        val dummy = List(3) {
            DanaKelolaan(it.toFloat(), Date())
        }
        coEvery { remoteDataSourceMock.getDanaKelolaanByIdSince("", 1, TimeUnit.Year) } returns
                RemoteResource.Success(dummy)

        val res = runBlocking { sut.getSinceLastYear("") } as RepositoryResource.Success

        coVerify(exactly = 1) { remoteDataSourceMock.getDanaKelolaanByIdSince("", 1, TimeUnit.Year) }
        Assert.assertEquals(dummy, res.data)
    }

    @Test
    fun `getGrafikDataSinceLastYear success, should return correct data`() {
        val dummy = List(3) {
            DanaKelolaan(it.toFloat(), Date())
        }
        coEvery { remoteDataSourceMock.getDanaKelolaanByIdSince("", 1, TimeUnit.Year) } returns
                RemoteResource.Success(dummy)

        val res = runBlocking { sut.getGrafikDataSinceLastYear("") } as RepositoryResource.Success

        coVerify(exactly = 1) { remoteDataSourceMock.getDanaKelolaanByIdSince("", 1, TimeUnit.Year) }
        Assert.assertEquals(
            dummy.map { GrafikFragment.GrafikEntry(it.date, it.value) },
            res.data.map { GrafikFragment.GrafikEntry(it.date, it.value) })
    }

    @Test
    fun `getSinceLastYear failed, should return correct data`() {
        coEvery { remoteDataSourceMock.getDanaKelolaanByIdSince("", 1, TimeUnit.Year) } returns
                RemoteResource.Failed(401, "Unauthorized")

        val res = runBlocking { sut.getSinceLastYear("") } as RepositoryResource.Error

        coVerify(exactly = 1) { remoteDataSourceMock.getDanaKelolaanByIdSince("", 1, TimeUnit.Year) }
        Assert.assertEquals("Unauthorized", res.message)
    }

    @Test
    fun `getGrafikDataSinceLastYear failed, should return correct data`() {
        coEvery { remoteDataSourceMock.getDanaKelolaanByIdSince("", 1, TimeUnit.Year) } returns
                RemoteResource.Failed(401, "Unauthorized")

        val res = runBlocking { sut.getGrafikDataSinceLastYear("") } as RepositoryResource.Error

        coVerify(exactly = 1) { remoteDataSourceMock.getDanaKelolaanByIdSince("", 1, TimeUnit.Year) }
        Assert.assertEquals("Unauthorized", res.message)
    }

    @Test
    fun `getSinceLastYear error, should return correct data`() {
        coEvery { remoteDataSourceMock.getDanaKelolaanByIdSince("", 1, TimeUnit.Year) } returns
                RemoteResource.Error("Timeout", SocketTimeoutException())

        val res = runBlocking { sut.getSinceLastYear("") } as RepositoryResource.Error

        coVerify(exactly = 1) { remoteDataSourceMock.getDanaKelolaanByIdSince("", 1, TimeUnit.Year) }
        Assert.assertEquals("Timeout", res.message)
    }
}