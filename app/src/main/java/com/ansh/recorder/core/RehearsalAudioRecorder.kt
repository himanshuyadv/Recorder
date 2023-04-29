package com.ansh.recorder.core

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.AudioRecord.OnRecordPositionUpdateListener
import android.media.MediaRecorder
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.ShortBuffer
import java.nio.channels.FileChannel

class RehearsalAudioRecorder @SuppressLint("MissingPermission") constructor(
    uncompressedCllRcc: Boolean,
    audioSourceCllRcc: Int,
    sampleRateCllRcc: Int,
    channelConfigCllRcc: Int,
    audioFormatCllRcc: Int
) {
    enum class StateCllRcc {
        INITIALIZINGCllRcc, READYCllRcc, RECORDINGCllRcc, ERRORCllRcc, STOPPEDCllRcc
    }

    // Toggles uncompressed recording on/off; RECORDING_UNCOMPRESSED / RECORDING_COMPRESSED
    private var rUncompressedCllRcc = false

    // Recorder used for uncompressed recording
    private var aRecorderCllRcc: AudioRecord? = null

    // Recorder used for compressed recording
    private var mRecorderCllRcc: MediaRecorder? = null

    // Stores current amplitude (only in uncompressed mode)
    private var cAmplitudeCllRcc = 0

    // Output file path
    private var fPathCllRcc: String? = null

    // Recorder state; see State
    var stateCllRcc: StateCllRcc? = null
        private set

    // File writer (only in uncompressed mode)
    private var fWriterCllRcc: RandomAccessFile? = null
    private var fChannelCllRcc: FileChannel? = null

    // Number of channels, sample rate, sample size(size in bits), buffer size, audio source, sample size(see AudioFormat)
    private var nChannelsCllRcc: Short = 0
    private var sRateCllRcc = 0
    private var bSamplesCllRcc: Short = 0
    private var bufferSizeCllRcc = 0
    private var aSourceCllRcc = 0
    private var aFormatCllRcc = 0

    // Number of frames written to file on each output(only in uncompressed mode)
    private var framePeriodCllRcc = 0

    // Buffer for output(only in uncompressed mode)
    private var shBufferCllRcc: ShortBuffer? = null
    private var bBufferCllRcc: ByteBuffer? = null

    // Number of bytes written to file after header(only in uncompressed mode)
    // after stop() is called, this size is written to the header/data chunk in the wave file
    private var payloadSizeCllRcc = 0

    // Apply gain - Supported only in uncompressed recording
    private var gainCllRcc // Gain converted from dB to multiplier;
            = 0.0

    // Based on info found in WaveGain application
    // multiplier = 10 ^ (0.05 * dB)
    // Flag indicating that recording is paused - Supported only in uncompressed recording
    var isPausedCllRcc = false
        private set
    private val updateListenerCllRcc: OnRecordPositionUpdateListener =
        object : OnRecordPositionUpdateListener {
            override fun onPeriodicNotification(recorderCllRcc: AudioRecord) {
                GlobalScope.launch {
                    aRecorderCllRcc!!.read(
                        bBufferCllRcc!!,
                        bBufferCllRcc!!.capacity()
                    ) // Fill buffer

                    // if recording is paused, ignore audio data
                    if (isPausedCllRcc) return@launch
                    try {
                        if (bSamplesCllRcc.toInt() == 16) {
                            shBufferCllRcc!!.rewind()
                            val bLengthCllRcc =
                                shBufferCllRcc!!.capacity() // Faster than accessing buffer.capacity each time
                            for (iCllRcc in 0 until bLengthCllRcc) { // 16bit sample size
                                val curSampleCllRcc =
                                    (shBufferCllRcc!![iCllRcc] * gainCllRcc).toInt().toShort()
                                //                        Log.e("gain",String.valueOf(gain));
                                if (curSampleCllRcc > cAmplitudeCllRcc) { // Check amplitude
                                    cAmplitudeCllRcc = curSampleCllRcc.toInt()
                                }
                                shBufferCllRcc!!.put(curSampleCllRcc)
                            }
                        } else { // 8bit sample size
                            val bLengthCllRcc =
                                bBufferCllRcc!!.capacity() // Faster than accessing buffer.capacity each time
                            bBufferCllRcc!!.rewind()
                            for (iCllRcc in 0 until bLengthCllRcc) {
                                val curSampleCllRcc =
                                    (bBufferCllRcc!![iCllRcc] * gainCllRcc).toInt().toByte()
                                if (curSampleCllRcc > cAmplitudeCllRcc) { // Check amplitude
                                    cAmplitudeCllRcc = curSampleCllRcc.toInt()
                                }
                                bBufferCllRcc!!.put(curSampleCllRcc)
                            }
                        }
                        bBufferCllRcc!!.rewind()
                        fChannelCllRcc!!.write(bBufferCllRcc) // Write buffer to file
                        payloadSizeCllRcc += bBufferCllRcc!!.capacity()
                    } catch (eCllRcc: IOException) {
                        Log.e(
                            RehearsalAudioRecorder::class.java.name,
                            "Error occured in updateListener, recording is aborted"
                        )
                        stopCllRcc()
                    }
                }
            }

            override fun onMarkerReached(recorderCllRcc: AudioRecord) {

                // NOT USED
            }
        }

    fun setGainCllRcc(dBGainCllRcc: Double) {
        gainCllRcc = Math.pow(10.0, dBGainCllRcc * 0.05)
//        Log.d("Gain", java.lang.Double.toString(gain))
    }

    fun setOutputFileCllRcc(argPathCllRcc: String?) {
        try {
            if (stateCllRcc == StateCllRcc.INITIALIZINGCllRcc) {
                fPathCllRcc = argPathCllRcc
                if (!rUncompressedCllRcc) {
                    mRecorderCllRcc!!.setOutputFile(fPathCllRcc)
                }
            }
        } catch (eCllRcc: Exception) {
            if (eCllRcc.message != null) {
                Log.e(RehearsalAudioRecorder::class.java.name, eCllRcc.message!!)
            } else {
                Log.e(
                    RehearsalAudioRecorder::class.java.name,
                    "Unknown error occured while setting output path"
                )
            }
            stateCllRcc = StateCllRcc.ERRORCllRcc
        }
    }

    /**
     *
     * Returns the largest amplitude sampled since the last call to this method.
     *
     * @return returns the largest amplitude since the last call, or 0 when not in recording state.
     */
    val maxAmplitudeCllRcc: Int
        get() = if (stateCllRcc == StateCllRcc.RECORDINGCllRcc) {
            if (rUncompressedCllRcc) {
                val result = cAmplitudeCllRcc
                cAmplitudeCllRcc = 0
                result
            } else {
                try {
                    mRecorderCllRcc!!.maxAmplitude
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                    0
                } catch (e: Exception) {
                    e.printStackTrace()
                    0
                }
            }
        } else {
            0
        }

    fun prepareCllRcc() {
        try {
            if (stateCllRcc == StateCllRcc.INITIALIZINGCllRcc) {
                if (rUncompressedCllRcc) {
                    if ((aRecorderCllRcc!!.state == AudioRecord.STATE_INITIALIZED) and (fPathCllRcc != null)) {
                        // write file header
                        fWriterCllRcc = RandomAccessFile(fPathCllRcc, "rw")
                        fChannelCllRcc = fWriterCllRcc!!.channel
                        fWriterCllRcc!!.setLength(0) // Set file length to 0, to prevent unexpected behavior in case the file already existed
                        fWriterCllRcc!!.writeBytes("RIFF")
                        fWriterCllRcc!!.writeInt(0) // Final file size not known yet, write 0
                        fWriterCllRcc!!.writeBytes("WAVE")
                        fWriterCllRcc!!.writeBytes("fmt ")
                        fWriterCllRcc!!.writeInt(Integer.reverseBytes(16)) // Sub-chunk size, 16 for PCM
                        fWriterCllRcc!!.writeShort(
                            java.lang.Short.reverseBytes(1.toShort()).toInt()
                        ) // AudioFormat, 1 for PCM
                        fWriterCllRcc!!.writeShort(
                            java.lang.Short.reverseBytes(nChannelsCllRcc).toInt()
                        ) // Number of channels, 1 for mono, 2 for stereo
                        fWriterCllRcc!!.writeInt(Integer.reverseBytes(sRateCllRcc)) // Sample rate
                        fWriterCllRcc!!.writeInt(Integer.reverseBytes(sRateCllRcc * bSamplesCllRcc * nChannelsCllRcc / 8)) // Byte rate, SampleRate*NumberOfChannels*BitsPerSample/8
                        fWriterCllRcc!!.writeShort(
                            java.lang.Short.reverseBytes((nChannelsCllRcc * bSamplesCllRcc / 8).toShort())
                                .toInt()
                        ) // Block align, NumberOfChannels*BitsPerSample/8
                        fWriterCllRcc!!.writeShort(
                            java.lang.Short.reverseBytes(bSamplesCllRcc).toInt()
                        ) // Bits per sample
                        fWriterCllRcc!!.writeBytes("data")
                        fWriterCllRcc!!.writeInt(0) // Data chunk size not known yet, write 0
                        bBufferCllRcc =
                            ByteBuffer.allocateDirect(framePeriodCllRcc * bSamplesCllRcc / 8 * nChannelsCllRcc)
                        bBufferCllRcc?.order(ByteOrder.LITTLE_ENDIAN)
                        bBufferCllRcc?.rewind()
                        shBufferCllRcc = bBufferCllRcc?.asShortBuffer()
                        stateCllRcc = StateCllRcc.READYCllRcc
                    } else {
                        Log.e(
                            RehearsalAudioRecorder::class.java.name,
                            "prepare() method called on uninitialized recorder"
                        )
                        stateCllRcc = StateCllRcc.ERRORCllRcc
                    }
                } else {
                    mRecorderCllRcc!!.prepare()
                    stateCllRcc = StateCllRcc.READYCllRcc
                }
            } else {
                Log.e(
                    RehearsalAudioRecorder::class.java.name,
                    "prepare() method called on illegal state"
                )
                releaseCllRcc()
                stateCllRcc = StateCllRcc.ERRORCllRcc
            }
        } catch (eCllRcc: Exception) {
            if (eCllRcc.message != null) {
                Log.e(RehearsalAudioRecorder::class.java.name, eCllRcc.message!!)
            } else {
                Log.e(
                    RehearsalAudioRecorder::class.java.name,
                    "Unknown error occured in prepare()"
                )
            }
            stateCllRcc = StateCllRcc.ERRORCllRcc
        }
    }

    /**
     *
     *
     * Releases the resources associated with this class, and removes the unnecessary files, when necessary
     *
     */
    fun releaseCllRcc() {
        if (stateCllRcc == StateCllRcc.RECORDINGCllRcc) {
            stopCllRcc()
        } else {
            if ((stateCllRcc == StateCllRcc.READYCllRcc) and rUncompressedCllRcc) {
                try {
                    fWriterCllRcc!!.close() // Remove prepared file
                } catch (e: IOException) {
                    Log.e(
                        RehearsalAudioRecorder::class.java.name,
                        "I/O exception occured while closing output file"
                    )
                }
                File(fPathCllRcc).delete()
            }
        }
        if (rUncompressedCllRcc) {
            if (aRecorderCllRcc != null) {
                aRecorderCllRcc!!.release()
            }
        } else {
            if (mRecorderCllRcc != null) {
                mRecorderCllRcc!!.release()
            }
        }
    }

    /**
     *
     *
     * Resets the recorder to the INITIALIZING state, as if it was just created.
     * In case the class was in RECORDING state, the recording is stopped.
     * In case of exceptions the class is set to the ERROR state.
     *
     */
    @SuppressLint("MissingPermission")
    fun resetCllRcc() {
        try {
            if (stateCllRcc != StateCllRcc.ERRORCllRcc) {
                releaseCllRcc()
                fPathCllRcc = null // Reset file path
                cAmplitudeCllRcc = 0 // Reset amplitude
                gainCllRcc = 1.0
                if (rUncompressedCllRcc) {
                    aRecorderCllRcc = AudioRecord(
                        aSourceCllRcc,
                        sRateCllRcc,
                        nChannelsCllRcc + 1,
                        aFormatCllRcc,
                        bufferSizeCllRcc
                    )
                } else {
                    mRecorderCllRcc = MediaRecorder()
                    mRecorderCllRcc!!.setAudioSource(MediaRecorder.AudioSource.MIC)
                    mRecorderCllRcc!!.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT)
                    mRecorderCllRcc!!.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
                }
                stateCllRcc = StateCllRcc.INITIALIZINGCllRcc
            }
        } catch (eCllRcc: Exception) {
            Log.e(RehearsalAudioRecorder::class.java.name, eCllRcc.message!!)
            stateCllRcc = StateCllRcc.ERRORCllRcc
        }
    }

    /**
     *
     *
     * Starts the recording, and sets the state to RECORDING.
     * Call after prepare().
     *
     */
    fun startCllRcc() {
        if (stateCllRcc == StateCllRcc.READYCllRcc) {
            if (rUncompressedCllRcc) {
                payloadSizeCllRcc = 0
                aRecorderCllRcc!!.startRecording()
                aRecorderCllRcc!!.read(bBufferCllRcc!!, bBufferCllRcc!!.capacity())
                bBufferCllRcc!!.rewind()
            } else {
                try {
                    mRecorderCllRcc?.prepare()
                } catch (eCllRcc: Exception) {
                    eCllRcc.printStackTrace()
                }
                mRecorderCllRcc!!.start()
            }
            stateCllRcc = StateCllRcc.RECORDINGCllRcc
        } else {
            Log.e(RehearsalAudioRecorder::class.java.name, "start() called on illegal state")
            stateCllRcc = StateCllRcc.ERRORCllRcc
        }
    }

    /**
     *
     *
     * Stops the recording, and sets the state to STOPPED.
     * In case of further usage, a reset is needed.
     * Also finalizes the wave file in case of uncompressed recording.
     *
     */
    fun stopCllRcc() {
        if (stateCllRcc == StateCllRcc.RECORDINGCllRcc) {
            if (rUncompressedCllRcc) {
                aRecorderCllRcc!!.stop()
                try {
                    fWriterCllRcc!!.seek(4) // Write size to RIFF header
                    fWriterCllRcc!!.writeInt(Integer.reverseBytes(36 + payloadSizeCllRcc))
                    fWriterCllRcc!!.seek(40) // Write size to Subchunk2Size field
                    fWriterCllRcc!!.writeInt(Integer.reverseBytes(payloadSizeCllRcc))
                    fWriterCllRcc!!.close()
                } catch (e: IOException) {
                    Log.e(
                        RehearsalAudioRecorder::class.java.name,
                        "I/O exception occured while closing output file"
                    )
                    stateCllRcc = StateCllRcc.ERRORCllRcc
                }
            } else {
                mRecorderCllRcc!!.stop()

            }
            stateCllRcc = StateCllRcc.STOPPEDCllRcc
        } else {
            Log.e(RehearsalAudioRecorder::class.java.name, "stop() called on illegal state")
            stateCllRcc = StateCllRcc.ERRORCllRcc
        }
    }

    fun togglePauseCllRcc() {

        isPausedCllRcc = !isPausedCllRcc
    }

    companion object {
        // The interval in which the recorded samples are output to the file
        // Used only in uncompressed mode
        private const val TIMER_INTERVALCllRcc = 120
    }

    init {
        try {
            rUncompressedCllRcc = uncompressedCllRcc
            if (rUncompressedCllRcc) { // RECORDING_UNCOMPRESSED
                bSamplesCllRcc = if (audioFormatCllRcc == AudioFormat.ENCODING_PCM_16BIT) {
                    16
                } else {
                    8
                }
                nChannelsCllRcc =
                    if (channelConfigCllRcc == AudioFormat.CHANNEL_CONFIGURATION_MONO) {
                        1
                    } else {
                        2
                    }
                aSourceCllRcc = audioSourceCllRcc
                sRateCllRcc = sampleRateCllRcc
                aFormatCllRcc = audioFormatCllRcc
                framePeriodCllRcc = sampleRateCllRcc * TIMER_INTERVALCllRcc / 1000
                bufferSizeCllRcc = framePeriodCllRcc * 2 * bSamplesCllRcc * nChannelsCllRcc / 8
                if (bufferSizeCllRcc < AudioRecord.getMinBufferSize(
                        sampleRateCllRcc,
                        channelConfigCllRcc,
                        audioFormatCllRcc
                    )
                ) { // Check to make sure buffer size is not smaller than the smallest allowed one
                    bufferSizeCllRcc =
                        AudioRecord.getMinBufferSize(
                            sampleRateCllRcc,
                            channelConfigCllRcc,
                            audioFormatCllRcc
                        )
                    // Set frame period and timer interval accordingly
                    framePeriodCllRcc =
                        bufferSizeCllRcc / (2 * bSamplesCllRcc * nChannelsCllRcc / 8)
                    Log.w(
                        RehearsalAudioRecorder::class.java.name,
                        "Increasing buffer size to " + Integer.toString(bufferSizeCllRcc)
                    )
                }
                aRecorderCllRcc =
                    AudioRecord(
                        audioSourceCllRcc,
                        sampleRateCllRcc,
                        channelConfigCllRcc,
                        audioFormatCllRcc,
                        bufferSizeCllRcc
                    )
                if (aRecorderCllRcc!!.state != AudioRecord.STATE_INITIALIZED) throw Exception("AudioRecord initialization failed")
                aRecorderCllRcc!!.setRecordPositionUpdateListener(updateListenerCllRcc)
                aRecorderCllRcc!!.positionNotificationPeriod = framePeriodCllRcc
            } else { // RECORDING_COMPRESSED

                mRecorderCllRcc = MediaRecorder()
                mRecorderCllRcc!!.setAudioSource(MediaRecorder.AudioSource.DEFAULT)
                mRecorderCllRcc!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                mRecorderCllRcc!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB)
                mRecorderCllRcc!!.setAudioEncodingBitRate(128000)
                mRecorderCllRcc!!.setAudioSamplingRate(44100)

            }
            cAmplitudeCllRcc = 0
            gainCllRcc = 1.0
            fPathCllRcc = null
            stateCllRcc = StateCllRcc.INITIALIZINGCllRcc
        } catch (eCllRcc: Exception) {
            if (eCllRcc.message != null) {
                Log.e(RehearsalAudioRecorder::class.java.name, eCllRcc.message!!)
            } else {
                Log.e(
                    RehearsalAudioRecorder::class.java.name,
                    "Unknown error occured while initializing recording"
                )
            }
            stateCllRcc = StateCllRcc.ERRORCllRcc
        }
    }
}