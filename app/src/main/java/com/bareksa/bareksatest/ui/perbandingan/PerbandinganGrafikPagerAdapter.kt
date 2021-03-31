package com.bareksa.bareksatest.ui.perbandingan

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.bareksa.bareksatest.model.ReksaDana
import com.bareksa.bareksatest.ui.grafik.GrafikFragment

class PerbandinganGrafikPagerAdapter(fm: FragmentManager) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var data : List<ReksaDana>? = null

    override fun getCount() = if(data == null) 0 else 2

    override fun getItem(position: Int): Fragment = generateGrafikFragment(position)

    override fun getPageTitle(position: Int) = when (position) {
        0 -> "Imbal Hasil"
        else -> "Dana Kelolaan"
    }

    private fun generateGrafikFragment(position: Int) = when(position) {
        0 -> GrafikFragment.newInstance(GrafikFragment.Type.ImbalHasil,  data!!.map { it.id })
        else -> GrafikFragment.newInstance(GrafikFragment.Type.DanaKelolaan,  data!!.map { it.id })
    }

    fun update(data: List<ReksaDana>) {
        this.data = data
        notifyDataSetChanged()
    }
}