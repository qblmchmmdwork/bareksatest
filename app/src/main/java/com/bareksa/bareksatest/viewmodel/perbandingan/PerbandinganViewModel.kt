package com.bareksa.bareksatest.viewmodel.perbandingan

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bareksa.bareksatest.repository.ReksaDanaRepository
import com.bareksa.bareksatest.repository.RepositoryResource
import com.bareksa.bareksatest.util.CoroutineDispatcherProvider
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerbandinganViewModel(
    private val dispatcher: CoroutineDispatcherProvider,
    private val reksaDanaRepository: ReksaDanaRepository,
    private val reksaDanaId1: String,
    private val reksaDanaId2: String,
    private val reksaDanaId3: String,
) : ViewModel() {
    private val _state = MutableLiveData<PerbandinganState>().apply { value = PerbandinganState() }
    val state: LiveData<PerbandinganState> = _state

    fun load() {
        if(_state.value?.loading == true) return
        _state.value = _state.value?.copy(loading = true, error = null)
        viewModelScope.launch {
            val result = withContext(dispatcher.io) {
                val load1 = async { reksaDanaRepository.getById(reksaDanaId1) }
                val load2 = async { reksaDanaRepository.getById(reksaDanaId2) }
                val load3 = async { reksaDanaRepository.getById(reksaDanaId3) }
                awaitAll(load1, load2, load3)

            }
            try {
                val success = result.map { it as RepositoryResource.Success }
                val data = PerbandinganState.ReksaDanaComparisonGroup(
                    success[0].data,
                    success[1].data,
                    success[2].data,
                )
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
