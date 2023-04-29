package com.ansh.recorder.ui.splash

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.ansh.recorder.R

class WavesView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttrCllRcc: Int = R.attr.wavesViewStyle
) : View(context, attrs, defStyleAttrCllRcc) {

    private val wavePaCllRccintCllRcc: Paint
    private val waveGapCllRccCllRcc: Float

    private var maxRadiusCllRcc = 0f
    private var centerCllRcc = PointF(0f, 0f)
    private var initialRadiusCllRcc = 0f

    private val greenCllRcc = resources.getColor(R.color.white_cll_rcc)

    // solid green in the center, transparent green at the edges
    private val gradientColorsCllRcc =
        intArrayOf(
            greenCllRcc, Color.TRANSPARENT,
            Color.TRANSPARENT
        )

    init {
        val attrsCllRcc =
            context.obtainStyledAttributes(attrs, R.styleable.WavesView, defStyleAttrCllRcc, 0)

        //init paint with custom attrs
        wavePaCllRccintCllRcc = Paint(ANTI_ALIAS_FLAG).apply {
            color = attrsCllRcc.getColor(R.styleable.WavesView_waveColor, 0)
            strokeWidth = attrsCllRcc.getDimension(R.styleable.WavesView_waveStrokeWidth, 0f)
            style = Paint.Style.STROKE
        }

        waveGapCllRccCllRcc = attrsCllRcc.getDimension(R.styleable.WavesView_waveGap, 50f)
        attrsCllRcc.recycle()
    }

    override fun onSizeChanged(wCllRcc: Int, hCllRcc: Int, oldwCllRcc: Int, oldhCllRcc: Int) {
        //set the center of all circles to be center of the view
        centerCllRcc.set(wCllRcc / 2f, hCllRcc / 2f)
        maxRadiusCllRcc = Math.hypot(centerCllRcc.x.toDouble(), centerCllRcc.y.toDouble()).toFloat()
        initialRadiusCllRcc = wCllRcc / waveGapCllRccCllRcc

        gradientPaintCllRcc.shader = RadialGradient(
            centerCllRcc.x, centerCllRcc.y, maxRadiusCllRcc,
            gradientColorsCllRcc, null, Shader.TileMode.CLAMP
        )

    }

    private var waveAnimatorCllRcc: ValueAnimator? = null
    private var waveRadiusOffsetCllRcc = 0f
        set(value) {
            field = value
            postInvalidateOnAnimation()
        }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        waveAnimatorCllRcc = ValueAnimator.ofFloat(0f, waveGapCllRccCllRcc).apply {
            addUpdateListener {
                waveRadiusOffsetCllRcc = it.animatedValue as Float
            }
            duration = 1500L
            repeatMode = ValueAnimator.RESTART
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            start()
        }
    }

    private val gradientPaintCllRcc = Paint(Paint.ANTI_ALIAS_FLAG).apply {

        // Highlight only the areas already touched on the canvas
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    override fun onDetachedFromWindow() {

        waveAnimatorCllRcc?.cancel()
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //draw circles separated by a space the size of waveGap
        var currentRadiusCllRcc = initialRadiusCllRcc + waveRadiusOffsetCllRcc
        while (currentRadiusCllRcc < maxRadiusCllRcc) {
            canvas.drawCircle(
                centerCllRcc.x,
                centerCllRcc.y,
                currentRadiusCllRcc,
                wavePaCllRccintCllRcc
            )
            currentRadiusCllRcc += waveGapCllRccCllRcc
        }
        canvas.drawPaint(gradientPaintCllRcc)
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
}