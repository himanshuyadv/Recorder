package com.ansh.recorder.ui.recording

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ansh.recorder.core.checkPermissionsForMemoryCllRcc
import com.ansh.recorder.R
import com.ansh.recorder.databinding.FragmentRecordBinding
import com.ansh.recorder.ui.recording.Timer.*
import java.util.*
import java.util.concurrent.TimeUnit


class RecordFragment : Fragment(), OnTimerTickListenerCllRcc {

    private lateinit var bindingCllRcc: FragmentRecordBinding
    private val viewModelCllRcc: VoiceRecordViewModel by viewModels()
    private var isRecordingCllRcc = false
    private var isPausedCllRcc = false

    private lateinit var timer: Timer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bindingCllRcc =
            DataBindingUtil.inflate(inflater, R.layout.fragment_record, container, false)



        return bindingCllRcc.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingCllRcc.isStarted = false
        timer = Timer(this)

        bindingCllRcc.btnStartRecord.setOnClickListener {

            if (requireActivity().checkPermissionsForMemoryCllRcc()) {

                when {
                    isPausedCllRcc -> resumeRecordingCllRcc()
                    isRecordingCllRcc -> pauseRecorderCllRcc()
                    else -> startRecordingCllRcc()
                }

                bindingCllRcc.isStarted = true
                viewModelCllRcc.startRecordingCllRcc(requireContext())
//            meter = SoundMeter()
//            meter.start()
                bindingCllRcc.btnStartRecord.isClickable = false

            } else {
                Toast.makeText(requireContext(), "allow storage permission", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        bindingCllRcc.saveRec.setOnClickListener {

            findNavController().navigate(RecordFragmentDirections.actionActionRecordToVerifyRecordFragment())
            viewModelCllRcc.finishRecordingCllRcc(requireContext()) {
                stopRecordCllRcc()
                bindingCllRcc.isStarted = false
                isRecordingCllRcc = false
                isPausedCllRcc = false
                viewModelCllRcc.recordingCllRcc = false
            }

        }

        bindingCllRcc.resumePauseRec.setOnClickListener {
            when {
                isPausedCllRcc -> {
                    resumeRecordingCllRcc()
                }
                isRecordingCllRcc -> {
                    pauseRecorderCllRcc()
                }
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.N)
    private fun pauseRecorderCllRcc() {

//        recorder.pause()
        isPausedCllRcc = true
        bindingCllRcc.isResume = true
        viewModelCllRcc.recordingCllRcc = false
        viewModelCllRcc.pauseResumeRecordingCllRcc()

        timer.pauseCllRccCllRcc()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun resumeRecordingCllRcc() {
//        recorder.resume()
        isPausedCllRcc = false
        bindingCllRcc.isResume = false
        viewModelCllRcc.recordingCllRcc = true
        viewModelCllRcc.pauseResumeRecordingCllRcc()

        timer.startCllRcc()
    }


    private fun startRecordingCllRcc() {

//        recorder = MediaRecorder()
//        dirPath = "${requireActivity().externalCacheDir?.absolutePath}/"

//        val simpleDateFormat = SimpleDateFormat("yyyy.MM.DD_hh.mm.ss")
//        val date = simpleDateFormat.format(Date())
//        fileName = "audio_record_$date"
//        recorder.apply {
//            setAudioSource(MediaRecorder.AudioSource.MIC)
//            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//            setOutputFile("$dirPath$fileName.mp3")
//
//            try {
//                prepare()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//            //recorder.isPrivacySensitive = false
//
//            start()
//
//        }

        isPausedCllRcc = false
        isRecordingCllRcc = true

        timer.startCllRcc()

    }

    private fun stopRecordCllRcc() {

        timer.stopCllRccCllRcc()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isRecordingCllRcc) {
            stopRecordCllRcc()
            viewModelCllRcc.finishRecordingCllRcc(requireContext()) {}
        }
    }

    override fun onTimerTickCllRcc(durationCllRcc: Long) {
//        val simpleDataFormatCllRcc = SimpleDateFormat(getString(R.string.mm_ss))
//        bindingCllRcc.timer.text = simpleDataFormatCllRcc.format(durationCllRcc)
        bindingCllRcc.timer.text = String.format(
            "%02d : %02d ",
            TimeUnit.MILLISECONDS.toMinutes(durationCllRcc),
            TimeUnit.MILLISECONDS.toSeconds(durationCllRcc) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(durationCllRcc))
        )

    }


    override fun onTimerTickWaveformCllRcc() {
        bindingCllRcc.waveform.addAmplitudeCllRcc(viewModelCllRcc.recorderCllRcc!!.recorderCllRcc!!.maxAmplitudeCllRcc.toFloat())
    }
}