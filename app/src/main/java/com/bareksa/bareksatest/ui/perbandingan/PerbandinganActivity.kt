package com.bareksa.bareksatest.ui.perbandingan

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bareksa.bareksatest.R
import com.bareksa.bareksatest.databinding.ActivityPerbandinganBinding
import com.bareksa.bareksatest.datasource.remote.DummyRemoteDataSource.Companion.ASCEND_ID
import com.bareksa.bareksatest.datasource.remote.DummyRemoteDataSource.Companion.BNI_ID
import com.bareksa.bareksatest.datasource.remote.DummyRemoteDataSource.Companion.CIPTA_DANA_ID
import com.bareksa.bareksatest.model.ReksaDana
import com.bareksa.bareksatest.ui.view.DetailBeliButtonView
import com.bareksa.bareksatest.ui.view.InfoDetailRowItemView
import com.bareksa.bareksatest.ui.view.InfoDetailRowView
import com.bareksa.bareksatest.viewmodel.perbandingan.PerbandinganViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class PerbandinganActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityPerbandinganBinding.inflate(layoutInflater)
    }
    private val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))

    private val perbandinganGrafikPagerAdapter =
        PerbandinganGrafikPagerAdapter(supportFragmentManager)
    private val viewModel: PerbandinganViewModel by viewModel()
    private val colorRes = listOf(
        R.color.green_50,
        R.color.violet_50,
        R.color.navy_50
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Perbandingan"
        binding.viewPager.adapter = perbandinganGrafikPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        binding.tabLayout.setTabTextColors(
            ContextCompat.getColor(this, R.color.disabled),
            ContextCompat.getColor(this, R.color.primary_800),
        )
        if (savedInstanceState == null)
            viewModel.load(listOf(BNI_ID, CIPTA_DANA_ID, ASCEND_ID))
        viewModel.state.observe(this) {
            if (it.loading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.scrollView.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.scrollView.visibility = View.VISIBLE
            }
            it.data?.let { data ->
                perbandinganGrafikPagerAdapter.update(data)
                updateInfoDetail(data)
            }
            it.error?.let { error -> Toast.makeText(this, error, Toast.LENGTH_SHORT).show() }
        }
    }


    private fun updateInfoDetail(data: List<ReksaDana>) {
        binding.infoRoot.removeAllViews()
        addDetailInfo(null, data) { setImage(it.image);label = it.nama }
        addDetailInfo("Jenis reksa dana", data) { label = it.jenis.toString() }
        addDetailInfo("Imbal hasil", data) { label = it.imbalHasil }
        addDetailInfo("Dana Kelolaan", data) { label = it.danaKelolaan }
        addDetailInfo("Min. pembelian", data) { label = it.minPembelian }
        addDetailInfo("Jangka waktu", data) { label = "${it.jangkaWaktuTahun} Tahun" }
        addDetailInfo("Tingkat risiko", data) { label = it.tingkatResiko.toString() }
        addDetailInfo("Peluncuran", data) { label = dateFormatter.format(it.tanggalPeluncuran) }
        binding.infoRoot.addView(InfoDetailRowView(this).apply {
            title = null
            setItems(
                data.map { reksaDana ->
                    DetailBeliButtonView(context).apply {
                        setOnDetailClick {
                            Toast.makeText(
                                context, "Detail ${reksaDana.nama}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        setOnBeliClick {
                            Toast.makeText(
                                context,
                                "Beli ${reksaDana.nama}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            )
        })
    }


    private fun addDetailInfo(
        title: String?,
        data: List<ReksaDana>,
        designBlock: InfoDetailRowItemView.(data: ReksaDana) -> Unit
    ) {
        binding.infoRoot.addView(InfoDetailRowView(this).apply {
            this.title = title
            setItems(
                data.mapIndexed { index, reksaDana ->
                    InfoDetailRowItemView(context).apply {
                        designBlock(this, reksaDana)
                        setBackgroundTintColor(colorRes[index % colorRes.size])
                    }
                }
            )
        })
    }
}