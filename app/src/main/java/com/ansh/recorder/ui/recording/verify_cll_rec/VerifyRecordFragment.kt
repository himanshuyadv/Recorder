package com.ansh.recorder.ui.recording.verify_cll_rec

import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ansh.recorder.core.*
import com.ansh.recorder.core.data.database.repositories_cll_rec.RecordsRepository
import com.ansh.recorder.core.data.model.RecordModel
import com.ansh.recorder.core.utils.FilesManager.renameFileCllRcc
import com.ansh.recorder.core.utils.milliSecondsToTimer
import com.ansh.recorder.core.utils.showToastCllRcc
import com.ansh.recorder.core.utils.toUriCllRcc
import com.ansh.recorder.ui.MainActivity
import com.ansh.recorder.ui.recording.VoiceRecordViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.ansh.recorder.R
import com.ansh.recorder.databinding.FragmentVerificationRecordBinding
import java.io.File
import java.util.*
import kotlin.properties.Delegates


class VerifyRecordFragment : Fragment() {
    private lateinit var bindingCllRcc: FragmentVerificationRecordBinding
    private val viewModelCllRcc: VoiceRecordViewModel by activityViewModels()
    private var lastFileCllRcc: File? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var isSubscribed by Delegates.notNull<Boolean>()

    private var renameFileNameCllRcc = ""

    private var needSaveCllRcc = false

    private var recordModel: RecordModel? = null
    var rccCllRcc: RecordModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingCllRcc = FragmentVerificationRecordBinding.inflate(inflater)

        bindingCllRcc.isSubscribed = isSubscribed

        return bindingCllRcc.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        bindingCllRcc.isResume = false

//        PhonecallReceiver.callCallback = this

        val mediaPlayerCllRcc = MediaPlayer()
        var mCurrentPositionCllRcc: Int

        lifecycleScope.launch(Dispatchers.IO) {
            val filesCllRcc = ArrayList<File>()
            val dCllRcc = Environment.getExternalStorageDirectory()
                .toString() + getString(R.string.call_splash)
            val allRecordsCllRcc = listfCllRcc(dCllRcc, filesCllRcc);

            while (true) {
                delay(100)
                lastFileCllRcc = checkLastmodCllRcc(allRecordsCllRcc!!);
                mediaPlayerCllRcc.setDataSource(lastFileCllRcc?.absolutePath)
                try {
                    mediaPlayerCllRcc.prepare()
                } catch (eCllRcc: Exception) {

                    eCllRcc.printStackTrace()
                }
                bindingCllRcc.wave.setRawDataCllRcc(lastFileCllRcc?.readBytes()!!) {

                }
                if (Build.MANUFACTURER.lowercase() == getString(R.string.samsung_cll_rcc) && bindingCllRcc.textNumber.text.toString()
                        .isNotEmpty()
                ) {
                    val mpCllRcc = MediaPlayer()
                    mpCllRcc.setDataSource(
                        Environment.getExternalStorageDirectory()
                            .toString() + getString(R.string.call_two_slash) + lastFileCllRcc!!.name
                    )
                    mpCllRcc.prepare()
                    val durationCllRcc = mpCllRcc.duration
                    rccCllRcc = RecordModel(
                        numberCllRcc = bindingCllRcc.textNumber.text.toString(),
                        id = Random().nextInt(),
                        nameCllRcc = lastFileCllRcc!!.name,
                        durationCllRcc = durationCllRcc.toLong()
                    )
                    lifecycleScope.launch(Dispatchers.IO) {
                        RecordsRepository.saveRecordCllRcc(requireContext(), rccCllRcc!!)
                    }
                    mpCllRcc.release()
                }

                if (lastFileCllRcc!!.exists()) {
                    break
                }
            }


        }.invokeOnCompletion {
            lifecycleScope.launch(Dispatchers.Main) {
                PhonecallReceiver.liveDataNumberCllRcc?.observe(viewLifecycleOwner) {
                    bindingCllRcc.textNumber.text = it
                }

                RecordsRepository.getRecordsCllRcc().observe(requireActivity()) {
//                    Log.d("submitList1", it.toString())

                    it.forEach {
//                        Log.d("submitList13", lastFile?.name?.toString() + it.name)
                        if (lastFileCllRcc?.name?.contains(it.nameCllRcc) == true) {
//                            Log.d("submitList12", it.toString())
                            recordModel = it
                        }
                    }
                }

                val mHandlerCllRcc = Handler()
                requireActivity().runOnUiThread(object : Runnable {
                    override fun run() {
                        try {
                            mCurrentPositionCllRcc =
                                (mediaPlayerCllRcc.currentPosition * 100) / mediaPlayerCllRcc.duration
                            bindingCllRcc.redLineSeekBar.translationX =
                                (bindingCllRcc.wave.width * mCurrentPositionCllRcc.toFloat()) / 101.5f
//                        binding.wave.progress = mCurrentPosition.toFloat()

                            mHandlerCllRcc.postDelayed(this, 10)
                            bindingCllRcc.isResume = mediaPlayerCllRcc.isPlaying

                            // showing duration of the Recording in TextView

                            val mUpdateTimeTask = Runnable {
                                //    val totalDuration: Long = mediaPlayerCllRcc.duration.toLong()
                                val currentDuration: Long = mediaPlayerCllRcc
                                    .currentPosition.toLong()

                                // Displaying time completed playing

                                bindingCllRcc.tvVerRecTimer.text =
                                    milliSecondsToTimer(currentDuration)

                                // Running this thread after 100 milliseconds
                                //   Handler().postDelayed(this,0)
                            }

                            mUpdateTimeTask.run()


                        } catch (eCllRcc: Exception) {
                            eCllRcc.printStackTrace()
                        }
                    }
                })

                bindingCllRcc.resumePauseRec.setOnClickListener {
                    if (mediaPlayerCllRcc.isPlaying) mediaPlayerCllRcc.pause() else mediaPlayerCllRcc.start()

                }
                bottomButtonsCllRcc()
            }
        }





