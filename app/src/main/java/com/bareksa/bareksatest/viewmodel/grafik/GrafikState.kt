package com.bareksa.bareksatest.viewmodel.grafik

import com.bareksa.bareksatest.ui.grafik.GrafikFragment

data class GrafikState(
    val loading: Boolean = false,
    val error: String? = null,
    val data: List<Pair<String, List<GrafikFragment.GrafikEntry>>>  ? = null
)