package com.bareksa.bareksatest.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.bareksa.bareksatest.generateReksaDanaDummies
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
    private val reksaDanaDummies = generateReksaDanaDummies(3)

    @Before
    fun setup() {
        Dispatchers.setMain(testCoroutineDispatcher)
        dataProvider = mockk(relaxed = true)
        sut = GrafikViewModel(
            "",
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
        coEvery { dataProvider.getGrafikDataSinceLastYear("") } returns
                RepositoryResource.Success(dummy)

        sut.load()

        coVerify(exactly = 1) { dataProvider.getGrafikDataSinceLastYear ("") }

        verify(ordering = Ordering.ORDERED) {
            observerMock.onChanged(GrafikState(loading = false, error = null, data = null))
            observerMock.onChanged(GrafikState(loading = true, error = null, data = null))
            observerMock.onChanged(GrafikState(loading = false, error = null, data = dummy))
        }
    }

    @Test
    fun `load error, should update with correct state`() {
        coEvery { dataProvider.getGrafikDataSinceLastYear("") } returns
                RepositoryResource.Error("Timeout")

        sut.load()


        coVerify(exactly = 1) { dataProvider.getGrafikDataSinceLastYear ("") }


        verify(ordering = Ordering.ORDERED) {
            observerMock.onChanged(GrafikState(loading = false, error = null, data = null))
            observerMock.onChanged(GrafikState(loading = true, error = null, data = null))
            observerMock.onChanged(GrafikState(loading = false, error = "Timeout", data = null))
        }
    }
}