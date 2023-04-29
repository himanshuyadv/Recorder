package com.ansh.recorder.ui.record_history

import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView.OnGroupExpandListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import com.ansh.recorder.core.data.database.repositories_cll_rec.RecordsRepository
import com.ansh.recorder.core.data.model.RecordModel
import com.ansh.recorder.core.deleteFileCllRcc
import com.ansh.recorder.core.shareCllRcc
import com.ansh.recorder.core.utils.toUriCllRcc

import com.ansh.recorder.ui.MainActivity
import com.ansh.recorder.ui.RecorderViewModel
import com.ansh.recorder.ui.more.EnterPasswordDialog
import com.ansh.recorder.R
import com.ansh.recorder.databinding.FragmentRecordHistoryBinding
import java.io.File


class RecordHistoryFragment : Fragment(), adapterListner {

    //    private val adapter: ExpendableAdapter by lazy { ExpendableAdapter(requireActivity()) }
    private val adapterCllRcc: RecordExpandableListAdapterCllRcc by lazy {
        RecordExpandableListAdapterCllRcc(
            requireContext(),
            historyViewModelCllRcc,
            viewLifecycleOwner, this
        )
    }
    private lateinit var bindingCllRcc: FragmentRecordHistoryBinding

    private val viewModelCllRcc: RecorderViewModel by viewModels()
    private val historyViewModelCllRcc: RecordHistoryViewModel by viewModels()

    private var listCllRcc: MutableList<RecordModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingCllRcc =
            FragmentRecordHistoryBinding.inflate(inflater)
        return bindingCllRcc.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        binding.recyclerPlayList.adapter = adapter

        bindingCllRcc.recordsListView.setAdapter(adapterCllRcc)
        bindingCllRcc.recordsListView.setOnGroupExpandListener(object : OnGroupExpandListener {
            var previousGroupCllRcc = -1
            override fun onGroupExpand(groupPositionCllRcc: Int) {
                if (groupPositionCllRcc != previousGroupCllRcc) {
                    bindingCllRcc.recordsListView.collapseGroup(
                        previousGroupCllRcc
                    )
                    historyViewModelCllRcc.releasePlayerCllRcc()
                }
                previousGroupCllRcc = groupPositionCllRcc

            }
        })

        if (com.ansh.recorder.ApplicationR.sharedPreferencesCR.getString(
                requireActivity().getString(R.string.password_cll_rcc),
                null
            ) != null
        )
            EnterPasswordDialog().showCllRcc(requireContext())

        RecordsRepository.getRecordsCllRcc().observe(viewLifecycleOwner) {
//            Log.d("submitList1", it.toString())
            listCllRcc = mutableListOf()
            it.forEach { recCllRcc ->
                if (File(
                        Environment.getExternalStorageDirectory()
                            .toString() + requireActivity().getString(R.string.call_two_slash) + recCllRcc.nameCllRcc
                    ).exists()
                ) {
                    if (listCllRcc.find { recordCllRcc ->
                            recordCllRcc.nameCllRcc == recCllRcc.nameCllRcc
                        } == null) {
                        listCllRcc.add(recCllRcc)
                    }
                }
            }
//            list = it.toMutableList()
//            list.forEach {  }

            adapterCllRcc.recordsListCllRccCllRcc = listCllRcc
            collapseGroupsCllRcc()
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
                        val filteredListCllRcc = mutableListOf<RecordModel>()
                        listCllRcc.forEach {
                            if ((it).nameCllRcc.startsWith(
                                    p0CllRcc.toString(),
                                    true
                                )
                            ) {
                                filteredListCllRcc.add(it)
                            }
                        }
                        //splitContactsListToCategories(filteredList)
                        viewModelCllRcc.recordLiveDataChangedCllRcc.postValue(filteredListCllRcc)
                        adapterCllRcc.recordsListCllRccCllRcc = filteredListCllRcc
                    } else {
                        viewModelCllRcc.recordLiveDataChangedCllRcc.postValue(listCllRcc)
                        adapterCllRcc.recordsListCllRccCllRcc = listCllRcc
                    }
                }
            }
        })
        historyViewModelCllRcc.deleteRecordCllRccModel.observe(viewLifecycleOwner) {
            kotlin.runCatching {
                requireActivity().deleteFileCllRcc(it)
                lifecycleScope.launch(Dispatchers.IO) {
                    RecordsRepository.deleteRecordCllRcc(requireActivity(), it)
                }
                Toast.makeText(requireContext(), getString(R.string.deleted), Toast.LENGTH_SHORT)
                    .show()
            }.exceptionOrNull()?.printStackTrace()
        }

        historyViewModelCllRcc.shareRecordModel.observe(viewLifecycleOwner) {
            kotlin.runCatching {
                val pathCllRcc = Environment.getExternalStorageDirectory()
                    .toString() + getString(R.string.call_two_slash) + it.nameCllRcc
                requireActivity().shareCllRcc(File(pathCllRcc).toUriCllRcc(requireActivity()))
                Toast.makeText(
                    requireContext(),
                    getString(R.string.share_cll_rcc),
                    Toast.LENGTH_SHORT
                ).show()
            }.exceptionOrNull()?.printStackTrace()
        }
        historyViewModelCllRcc.saveRecordModel.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), getString(R.string.saved_cll_rcc), Toast.LENGTH_SHORT)
                .show()
        }
        historyViewModelCllRcc.currentRecordModel.observe(viewLifecycleOwner) {
            historyViewModelCllRcc.setMediaPlayerCllRcc()
        }
    }

    private fun collapseGroupsCllRcc() {
        for (iCllRcc in 0 until adapterCllRcc.groupCount) {
            bindingCllRcc.recordsListView.collapseGroup(iCllRcc)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).showSearchIconCllRcc {
            bindingCllRcc.cardViewEt.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).hideSearchIconCllRcc()
        historyViewModelCllRcc.releasePlayerCllRcc()
    }



    override fun onItemClick(position: Int) {
        if (bindingCllRcc.recordsListView.isGroupExpanded(position)) {
            bindingCllRcc.recordsListView.collapseGroup(position)
        } else {
            bindingCllRcc.recordsListView.expandGroup(position)
        }

        super.onItemClick(position)
    }

}