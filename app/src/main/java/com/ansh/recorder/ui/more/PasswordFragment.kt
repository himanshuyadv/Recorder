package com.ansh.recorder.ui.more

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ybs.passwordstrengthmeter.PasswordStrength

import com.ansh.recorder.core.utils.showToastCllRcc

import com.ansh.recorder.ui.MainActivity
import com.ansh.recorder.R
import com.ansh.recorder.databinding.FramentSetPasswordBinding

class PasswordFragment : Fragment(), TextWatcher {
    private lateinit var bindingCllRcc: FramentSetPasswordBinding
    private var mOldPasswordCllRcc = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingCllRcc =
            DataBindingUtil.inflate(inflater, R.layout.frament_set_password, container, false)

        return bindingCllRcc.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingCllRcc.editText.addTextChangedListener(this)
        val oldPasswordCllRcc = com.ansh.recorder.ApplicationR.sharedPreferencesCR.getString(
            requireActivity().getString(R.string.password_cll_rcc),
            null
        )
//        Log.d("oldPassword", oldPassword.toString())
        bindingCllRcc.btnSubmit.setOnClickListener {
            if (oldPasswordCllRcc == null) {
                if (bindingCllRcc.editText.text.toString() != "") {
                    com.ansh.recorder.ApplicationR.sharedPreferencesCR.edit()
                        .putString(
                            requireActivity().getString(R.string.password_cll_rcc),
                            bindingCllRcc.editText.text.toString()
                        )
                        .apply()
                    showToastCllRcc(getString(R.string.saved_cll_rcc))
                    findNavController().navigateUp()
                }
            } else {
//                Log.d("oldPassword1", oldPassword.toString() + mOldPassword)
                if (mOldPasswordCllRcc == oldPasswordCllRcc) {
                    com.ansh.recorder.ApplicationR.sharedPreferencesCR.edit().clear().apply()
                    com.ansh.recorder.ApplicationR.sharedPreferencesCR.edit()
                        .putString(
                            requireActivity().getString(R.string.password_cll_rcc),
                            bindingCllRcc.editText.text.toString()
                        )
                        .apply()
                    showToastCllRcc(requireActivity().getString(R.string.saved_cll_rcc))
                    findNavController().navigateUp()
                }
            }
        }

