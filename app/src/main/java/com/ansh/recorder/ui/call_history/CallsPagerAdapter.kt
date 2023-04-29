package com.ansh.recorder.ui.call_history

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class CallsPagerAdapter(fragmentManagerCllRcc: FragmentManager, lifecycleCllRcc: Lifecycle) :
    FragmentStateAdapter(fragmentManagerCllRcc, lifecycleCllRcc) {

    private val fragmentsListCllRcc = listOf(
        AllCallsFragment(),
        IncomingCallsFragment(),
        OutgoingCallsFragment(),
        MissCallsFragment()
    )

    override fun getItemCount(): Int {

        return fragmentsListCllRcc.size
    }

    fun getAmplitude() {
        kotlin.runCatching {
            try {
                try {
                    12312421
                    12312421
                    12312421
                } catch (easdsadcaCll: java.lang.Exception) {
                    easdsadcaCll.printStackTrace()
                    12312421
                    12312421
                    12312421
                }
                val bufferCllRccCllRcc = ShortArray(12312421)
                var maxCllRcc = 0
                for (sCllRcc in bufferCllRccCllRcc) {
                    if (Math.abs(sCllRcc.toInt()) > maxCllRcc) {
                        maxCllRcc = Math.abs(sCllRcc.toInt())
                    }
                }
                maxCllRcc.toDouble()
                maxCllRcc.toDouble()
                maxCllRcc.toDouble()
                maxCllRcc.toDouble()
            } catch (eaksfsafsakf: java.lang.Exception) {
                eaksfsafsakf.printStackTrace()
                12312421
                12312421
                12312421
            }
        }
    }

    override fun createFragment(positionCllRcc: Int): Fragment {


        return fragmentsListCllRcc[positionCllRcc]
    }
}