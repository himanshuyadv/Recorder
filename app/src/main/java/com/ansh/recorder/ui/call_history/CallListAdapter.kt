package com.ansh.recorder.ui.call_history

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ansh.recorder.core.checkPermissionsCllRcc
import com.ansh.recorder.core.checkSimCllRcc
import com.ansh.recorder.core.models.Call

import com.ansh.recorder.R
import com.ansh.recorder.databinding.ListItemCallBinding

class CallListAdapterCllRcc(val context: Context) :
    ListAdapter<Call, CallListAdapterCllRcc.CallHolder>(CallCallbackCllRcc()) {
    inner class CallHolder(val bindingCllRcc: ListItemCallBinding) :
        RecyclerView.ViewHolder(bindingCllRcc.root) {
        fun bindCall(call: Call) {
            bindingCllRcc.call = call
            bindingCllRcc.callButton.setOnClickListener {
                context.checkPermissionsCllRcc(Manifest.permission.CALL_PHONE) {
                    if (context.checkSimCllRcc()) {
                        val uriCllRcc =
                            Uri.parse(context.getString(R.string.tel_cll_rcc) + call.phoneNumberCllRcc.trim())
                        context.startActivity(Intent(Intent.ACTION_CALL, uriCllRcc))
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallHolder {
        val bindingCllRcc: ListItemCallBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.list_item_call,
            parent, false
        )


        return CallHolder(bindingCllRcc)
    }

    override fun onBindViewHolder(holder: CallHolder, position: Int) {

        holder.bindCall(currentList[position])
    }
}

class CallCallbackCllRcc : DiffUtil.ItemCallback<Call>() {
    override fun areItemsTheSame(oldItemCllRcc: Call, newItemCllRcc: Call): Boolean {

        return oldItemCllRcc.id == newItemCllRcc.id
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

    override fun areContentsTheSame(oldItemCllRcc: Call, newItemCllRcc: Call): Boolean {

        return oldItemCllRcc == newItemCllRcc
    }

}