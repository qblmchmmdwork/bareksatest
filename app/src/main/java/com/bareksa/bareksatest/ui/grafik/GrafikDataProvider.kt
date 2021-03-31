package com.bareksa.bareksatest.ui.grafik

import com.bareksa.bareksatest.repository.RepositoryResource

interface GrafikDataProvider {
    suspend fun getGrafikDataSinceLastYear(): RepositoryResource<List<GrafikFragment.GrafikEntry>>
}