package com.bareksa.bareksatest.ui.grafik

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bareksa.bareksatest.R
import com.bareksa.bareksatest.databinding.FragmentGrafikBinding
import com.bareksa.bareksatest.viewmodel.grafik.GrafikViewModel
import com.google.android.material.tabs.TabLayout
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.*
import kotlin.collections.ArrayList


class GrafikFragment : Fragment(), TabLayout.OnTabSelectedListener {

    enum class Type {
        ImbalHasil,
        DanaKelolaan
    }

    private lateinit var binding: FragmentGrafikBinding
    private val type by lazy { arguments!!.getSerializable(ARG_TYPE) as Type }
    private val reksaDanaIds by lazy { arguments!!.getStringArrayList(ARG_REKSA_DANA_IDS)!! }
    private val viewModel: GrafikViewModel by viewModel { parametersOf(reksaDanaIds, type) }
    private val colorRes = listOf(
        R.color.green_dark,
        R.color.violet_dark,
        R.color.navy_dark
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentGrafikBinding.inflate(layoutInflater, container, false).run {
        binding = this;root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lineChart.setLegend(legend)
            listOf("1W", "1M", "1Y", "3Y", "5Y", "10Y", "All").forEach {
                durationTabLayout.addTab(durationTabLayout.newTab().setText(it))
                durationTabLayout.addOnTabSelectedListener(this@GrafikFragment)
                durationTabLayout.setTabTextColors(
                    ContextCompat.getColor(requireContext(), R.color.disabled),
                    ContextCompat.getColor(requireContext(), R.color.primary_800),
                )
            }
            viewModel.state.observe(viewLifecycleOwner) {
                if (it.loading) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.lineChart.visibility = View.GONE
                    binding.legend.visibility = View.GONE
                    binding.durationTabLayout.visibility = View.GONE
                } else {
                    binding.progressBar.visibility = View.GONE
                    binding.lineChart.visibility = View.VISIBLE
                    binding.legend.visibility = View.VISIBLE
                    binding.durationTabLayout.visibility = View.VISIBLE
                }
                it.data?.let { grafikEntries ->
                    lineChart.setData(grafikEntries.mapIndexed { i, data ->
                        GrafikData(data.first, colorRes[i % colorRes.size], data.second)
                    })
                }
                it.error?.let { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
            if (savedInstanceState == null)
                viewModel.load()
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab) {
        Toast.makeText(requireContext(), "Tab ${tab.text} selected", Toast.LENGTH_SHORT).show()
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}
    override fun onTabReselected(tab: TabLayout.Tab?) {}


    companion object {
        private const val ARG_TYPE = "TYPE"
        private const val ARG_REKSA_DANA_IDS = "REKSA_DANA_IDS"

        @JvmStatic
        fun newInstance(type: Type, data: List<String>) =
            GrafikFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_REKSA_DANA_IDS, ArrayList(data))
                    putSerializable(ARG_TYPE, type)
                }
            }
    }

    data class GrafikEntry(val date: Date, val value: Float)
    data class GrafikData(
        val name: String,
        @ColorRes val color: Int,
        val entries: List<GrafikEntry>
    )
}