        if (oldPasswordCllRcc != null) {
            bindingCllRcc.currentPassword.visibility = View.VISIBLE
            bindingCllRcc.textInputOld.visibility = View.VISIBLE
            bindingCllRcc.outlineEditTextOld.visibility = View.VISIBLE

            bindingCllRcc.editTextOld.addTextChangedListener(object : TextWatcher {
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

                override fun afterTextChanged(enterOldPasswordCllRcc: Editable?) {
                    mOldPasswordCllRcc = enterOldPasswordCllRcc.toString()
                }

            })
        } else {
            bindingCllRcc.currentPassword.visibility = View.GONE
            bindingCllRcc.textInputOld.visibility = View.GONE
            bindingCllRcc.outlineEditTextOld.visibility = View.GONE
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun updatePasswordCllRcc(editTextCllRcc: String) {
        val passwordCllRcc: PasswordStrength = PasswordStrength.calculateStrength(editTextCllRcc)
//        Log.d("password1", password.getText(requireContext()).toString())
        bindingCllRcc.llPasswordSafe[0].background =
            resources.getDrawable(R.drawable.item_progress, null)
        bindingCllRcc.llPasswordSafe[1].background =
            resources.getDrawable(R.drawable.item_progress, null)
        bindingCllRcc.llPasswordSafe[2].background =
            resources.getDrawable(R.drawable.item_progress, null)
        bindingCllRcc.llPasswordSafe[3].background =
            resources.getDrawable(R.drawable.item_progress, null)
        when (passwordCllRcc.getText(requireContext())) {
            getString(R.string.week_cll_rcc) -> {
                bindingCllRcc.llPasswordSafe[0].background =
                    resources.getDrawable(R.drawable.item_progress_week, null)
                bindingCllRcc.weekPassword.text = getString(R.string.week_cll_rcc)
                bindingCllRcc.iconInfo.visibility = View.VISIBLE
                bindingCllRcc.pwdErrorMessage.visibility = View.GONE
                bindingCllRcc.btnSubmit.background =
                    resources.getDrawable(R.drawable.btn_submit_on, null)
            }
            getString(R.string.medium) -> {
                bindingCllRcc.llPasswordSafe[0].background =
                    resources.getDrawable(R.drawable.item_progress_soso, null)
                bindingCllRcc.llPasswordSafe[1].background =
                    resources.getDrawable(R.drawable.item_progress_soso, null)
                bindingCllRcc.weekPassword.text = getString(R.string.so_so)
                bindingCllRcc.iconInfo.visibility = View.VISIBLE
                bindingCllRcc.pwdErrorMessage.visibility = View.GONE
                bindingCllRcc.btnSubmit.background =
                    resources.getDrawable(R.drawable.btn_submit_on, null)
            }
            getString(R.string.strong) -> {
                bindingCllRcc.llPasswordSafe[0].background =

                    resources.getDrawable(R.drawable.item_progress_good, null)
                bindingCllRcc.llPasswordSafe[1].background =
                    resources.getDrawable(R.drawable.item_progress_good, null)
                bindingCllRcc.llPasswordSafe[2].background =
                    resources.getDrawable(R.drawable.item_progress_good, null)
                bindingCllRcc.weekPassword.text = getString(R.string.good)
                bindingCllRcc.iconInfo.visibility = View.VISIBLE
                bindingCllRcc.pwdErrorMessage.visibility = View.GONE
                bindingCllRcc.btnSubmit.background =
                    resources.getDrawable(R.drawable.btn_submit_on, null)
            }
            getString(R.string.very_strong) -> {
                bindingCllRcc.llPasswordSafe[0].background =
                    resources.getDrawable(R.drawable.item_progress_great, null)
                bindingCllRcc.llPasswordSafe[1].background =
                    resources.getDrawable(R.drawable.item_progress_great, null)
                bindingCllRcc.llPasswordSafe[2].background =
                    resources.getDrawable(R.drawable.item_progress_great, null)
                bindingCllRcc.llPasswordSafe[3].background =
                    resources.getDrawable(R.drawable.item_progress_great, null)
                bindingCllRcc.weekPassword.text = getString(R.string.great)
                bindingCllRcc.iconInfo.visibility = View.GONE
                bindingCllRcc.pwdErrorMessage.visibility = View.GONE
                bindingCllRcc.btnSubmit.background =
                    resources.getDrawable(R.drawable.btn_submit_on, null)
            }
            else -> {
                bindingCllRcc.llPasswordSafe[0].background =
                    resources.getDrawable(R.drawable.item_progress, null)
                bindingCllRcc.llPasswordSafe[1].background =
                    resources.getDrawable(R.drawable.item_progress, null)
                bindingCllRcc.llPasswordSafe[2].background =
                    resources.getDrawable(R.drawable.item_progress, null)
                bindingCllRcc.llPasswordSafe[3].background =
                    resources.getDrawable(R.drawable.item_progress, null)
                bindingCllRcc.weekPassword.text = getString(R.string.very_week)
                bindingCllRcc.pwdErrorMessage.visibility = View.VISIBLE
                bindingCllRcc.iconInfo.visibility = View.VISIBLE
                bindingCllRcc.btnSubmit.background =
                    resources.getDrawable(R.drawable.btn_submit_off, null)
            }
        }
    }

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
//        newPassword = password.toString()
        updatePasswordCllRcc(passwordCllRcc.toString())
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).hideOptionsCllRcc()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as MainActivity).showOptionsCllRcc()

    }
}