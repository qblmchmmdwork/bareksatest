package com.bareksa.bareksatest.ui.grafik

import com.bareksa.bareksatest.repository.RepositoryResource

interface GrafikDataProvider {
    suspend fun getGrafikDataSinceLastYear(id: String): RepositoryResource<List<GrafikFragment.GrafikEntry>>
}