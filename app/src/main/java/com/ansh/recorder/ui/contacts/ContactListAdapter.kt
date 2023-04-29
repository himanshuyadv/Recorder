package com.ansh.recorder.ui.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.ansh.recorder.core.models.ContactData
import com.ansh.recorder.core.models.ContactHeader
import com.ansh.recorder.ui.RecorderViewModel
import com.ansh.recorder.R
import com.ansh.recorder.databinding.ListItemAlphabeticCategoryBinding
import com.ansh.recorder.databinding.ListItemContactBinding

class ContactListAdapterCllRcc(
    private val context: Context,
    private val viewModelCllRcc: RecorderViewModel
) : ListAdapter<Any, RecyclerView.ViewHolder>(ContactCallbackCllRcc()) {

    private val HEADERCllRcc = 0
    private val CONTACTCllRcc = 1


    inner class ContactHolderCllRcc(private val bindingCllRcc: ListItemContactBinding) :
        RecyclerView.ViewHolder(bindingCllRcc.root) {
        init {
            bindingCllRcc.viewModelRVM = viewModelCllRcc
        }

        fun bindContact(contactData: ContactData) {

            bindingCllRcc.contact = contactData
        }

    }

    inner class HeaderHolderCllRcc(private val bindingCllRcc: ListItemAlphabeticCategoryBinding) :
        RecyclerView.ViewHolder(bindingCllRcc.root) {
        fun bindHeader(contactHeader: ContactHeader) {
            bindingCllRcc.header = contactHeader
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
    }

    override fun getItemViewType(position: Int): Int {

        return when (currentList[position]) {
            is ContactData -> {
                CONTACTCllRcc
            }
            is ContactHeader -> {
                HEADERCllRcc
            }
            else -> {
                123
            }
        }
//        return super.getItemViewType(position)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            CONTACTCllRcc -> {
                val bindingCllRcc: ListItemContactBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_contact,
                    parent,
                    false
                )
                return ContactHolderCllRcc(bindingCllRcc)
            }
            HEADERCllRcc -> {
                val bindingCllRcc: ListItemAlphabeticCategoryBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.context),
                    R.layout.list_item_alphabetic_category,
                    parent,
                    false
                )
                return HeaderHolderCllRcc(bindingCllRcc)
            }
            else -> {
                return null!!
            }
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            CONTACTCllRcc -> {
                val holderCllRcc = holder as ContactHolderCllRcc
                holderCllRcc.bindContact(currentList[position] as ContactData)
            }
            HEADERCllRcc -> {
                val holderCllRcc = holder as HeaderHolderCllRcc
                holderCllRcc.bindHeader(currentList[position] as ContactHeader)
            }
        }
    }
}

class ContactCallbackCllRcc : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItemCllRcc: Any, newItemCllRcc: Any): Boolean {
        if (oldItemCllRcc is ContactData && newItemCllRcc is ContactData) {

            return oldItemCllRcc.contactIdCllRcc == newItemCllRcc.contactIdCllRcc
        }
        return true
    }

    override fun areContentsTheSame(oldItemCllRcc: Any, newItemCllRcc: Any): Boolean {
        if (oldItemCllRcc is ContactData && newItemCllRcc is ContactData) {

            return oldItemCllRcc == newItemCllRcc
        }
        return true
    }

}

