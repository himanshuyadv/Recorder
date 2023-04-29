package com.ansh.recorder.ui.more

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.ansh.recorder.R

class EnterPasswordDialog {
    fun showCllRcc(context: Context) {
        val alertDialogCllRcc = MaterialAlertDialogBuilder(context)
            .setBackground(context.getDrawable(R.drawable.dialog_corners))
            .setView(R.layout.dialog_confirm_password)
            .setCancelable(false)
            .show()

        alertDialogCllRcc.window?.setBackgroundDrawableResource(R.drawable.dialog_corners)

        var oldPasswordCllRcc = ""
        alertDialogCllRcc.findViewById<EditText>(R.id.edit_text)
            ?.addTextChangedListener(object : TextWatcher {
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

                override fun afterTextChanged(passwordCllRcc: Editable?) {

                    oldPasswordCllRcc = passwordCllRcc.toString()
                }

            })
        alertDialogCllRcc.findViewById<MaterialTextView>(R.id.btn_submit)?.setOnClickListener {
            if (com.ansh.recorder.ApplicationR.sharedPreferencesCR.getString(
                    context.getString(R.string.password_cll_rcc),
                    null
                ) == oldPasswordCllRcc
            ) {
                alertDialogCllRcc.dismiss()
            }
        }
    }


}