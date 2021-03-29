package com.bareksa.bareksatest.repository

import com.bareksa.bareksatest.datasource.remote.ReksaDanaRemoteDataSource
import com.bareksa.bareksatest.datasource.remote.RemoteResource
import com.bareksa.bareksatest.generateReksaDanaDummies
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.net.SocketTimeoutException

class ReksaDanaRepositoryTest {
    private val reksaDanaDummy = generateReksaDanaDummies(1).first()
    private lateinit var remoteDataSourceMock : ReksaDanaRemoteDataSource
    private lateinit var sut : ReksaDanaRepository

    @Before
    fun setup() {
        remoteDataSourceMock = mockk(relaxed = true)
        sut = ReksaDanaRepository(remoteDataSourceMock)
    }

    @Test
    fun `getById success, should return correct data`() {
        coEvery { remoteDataSourceMock.getById(reksaDanaDummy.id) } returns RemoteResource.Success(reksaDanaDummy)

        val res = runBlocking { sut.getById(reksaDanaDummy.id) } as RepositoryResource.Success

        coVerify(exactly = 1) { remoteDataSourceMock.getById(reksaDanaDummy.id) }
        assertEquals(reksaDanaDummy, res.data)
    }

    @Test
    fun `getById failed, should return correct data`() {
        coEvery { remoteDataSourceMock.getById(reksaDanaDummy.id) } returns
                RemoteResource.Failed(401, "Unauthorized")

        val res = runBlocking { sut.getById(reksaDanaDummy.id) } as RepositoryResource.Error

        coVerify(exactly = 1) { remoteDataSourceMock.getById(reksaDanaDummy.id) }
        assertEquals("Unauthorized", res.message)
    }

    @Test
    fun `getById error, should return correct data`() {
        coEvery { remoteDataSourceMock.getById(reksaDanaDummy.id) } returns
                RemoteResource.Error("Timeout", SocketTimeoutException())

        val res = runBlocking { sut.getById(reksaDanaDummy.id) } as RepositoryResource.Error

        coVerify(exactly = 1) { remoteDataSourceMock.getById(reksaDanaDummy.id) }
        assertEquals("Timeout", res.message)
    }
}