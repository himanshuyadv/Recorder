package com.ansh.recorder.ui.call_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.ansh.recorder.core.models.CallTypes

import com.ansh.recorder.ui.RecorderViewModel
import com.ansh.recorder.databinding.FragmentAllCallsBinding


class OutgoingCallsFragment : Fragment() {

    private val adapterCllRcc: CallListAdapterCllRcc by lazy { CallListAdapterCllRcc(requireContext()) }
    private lateinit var bindingCllRcc: FragmentAllCallsBinding
    private val viewModelCllRcc: RecorderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingCllRcc = FragmentAllCallsBinding.inflate(inflater)

        return bindingCllRcc.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingCllRcc.callList.adapter = adapterCllRcc
        viewModelCllRcc.getAllCallsCllRcc().observe(viewLifecycleOwner) {
            adapterCllRcc.submitList(it.filter { callCllRcc -> callCllRcc.typeCllRcc == CallTypes.OUTGOINGCllRcc })
        }


        viewModelCllRcc.callLiveDataChangedCllRcc.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty())
                adapterCllRcc.submitList(it.filter { callCllRcc -> callCllRcc.typeCllRcc == CallTypes.OUTGOINGCllRcc })
        }
    }
}