package com.ansh.recorder.ui.contacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.ansh.recorder.core.*
import com.ansh.recorder.core.models.ContactData
import com.ansh.recorder.ui.MainActivity
import com.ansh.recorder.ui.RecorderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.ansh.recorder.R
import com.ansh.recorder.databinding.FragmentContactListBinding


class ContactListFragment : Fragment() {
    private val fakeChecker = FakeChecker.createDisabledCllRcc()
    private lateinit var bindingCllRcc: FragmentContactListBinding
    private val viewModelCllRcc: RecorderViewModel by activityViewModels()
    private val adapterCllRcc: ContactListAdapterCllRcc by lazy {
        ContactListAdapterCllRcc(
            requireContext(),
            viewModelCllRcc
        )
    }
    private var fullListCllRcc = mutableListOf<Any>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingCllRcc = FragmentContactListBinding.inflate(inflater)

        return bindingCllRcc.root
    }


    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindingCllRcc.contactsList.adapter = adapterCllRcc
        requireContext().checkPermissionsCllRcc(
            Manifest.permission.READ_CONTACTS
        ) { grantedCllRcc ->
            if (grantedCllRcc) {
                requireContext().checkPermissionsCllRcc(Manifest.permission.PROCESS_OUTGOING_CALLS) { callsCllRcc ->
                    if (callsCllRcc) {
                        requireContext().checkPermissionsCllRcc(Manifest.permission.RECORD_AUDIO) { recCllRcc ->
                            if (recCllRcc) {
                                requireContext().checkPermissionsCllRcc(Manifest.permission.READ_PHONE_STATE) { stateCllRcc ->

                                    val notificationListenerStringCllRcc: String =
                                        Settings.Secure.getString(
                                            requireActivity().contentResolver,
                                            "enabled_notification_listeners"
                                        )
//Check notifications access permission
//Check notifications access permission
                                    if (notificationListenerStringCllRcc == null || !notificationListenerStringCllRcc.contains(
                                            requireActivity().packageName
                                        )
                                    ) {
                                        val intentCllRcc =
                                            Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
                                        startActivity(intentCllRcc)
                                        //The notification access has not acquired yet!
                                    } else {
                                        //Your application has access to the notifications
                                    }

                                    requireActivity().checkPermissionsForMemoryCllRcc()
                                    requireActivity().checkAccessibilityCllRcc(fakeChecker) {}
                                    requireActivity().checkOverlayCllRcc(fakeChecker) {}

                                    lifecycleScope.launch(Dispatchers.IO) {
                                        val contactsListCllRcc =
                                            requireActivity().retrieveAllContactsCllRcc()
                                                .toMutableList()
                                        splitContactsListToCategoriesCllRcc(contactsListCllRcc)
                                        fullListCllRcc = contactsListCllRcc
                                        withContext(Dispatchers.Main) {
                                            adapterCllRcc.submitList(fullListCllRcc)
                                            bindingCllRcc.contactListProgressbar.visibility =
                                                View.GONE
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.perm_not_granted),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
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
                if (!fullListCllRcc.isNullOrEmpty()) {
                    if (!p0CllRcc.isNullOrEmpty()) {
                        val filteredListCllRcc = mutableListOf<Any>()
                        fullListCllRcc.forEach {
                            if ((it is ContactData) && ((it).nameCllRcc.startsWith(
                                    p0CllRcc.toString(),
                                    true
                                ) || (it).phoneNumberCllRcc.filter { contactCllRcc ->
                                    contactCllRcc.startsWith(
                                        p0CllRcc.toString(),
                                        true
                                    )
                                }.isNotEmpty())
                            ) {
                                filteredListCllRcc.add(it)
                            }
                        }
                        splitContactsListToCategoriesCllRcc(filteredListCllRcc)
                        adapterCllRcc.submitList(filteredListCllRcc)
                    } else {
                        adapterCllRcc.submitList(fullListCllRcc)
                    }
                }
            }
        })
        viewModelCllRcc.startDialerCllRcc.observe(viewLifecycleOwner) { numberCllRcc ->
            requireContext().checkPermissionsCllRcc(Manifest.permission.CALL_PHONE) {
                if (requireContext().checkSimCllRcc()) {
                    val uriCllRcc =
                        Uri.parse(getString(R.string.tel_cll_rcc) + numberCllRcc.trim())
                    startActivity(Intent(Intent.ACTION_CALL, uriCllRcc))
                }
            }
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
    }
}