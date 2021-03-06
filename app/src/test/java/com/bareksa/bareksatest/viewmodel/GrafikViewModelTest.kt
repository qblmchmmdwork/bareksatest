package com.bareksa.bareksatest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bareksa.bareksatest.repository.RepositoryResource
import com.bareksa.bareksatest.ui.grafik.GrafikDataProvider
import com.bareksa.bareksatest.ui.grafik.GrafikFragment
import com.bareksa.bareksatest.util.CoroutineDispatcherProvider
import com.bareksa.bareksatest.viewmodel.grafik.GrafikState
import com.bareksa.bareksatest.viewmodel.grafik.GrafikViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

@ExperimentalCoroutinesApi
class GrafikViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testCoroutineDispatcher = TestCoroutineDispatcher()
    private val coroutineDispatcherProvider =
        CoroutineDispatcherProvider(testCoroutineDispatcher, testCoroutineDispatcher)
    private lateinit var dataProvider: GrafikDataProvider
    private lateinit var sut: GrafikViewModel
    private lateinit var observerMock: Observer<GrafikState>

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
        dataProvider = mockk(relaxed = true)
        sut = GrafikViewModel(
            listOf("1", "2"),
            coroutineDispatcherProvider,
            dataProvider,
        )
        observerMock = mockk(relaxed = true)
        sut.state.observeForever(observerMock)
    }

    @Test
    fun `load success, should update with correct state`() {
        val dummy = List(3){
            GrafikFragment.GrafikEntry(Date(), it.toFloat())
        }
        coEvery { dataProvider.getGrafikDataSinceLastYear("1") } returns
                RepositoryResource.Success(dummy)
        coEvery { dataProvider.getGrafikDataSinceLastYear("2") } returns
                RepositoryResource.Success(dummy)

        sut.load()

        coVerify(ordering = Ordering.ORDERED) {
            dataProvider.getGrafikDataSinceLastYear ("1")
            dataProvider.getGrafikDataSinceLastYear ("2")
        }

        verify(ordering = Ordering.ORDERED) {
            observerMock.onChanged(GrafikState(loading = false, error = null, data = null))
            observerMock.onChanged(GrafikState(loading = true, error = null, data = null))
            observerMock.onChanged(GrafikState(loading = false, error = null, data = listOf("1" to dummy,"2" to dummy)))
        }
    }

    @Test
    fun `load error, should update with correct state`() {
        coEvery { dataProvider.getGrafikDataSinceLastYear("1") } returns
                RepositoryResource.Error("Timeout")

        sut.load()


        coVerify(exactly = 1) { dataProvider.getGrafikDataSinceLastYear ("1") }


        verify(ordering = Ordering.ORDERED) {
            observerMock.onChanged(GrafikState(loading = false, error = null, data = null))
            observerMock.onChanged(GrafikState(loading = true, error = null, data = null))
            observerMock.onChanged(GrafikState(loading = false, error = "Timeout", data = null))
        }
    }
}