        bindingCllRcc.wave.onProgressListenerCllRcc = object : CustomProgressListener() {
            override fun onProgressChanged(progress: Float, byUser: Boolean) {
                if (byUser) {
                    mediaPlayerCllRcc.seekTo(((mediaPlayerCllRcc.duration * progress) / 100).toInt())
//                    if (!mediaPlayer.isPlaying)
//                        mediaPlayer.start()
                }
            }
        }
        renameFileCllRcc()
    }


    private fun bottomButtonsCllRcc() {
        with(bindingCllRcc) {
            clickSave.setOnClickListener {


                needSaveCllRcc = true
                showToastCllRcc(getString(R.string.saved_cll_rcc))


            }
            clickSaveGd.setOnClickListener {

                requireActivity().shareToDriveCllRcc(lastFileCllRcc?.absolutePath ?: "")

            }
            clickShare.setOnClickListener {

                requireContext().shareCllRcc(lastFileCllRcc?.toUriCllRcc(requireContext())!!)

            }
            clickDelete.setOnClickListener {
                deleteFileCllRcc()
                showToastCllRcc(getString(R.string.deleted))
                findNavController().popBackStack()
            }
        }
    }

    private fun deleteFileCllRcc() {
        if (Build.MANUFACTURER.lowercase() == getString(R.string.samsung_cll_rcc) && bindingCllRcc.textNumber.text.toString()
                .isNotEmpty()
        ) {
//            rcc = Record(
//                number = binding.textNumber.text.toString(),
//                id = Random().nextInt(),
//                name = lastFile!!.name
//            )
            lifecycleScope.launch(Dispatchers.IO) {
                RecordsRepository.deleteRecordCllRcc(requireContext(), rccCllRcc!!)
            }
        } else {
            GlobalScope.launch {
                RecordsRepository.deleteRecordCllRcc(requireContext(), recordModel!!)
            }
        }
        requireActivity().contentResolver.delete(
            lastFileCllRcc?.toUriCllRcc(requireContext())!!,
            null,
            null
        )
    }

    private fun renameFileCllRcc() {
        bindingCllRcc.textRename.addTextChangedListener(object : TextWatcher {
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

            override fun afterTextChanged(renameTextCllRcc: Editable?) {


                renameFileNameCllRcc = renameTextCllRcc.toString()
            }
        })
    }

    override fun onResume() {
        super.onResume()

        bindingCllRcc.textNumber.text = PhonecallReceiver.savedNumberCllRcc

        (requireActivity() as MainActivity).hideOptionsCllRcc()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        if (!needSaveCllRcc) {
//            deleteFile()

        } else {
            if (renameFileNameCllRcc != "") {
                GlobalScope.launch {
                    RecordsRepository.updateRecordCllRcc(
                        requireContext(),
                        recordModel?.apply {
                            nameCllRcc = "$renameFileNameCllRcc.m4a"
                        }!!
                    )
                }
                lastFileCllRcc?.renameFileCllRcc("$renameFileNameCllRcc.m4a")
            }
        }
        PhonecallReceiver.savedNumberCllRcc = null
        (requireActivity() as MainActivity).showOptionsCllRcc()
    }
}




