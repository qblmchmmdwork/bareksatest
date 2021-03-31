package com.bareksa.bareksatest.repository

import com.bareksa.bareksatest.datasource.remote.ImbalHasilRemoteDataSource
import com.bareksa.bareksatest.datasource.remote.RemoteResource
import com.bareksa.bareksatest.model.ImbalHasil
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


class ImbalHasilRepositoryTest {
    private lateinit var remoteDataSourceMock: ImbalHasilRemoteDataSource
    private lateinit var sut: ImbalHasilRepository

    @Before
    fun setup() {
        remoteDataSourceMock = mockk(relaxed = true)
        sut = ImbalHasilRepository(remoteDataSourceMock)
    }

    @Test
    fun `getSinceLastYear success, should return correct data`() {
        val dummy = List(3) {
            ImbalHasil(it.toFloat(), Date())
        }
        coEvery { remoteDataSourceMock.getImbalHasilByIdSince("", 1, TimeUnit.Year) } returns
                RemoteResource.Success(dummy)

        val res = runBlocking { sut.getSinceLastYear("") } as RepositoryResource.Success

        coVerify(exactly = 1) { remoteDataSourceMock.getImbalHasilByIdSince("", 1, TimeUnit.Year) }
        Assert.assertEquals(dummy, res.data)
    }

    @Test
    fun `getGrafikDataSinceLastYear success, should return correct data`() {
        val dummy = List(3) {
            ImbalHasil(it.toFloat(), Date())
        }
        coEvery { remoteDataSourceMock.getImbalHasilByIdSince("", 1, TimeUnit.Year) } returns
                RemoteResource.Success(dummy)

        val res = runBlocking { sut.getGrafikDataSinceLastYear("") } as RepositoryResource.Success

        coVerify(exactly = 1) { remoteDataSourceMock.getImbalHasilByIdSince("", 1, TimeUnit.Year) }
        Assert.assertEquals(
            dummy.map { GrafikFragment.GrafikEntry(it.date, it.value) },
            res.data.map { GrafikFragment.GrafikEntry(it.date, it.value) })
    }

    @Test
    fun `getSinceLastYear failed, should return correct data`() {
        coEvery { remoteDataSourceMock.getImbalHasilByIdSince("", 1, TimeUnit.Year) } returns
                RemoteResource.Failed(401, "Unauthorized")

        val res = runBlocking { sut.getSinceLastYear("") } as RepositoryResource.Error

        coVerify(exactly = 1) { remoteDataSourceMock.getImbalHasilByIdSince("", 1, TimeUnit.Year) }
        Assert.assertEquals("Unauthorized", res.message)
    }

    @Test
    fun `getGrafikDataSinceLastYear failed, should return correct data`() {
        coEvery { remoteDataSourceMock.getImbalHasilByIdSince("", 1, TimeUnit.Year) } returns
                RemoteResource.Failed(401, "Unauthorized")

        val res = runBlocking { sut.getGrafikDataSinceLastYear("") } as RepositoryResource.Error

        coVerify(exactly = 1) { remoteDataSourceMock.getImbalHasilByIdSince("", 1, TimeUnit.Year) }
        Assert.assertEquals("Unauthorized", res.message)
    }

    @Test
    fun `getSinceLastYear error, should return correct data`() {
        coEvery { remoteDataSourceMock.getImbalHasilByIdSince("", 1, TimeUnit.Year) } returns
                RemoteResource.Error("Timeout", SocketTimeoutException())

        val res = runBlocking { sut.getSinceLastYear("") } as RepositoryResource.Error

        coVerify(exactly = 1) { remoteDataSourceMock.getImbalHasilByIdSince("", 1, TimeUnit.Year) }
        Assert.assertEquals("Timeout", res.message)
    }
}