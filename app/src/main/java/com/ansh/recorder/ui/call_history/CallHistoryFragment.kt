package com.ansh.recorder.ui.call_history

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.ansh.recorder.core.checkPermissionsCllRcc
import com.ansh.recorder.core.models.Call
import com.ansh.recorder.core.splitContactsListToCategoriesCllRcc
import com.ansh.recorder.ui.MainActivity
import com.ansh.recorder.ui.RecorderViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.ansh.recorder.R
import com.ansh.recorder.databinding.FragmentCallHistoryBinding


class CallHistoryFragment : Fragment() {

    private lateinit var bindingCllRcc: FragmentCallHistoryBinding
    private val viewModelCllRcc: RecorderViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingCllRcc = FragmentCallHistoryBinding.inflate(inflater)

        return bindingCllRcc.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        requireContext().checkPermissionsCllRcc(android.Manifest.permission.READ_CALL_LOG) { grantedCllRcc ->
            if (grantedCllRcc) {
                //todo
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModelCllRcc.parseCallsCllRcc(requireActivity())
                }
            } else {

            }
        }


        var listCllRcc = mutableListOf<Call>()

        viewModelCllRcc.getAllCallsCllRcc().observe(viewLifecycleOwner) {
//            adapter.submitList(it)
            listCllRcc = it as MutableList<Call>
        }



        bindingCllRcc.contactsSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                p0CllRcc: CharSequence?,
                p1CllRcc: Int,
                p2CllRcc: Int,
                p3CllRcc: Int
            ) {

            }

            override fun onTextChanged(
                p0CllRcc: CharSequence?,
                p1CllRcc: Int,
                p2CllRcc: Int,
                p3CllRcc: Int
            ) {

            }

            override fun afterTextChanged(p0CllRcc: Editable?) {
                if (!listCllRcc.isNullOrEmpty()) {
                    if (!p0CllRcc.isNullOrEmpty()) {
                        val filteredListCllRcc = mutableListOf<Call>()
                        listCllRcc.forEach {
                            if ((it).contactNameCllRcc.startsWith(
                                    p0CllRcc.toString(),
                                    true
                                )
                            ) {
                                filteredListCllRcc.add(it)
                            }
                        }
                        splitContactsListToCategoriesCllRcc(filteredListCllRcc)
                        viewModelCllRcc.callLiveDataChangedCllRcc.postValue(filteredListCllRcc)
                    } else {
                        viewModelCllRcc.callLiveDataChangedCllRcc.postValue(listCllRcc)
                    }
                }
            }
        })
    }

    private fun setupViews() {
        bindingCllRcc.callsPager.adapter = CallsPagerAdapter(
            childFragmentManager,
            lifecycle
        )
        TabLayoutMediator(
            bindingCllRcc.callsTablayout,
            bindingCllRcc.callsPager
        ) { tabCllRcc, position ->
            when (position) {
                0 -> {
                    tabCllRcc.text = getString(R.string.title_all)
                }
                1 -> {
                    tabCllRcc.text = getString(R.string.title_incoming)
                }
                2 -> {
                    tabCllRcc.text = getString(R.string.title_outgoing)
                }
                else -> {
                    tabCllRcc.text = getString(R.string.miss)
                }
            }
        }.attach()
    }



    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).showSearchIconCllRcc {
            bindingCllRcc.cardViewEt.visibility = View.VISIBLE
            bindingCllRcc.callsTablayout.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).hideSearchIconCllRcc()

    }
}

