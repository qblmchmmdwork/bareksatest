package com.bareksa.bareksatest.viewmodel.perbandingan

import com.bareksa.bareksatest.model.ReksaDana

data class PerbandinganState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: ReksaDanaComparisonGroup? = null
) {
    data class ReksaDanaComparisonGroup(
        val reksaDana1: ReksaDana,
        val reksaDana2: ReksaDana,
        val reksaDana3: ReksaDana,
    )
}
