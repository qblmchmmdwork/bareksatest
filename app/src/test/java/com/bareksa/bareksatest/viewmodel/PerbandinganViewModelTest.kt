package com.bareksa.bareksatest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bareksa.bareksatest.generateReksaDanaDummies
import com.bareksa.bareksatest.repository.ReksaDanaRepository
import com.bareksa.bareksatest.repository.RepositoryResource
import com.bareksa.bareksatest.util.CoroutineDispatcherProvider
import com.bareksa.bareksatest.viewmodel.perbandingan.PerbandinganState
import com.bareksa.bareksatest.viewmodel.perbandingan.PerbandinganViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PerbandinganViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val coroutineDispatcherProvider =
        CoroutineDispatcherProvider(testCoroutineDispatcher, testCoroutineDispatcher)
    private lateinit var reksaDanaRepositoryMock: ReksaDanaRepository
    private lateinit var sut: PerbandinganViewModel
    private lateinit var observerMock: Observer<PerbandinganState>
    private val reksaDanaDummies = generateReksaDanaDummies(3)

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
        reksaDanaRepositoryMock = mockk(relaxed = true)
        sut = PerbandinganViewModel(
            coroutineDispatcherProvider,
            reksaDanaRepositoryMock,
            reksaDanaDummies[0].id,
            reksaDanaDummies[1].id,
            reksaDanaDummies[2].id,
        )
        observerMock = mockk(relaxed = true)
        sut.state.observeForever(observerMock)
    }

    @Test
    fun `load success, should update with correct state`() {
        coEvery { reksaDanaRepositoryMock.getById(reksaDanaDummies[0].id) } returns
                RepositoryResource.Success(reksaDanaDummies[0])
        coEvery { reksaDanaRepositoryMock.getById(reksaDanaDummies[1].id) } returns
                RepositoryResource.Success(reksaDanaDummies[1])
        coEvery { reksaDanaRepositoryMock.getById(reksaDanaDummies[2].id) } returns
                RepositoryResource.Success(reksaDanaDummies[2])

        sut.load()

        coVerify(ordering = Ordering.ORDERED) {
            reksaDanaRepositoryMock.getById(reksaDanaDummies[0].id)
            reksaDanaRepositoryMock.getById(reksaDanaDummies[1].id)
            reksaDanaRepositoryMock.getById(reksaDanaDummies[2].id)
        }

        verify(ordering = Ordering.ORDERED) {
            observerMock.onChanged(PerbandinganState(loading = false, error = null, data = null))
            observerMock.onChanged(PerbandinganState(loading = true, error = null, data = null))
            observerMock.onChanged(
                PerbandinganState(
                    loading = false, error = null, data =
                    PerbandinganState.ReksaDanaComparisonGroup(
                        reksaDanaDummies[0],
                        reksaDanaDummies[1],
                        reksaDanaDummies[2]
                    )
                )
            )
        }
    }

    @Test
    fun `load error, should update with correct state`() {
        coEvery { reksaDanaRepositoryMock.getById(reksaDanaDummies[0].id) } returns
                RepositoryResource.Success(reksaDanaDummies[0])
        coEvery { reksaDanaRepositoryMock.getById(reksaDanaDummies[1].id) } returns
                RepositoryResource.Error("Timeout")
        coEvery { reksaDanaRepositoryMock.getById(reksaDanaDummies[2].id) } returns
                RepositoryResource.Success(reksaDanaDummies[2])

        sut.load()

        coVerify(ordering = Ordering.ORDERED) {
            reksaDanaRepositoryMock.getById(reksaDanaDummies[0].id)
            reksaDanaRepositoryMock.getById(reksaDanaDummies[1].id)
            reksaDanaRepositoryMock.getById(reksaDanaDummies[2].id)
        }

        verify(ordering = Ordering.ORDERED) {
            observerMock.onChanged(PerbandinganState(loading = false, error = null, data = null))
            observerMock.onChanged(PerbandinganState(loading = true, error = null, data = null))
            observerMock.onChanged(
                PerbandinganState(
                    loading = false,
                    error = "Timeout",
                    data = null
                )
            )
        }
    }
}