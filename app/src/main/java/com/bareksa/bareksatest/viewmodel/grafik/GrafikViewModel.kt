package com.bareksa.bareksatest.viewmodel.grafik

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bareksa.bareksatest.repository.RepositoryResource
import com.bareksa.bareksatest.ui.grafik.GrafikDataProvider
import com.bareksa.bareksatest.util.CoroutineDispatcherProvider
import kotlinx.coroutines.*

class GrafikViewModel(
    private val ids : List<String>,
    private val dispatcher: CoroutineDispatcherProvider,
    private val dataProvider: GrafikDataProvider
): ViewModel() {
    private val _state = MutableLiveData<GrafikState>().apply { value = GrafikState() }
    val state: LiveData<GrafikState> = _state

    fun load() {
        _state.value = _state.value?.copy(loading = true, error = null)
        viewModelScope.launch {
            val loadAllAsync = ids.map {
                async { dataProvider.getGrafikDataSinceLastYear(it) }
            }.toTypedArray()
            val result = withContext(dispatcher.io) { awaitAll(*loadAllAsync)}
            try {
                val success = result.map { it as RepositoryResource.Success }
                val data = success.map { it.data }
                _state.value = _state.value?.copy(loading = false, data = ids.zip(data))
            } catch (e: Exception) {
                val failedResult =
                    result.find { it is RepositoryResource.Error } as RepositoryResource.Error
                _state.value = _state.value?.copy(loading = false, error = failedResult.message)
                return@launch
            }
        }
    }
}