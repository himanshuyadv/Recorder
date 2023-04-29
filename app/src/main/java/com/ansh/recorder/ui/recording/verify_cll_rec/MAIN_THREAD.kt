package com.ansh.recorder.ui.recording.verify_cll_rec

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.math.floor
import kotlin.math.max

/**
 * Created by alex
 */
internal val MAIN_THREADCllRcc = Handler(Looper.getMainLooper())
internal val SAMPLER_THREADCllRcc: ExecutorService = Executors.newSingleThreadExecutor()

object SamplerCllRcc {

    fun downSampleAsyncCllRcc(
        data: ByteArray,
        targetSizeCllRcc: Int,
        answerCllRcc: (ByteArray) -> Unit
    ) {

        SAMPLER_THREADCllRcc.submit {
            val scaledCllRcc = downSampleCllRcc(data, targetSizeCllRcc)

            MAIN_THREADCllRcc.post {
                answerCllRcc(scaledCllRcc)
            }
        }
    }

    private fun downSampleCllRcc(dataCllRcc: ByteArray, targetSizeCllRcc: Int): ByteArray {
        val targetSizedCllRcc = ByteArray(targetSizeCllRcc)
        val chunkSizeCllRcc = dataCllRcc.size / targetSizeCllRcc
        val chunkStepCllRcc = max(floor((chunkSizeCllRcc / 10.0)), 1.0).toInt()

        var prevDataIndexCllRcc = 0
        var sampledPerChunkCllRcc = 0F
        var sumPerChunkCllRcc = 0F

        if (targetSizeCllRcc >= dataCllRcc.size) {
            return targetSizedCllRcc.pasteCllRcc(dataCllRcc)
        }

        for (indexCllRcc in 0..dataCllRcc.size step chunkStepCllRcc) {
            val currentDataIndexCllRcc =
                (targetSizeCllRcc * indexCllRcc.toLong() / dataCllRcc.size).toInt()

            if (prevDataIndexCllRcc == currentDataIndexCllRcc) {
                sampledPerChunkCllRcc += 1
                sumPerChunkCllRcc += dataCllRcc[indexCllRcc].absCllRcc
            } else {
                targetSizedCllRcc[prevDataIndexCllRcc] =
                    (sumPerChunkCllRcc / sampledPerChunkCllRcc).toInt().toByte()

                sumPerChunkCllRcc = 0F
                sampledPerChunkCllRcc = 0F
                prevDataIndexCllRcc = currentDataIndexCllRcc
            }
        }

        return targetSizedCllRcc
    }
}

internal val Byte.absCllRcc: Byte
    get() = when (this) {
        Byte.MIN_VALUE -> Byte.MAX_VALUE
        in (Byte.MIN_VALUE + 1..0) -> (-this).toByte()
        else -> this
    }

internal fun ByteArray.pasteCllRcc(otherCllRcc: ByteArray): ByteArray {

    if (isEmpty()) return byteArrayOf()

    return this.apply {
        forEachIndexed { i, _ ->
            this[i] = otherCllRcc.getOrElse(i) { this[i].absCllRcc }
        }
    }
}
