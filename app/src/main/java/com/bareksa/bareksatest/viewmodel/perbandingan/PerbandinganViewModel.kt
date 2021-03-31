package com.bareksa.bareksatest.viewmodel.perbandingan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bareksa.bareksatest.repository.ReksaDanaRepository
import com.bareksa.bareksatest.repository.RepositoryResource
import com.bareksa.bareksatest.util.CoroutineDispatcherProvider
import kotlinx.coroutines.*

class PerbandinganViewModel(
    private val dispatcher: CoroutineDispatcherProvider,
    private val reksaDanaRepository: ReksaDanaRepository,
) : ViewModel() {
    private val _state = MutableLiveData<PerbandinganState>().apply { value = PerbandinganState() }
    val state: LiveData<PerbandinganState> = _state

    fun load(reksaDanaIds: List<String>) {
        if(_state.value?.loading == true) return
        _state.value = _state.value?.copy(loading = true, error = null)
        viewModelScope.launch {
            val result = withContext(dispatcher.io) {
                val loadAllAsync = reksaDanaIds.map { async { reksaDanaRepository.getById(it) } }.toTypedArray()
                awaitAll(*loadAllAsync)
            }
            try {
                val success = result.map { it as RepositoryResource.Success }
                val data = success.map { it.data }
                _state.value = _state.value?.copy(loading = false, data = data)
            } catch (e: Exception) {
                val failedResult =
                    result.find { it is RepositoryResource.Error } as RepositoryResource.Error
                _state.value = _state.value?.copy(loading = false, error = failedResult.message)
                return@launch
            }
        }
    }
}
