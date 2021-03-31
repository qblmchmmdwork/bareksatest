package com.bareksa.bareksatest.viewmodel.grafik

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bareksa.bareksatest.repository.RepositoryResource
import com.bareksa.bareksatest.ui.grafik.GrafikDataProvider
import com.bareksa.bareksatest.util.CoroutineDispatcherProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GrafikViewModel(
    private val id : String,
    private val dispatcher: CoroutineDispatcherProvider,
    private val dataProvider: GrafikDataProvider
): ViewModel() {
    private val _state = MutableLiveData<GrafikState>().apply { value = GrafikState() }
    val state: LiveData<GrafikState> = _state

    fun load() {
        _state.value = _state.value?.copy(loading = true, error = null)
        viewModelScope.launch {
            when(val res =
                withContext(dispatcher.io) { dataProvider.getGrafikDataSinceLastYear(id) }) {
                is RepositoryResource.Error -> _state.value = _state.value?.copy(loading = false, error = res.message)
                is RepositoryResource.Success -> _state.value = _state.value?.copy(loading = false, data = res.data)
            }
        }
    }
}