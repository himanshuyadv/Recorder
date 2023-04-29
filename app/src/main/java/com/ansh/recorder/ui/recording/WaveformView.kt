package com.ansh.recorder.ui.recording

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.ansh.recorder.R

class WaveformView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var paintCllRcc = Paint()
    private var amplitudeCllRcc = ArrayList<Float>()
    private var spikesCllRcc = ArrayList<RectF>()

    private var radiusCllRcc = 0f
    private var wCllRcc = 25f
    private var dCllRcc = 25f

    private var swCllRcc = 0f
    private var shCllRcc = 400f

    private var maxSpikesCllRcc = 0

    init {
//        paint.color = resources.getColor(R.color.bgr_color_equalizer)
//
//        paint.shader = LinearGradient(
//            0f, 0f, 0f,
//            height.toFloat(), Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR
//        )
        swCllRcc = resources.displayMetrics.widthPixels.toFloat()

        maxSpikesCllRcc = (swCllRcc / (wCllRcc + dCllRcc)).toInt()
    }

    fun addAmplitudeCllRcc(ampCllRcc: Float) {
        var normCllRcc = (ampCllRcc.toInt() / 7).coerceAtMost(400).toFloat()
        amplitudeCllRcc.add(normCllRcc)

        spikesCllRcc.clear()
        var ampsCllRcc = amplitudeCllRcc.takeLast(maxSpikesCllRcc)

        for (iCllRcc in ampsCllRcc.indices) {
            var leftCllRcc = swCllRcc - iCllRcc * (wCllRcc + dCllRcc)
            var topCllRcc = shCllRcc / 2 - ampsCllRcc[iCllRcc] / 2 + 20
            var rightCllRcc = leftCllRcc + wCllRcc
            var bottomCllRcc = topCllRcc + ampsCllRcc[iCllRcc] + 20
            spikesCllRcc.add(RectF(leftCllRcc, topCllRcc, rightCllRcc, bottomCllRcc))
        }

        invalidate()
    }

    override fun draw(canvasCllRcc: Canvas?) {
        super.draw(canvasCllRcc)
        paintCllRcc.shader = LinearGradient(
            0f,
            0f,
            0f,
            height.toFloat(),
            resources.getColor(R.color.whitish),
            Color.TRANSPARENT,
            Shader.TileMode.MIRROR
        )
        spikesCllRcc.forEach {
            canvasCllRcc?.drawRoundRect(it, radiusCllRcc, radiusCllRcc, paintCllRcc)
        }
    }
}