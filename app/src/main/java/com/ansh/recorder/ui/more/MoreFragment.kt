package com.ansh.recorder.ui.more


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ansh.recorder.R
import com.ansh.recorder.databinding.FragmentMenuBinding
import com.ansh.recorder.ui.MainActivity


class MoreFragment : Fragment() {
    private lateinit var bindingCllRcc: FragmentMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        bindingCllRcc =
            DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)

        return bindingCllRcc.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(bindingCllRcc) {
            llPassword.setOnClickListener {
                findNavController().navigate(MoreFragmentDirections.actionMoreFragmentToPasswordFragment())
            }

            about.setOnClickListener {
                val uriCllRcc = Uri.parse(getString(R.string.about_link))

                val intentCllRcc = Intent(Intent.ACTION_VIEW, uriCllRcc)
                startActivity(intentCllRcc)
            }


            privacyPolicyFmc.setOnClickListener {
                val uriCllRcc = Uri.parse(getString(R.string.privicy_pol))
                val intentCllRcc = Intent(Intent.ACTION_VIEW, uriCllRcc)
                startActivity(intentCllRcc)
            }
        }
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