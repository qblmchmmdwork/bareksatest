package com.bareksa.bareksatest.di

import com.bareksa.bareksatest.datasource.remote.DanaKelolaanRemoteDataSource
import com.bareksa.bareksatest.datasource.remote.DummyRemoteDataSource
import com.bareksa.bareksatest.datasource.remote.ImbalHasilRemoteDataSource
import com.bareksa.bareksatest.datasource.remote.ReksaDanaRemoteDataSource
import com.bareksa.bareksatest.repository.DanaKelolaanRepository
import com.bareksa.bareksatest.repository.ImbalHasilRepository
import com.bareksa.bareksatest.repository.ReksaDanaRepository
import com.bareksa.bareksatest.ui.grafik.GrafikFragment
import com.bareksa.bareksatest.util.CoroutineDispatcherProvider
import com.bareksa.bareksatest.viewmodel.grafik.GrafikViewModel
import com.bareksa.bareksatest.viewmodel.perbandingan.PerbandinganViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.binds
import org.koin.dsl.module

val appModule = module {
    single { CoroutineDispatcherProvider(Dispatchers.Main, Dispatchers.IO) }
    single { DummyRemoteDataSource() } binds arrayOf(
        ReksaDanaRemoteDataSource::class,
        ImbalHasilRemoteDataSource::class,
        DanaKelolaanRemoteDataSource::class
    )
    single { ReksaDanaRepository(get()) }
    single { ImbalHasilRepository(get()) }
    single { DanaKelolaanRepository(get()) }
    viewModel { PerbandinganViewModel(get(), get()) }
    viewModel { (ids: List<String>, type: GrafikFragment.Type) -> GrafikViewModel(ids, get(), when(type){
        GrafikFragment.Type.ImbalHasil -> get<ImbalHasilRepository>()
        GrafikFragment.Type.DanaKelolaan -> get<DanaKelolaanRepository>()
    }) }
}