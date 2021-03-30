package com.bareksa.bareksatest.viewmodel.perbandingan

import com.bareksa.bareksatest.model.ReksaDana

data class PerbandinganState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<ReksaDana>? = null
)
