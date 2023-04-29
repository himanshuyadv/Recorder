package com.ansh.recorder.ui.utils

import android.content.Context
import android.os.Build
import android.view.accessibility.AccessibilityNodeInfo
import com.ansh.recorder.ui.utils.ButtonCondition.ClickedCllRcc.RECORD_BUTTONCllRcc

class ButtonCondition(
    private val context: Context,
    private val nodeCllRcc: AccessibilityNodeInfo
) : Condition<Int> {

    private val languageCllRcc: String
        get() = context.localeCllRcc.language

    override fun invoke(IDCllRcc: Int): Boolean {
        val textCllRcc = nodeCllRcc.text?.toString()?.trim() ?: return false
        // if(node.viewIdResourceName == "com.android.settings:id/button3"){
//            Log.e("node",  nodeCllRcc.text.toString() + " - " + nodeCllRcc.toString())
        // }


        return when {
            IDCllRcc == RECORD_BUTTONCllRcc && recordButtonCllRcc.checkNode(textCllRcc) -> true
            else -> false
        }
    }

    object ClickedCllRcc {
        const val RECORD_BUTTONCllRcc = 37
    }

    private val recordButtonCllRcc: Array<String>
        get() = when (languageCllRcc) {
            "ru" -> arrayOf("Записать")
            "en" -> arrayOf("Record")
            "uk" -> arrayOf()
            else -> arrayOf()
        }

    private fun Array<String>.checkNode(nodeTextCllRcc: String) = any {

        nodeTextCllRcc.equals(it, true)
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

    private val Context.localeCllRcc
        get() = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N)
            resources.configuration.locales.get(0)
        else
            resources.configuration.locale
}