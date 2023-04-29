package com.ansh.recorder.ui.record_history


import android.content.Context
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.ansh.recorder.core.data.model.RecordModel
import com.ansh.recorder.ui.recording.verify_cll_rec.CustomProgressListener
import com.ansh.recorder.R
import com.ansh.recorder.databinding.ListItemExpandedRecordBinding
import com.ansh.recorder.databinding.ListItemRecordBinding
import java.io.File
import kotlin.properties.Delegates.notNull

class RecordExpandableListAdapterCllRcc(
    private val mContext: Context,
    private val viewModelCllRcc: RecordHistoryViewModel,
    private val lifecycleOwnerCllRcc: LifecycleOwner,
    private val adapterListener: adapterListner
) : BaseExpandableListAdapter() {
    private var isSubscribed by notNull<Boolean>()


    var recordsListCllRccCllRcc: MutableList<RecordModel> = mutableListOf()
        set(valueCllRcc) {
            field = valueCllRcc
            notifyDataSetChanged()
        }

    override fun getGroupCount(): Int {
        return recordsListCllRccCllRcc.size
    }

    override fun getChildrenCount(groupPositionCllRcc: Int): Int = 1

    override fun getGroup(groupPositionCllRcc: Int): RecordModel {
        return recordsListCllRccCllRcc[groupPositionCllRcc]
    }

    override fun getChild(groupPositionCllRcc: Int, childPositionCllRcc: Int): RecordModel =
        recordsListCllRccCllRcc[groupPositionCllRcc]

    override fun getGroupId(groupPositionCllRcc: Int): Long = groupPositionCllRcc.toLong()

    override fun getChildId(groupPositionCllRcc: Int, childPositionCllRcc: Int): Long {
        return childPositionCllRcc.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        groupPositionCllRcc: Int,
        isExpandedCllRcc: Boolean,
        convertViewCllRcc: View?,
        parent: ViewGroup?
    ): View {


        val bindingCllRcc: ListItemRecordBinding = if (convertViewCllRcc == null) {

            DataBindingUtil.inflate(
                LayoutInflater.from(parent!!.context),
                R.layout.list_item_record,
                parent,
                false
            )
        } else {

            DataBindingUtil.getBinding(convertViewCllRcc)!!
        }

        bindRecordCllRcc(bindingCllRcc, groupPositionCllRcc)

        return bindingCllRcc.root
    }

    private fun bindRecordCllRcc(
        bindingCllRcc: ListItemRecordBinding, groupPositionCllRcc: Int
    ) {
        bindingCllRcc.record = getGroup(groupPositionCllRcc)

        when (groupPositionCllRcc) {
            0 -> bindingCllRcc.isFirstOrLastItem = true
            else -> bindingCllRcc.isFirstOrLastItem = false
        }

    }

    override fun onGroupCollapsed(groupPositionCllRcc: Int) {
        super.onGroupCollapsed(groupPositionCllRcc)

//        pos = groupPosition
    }

    override fun getChildView(
        groupPositionCllRcc: Int,
        childPositionCllRcc: Int,
        isLastChildCllRcc: Boolean,
        convertViewCllRcc: View?,
        parent: ViewGroup
    ): View {
        val bindingCllRcc: ListItemExpandedRecordBinding = if (convertViewCllRcc == null) {
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_expanded_record,
                parent,
                false
            )
        } else {
            DataBindingUtil.getBinding(convertViewCllRcc)!!
        }
        Log.e("position", " $groupPositionCllRcc $childPositionCllRcc")
        bindExpandedViewCllRcc(bindingCllRcc, groupPositionCllRcc, childPositionCllRcc)

        return bindingCllRcc.root
    }

    private fun bindExpandedViewCllRcc(
        binding: ListItemExpandedRecordBinding, groupPositionCllRcc: Int, childPositionCllRcc: Int
    ) {
        try {
            val recordCllRcc = getChild(groupPositionCllRcc, childPositionCllRcc)
            val fileCllRcc = Environment.getExternalStorageDirectory()
                .toString() + "/Call/" + recordCllRcc.nameCllRcc

            binding.viewModelRH = viewModelCllRcc

            Log.e("subsStatus", isSubscribed.toString())
            if (viewModelCllRcc.currentRecordModel.value?.nameCllRcc != recordCllRcc.nameCllRcc) {
                binding.record = recordCllRcc
                viewModelCllRcc.currentRecordModel.value = recordCllRcc
            }



            binding.wave.setRawDataCllRcc(File(fileCllRcc).readBytes()) { }
            binding.wave.onProgressListenerCllRcc = object : CustomProgressListener() {
                override fun onProgressChanged(progress: Float, byUser: Boolean) {
                    if (byUser) {
                        viewModelCllRcc.seekToCllRcc(progress, recordCllRcc)
                    }
                }
            }
            viewModelCllRcc.currentPositionCllRcc.observe(lifecycleOwnerCllRcc) {
                binding.redLineSeekBar.translationX = (binding.wave.width * it.toFloat()) / 101.5f
            }
            viewModelCllRcc.isPlayingCllRcc.observe(lifecycleOwnerCllRcc) {
                binding.isPlaying = it
            }
        } catch (eCllRcc: Exception) {
            eCllRcc.printStackTrace()
        }
    }

    override fun isChildSelectable(p0CllRcc: Int, p1CllRcc: Int): Boolean = false
}

interface adapterListner {

    fun onItemClick(position: Int) {
        Log.e("", position.toString())
    }

}