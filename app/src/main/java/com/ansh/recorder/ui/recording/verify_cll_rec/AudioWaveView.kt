package com.ansh.recorder.ui.recording.verify_cll_rec

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.OvershootInterpolator
import rm.com.audiowave.OnProgressListener
import kotlin.math.abs
import kotlin.math.max

class AudioWaveView : View {


    constructor(context: Context?) : super(context) {
        setWillNotDraw(false)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        setWillNotDraw(false)
        inflateAttrsCllRcc(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttrCllRcc: Int) : super(
        context, attrs,
        defStyleAttrCllRcc
    ) {
        setWillNotDraw(false)
        inflateAttrsCllRcc(attrs)
    }

    var onProgressListenerCllRcc: OnProgressListener? = null

    var onProgressChangedCllRcc: (Float, Boolean) -> Unit = { _CllRcc, _CllR -> Unit }

    var onStartTrackingCllRcc: (Float) -> Unit = {}

    var onStopTrackingCllRcc: (Float) -> Unit = {}

    var chunkHeightCllRcc: Int = 0
        get() = if (field == 0) hCllRcc else abs(field)
        set(value) {
            field = value
            redrawDataCllRccCllRcc()
        }

    var chunkWidthCllRcc: Int = dipCllRcc(2)
        set(value) {
            field = abs(value)
            redrawDataCllRccCllRcc()
        }

    private var chunkSpacingCllRcc: Int = dipCllRcc(1)
        set(value) {
            field = abs(value)
            redrawDataCllRccCllRcc()
        }

    private var chunkRadiusCllRcc: Int = 0
        set(value) {
            field = abs(value)
            redrawDataCllRccCllRcc()
        }

    var minChunkHeightCllRcc: Int = dipCllRcc(2)
        set(value) {
            field = abs(value)
            redrawDataCllRccCllRcc()
        }

    private var waveColorCllRcc: Int = Color.BLACK
        set(value) {
//            wavePaint = smoothPaint(value)
//            waveFilledPaint = filterPaint(value)
            wavePaintCllRcc.shader =
                LinearGradient(
                    0f,
                    0f,
                    0f,
                    chunkHeightCllRcc.toFloat(),
                    Color.BLACK,
                    Color.WHITE,
                    Shader.TileMode.MIRROR
                )
            waveFilledPaintCllRcc.shader =
                LinearGradient(
                    0f,
                    0f,
                    0f,
                    chunkHeightCllRcc.toFloat(),
                    Color.BLACK,
                    Color.WHITE,
                    Shader.TileMode.MIRROR
                )
            redrawDataCllRccCllRcc()
            field = value
        }

    private var progressCllRcc: Float = 0F
        set(value) {
            require(value in 0.0..100.0) { "Progress must be in 0..100" }

            field = abs(value)

            onProgressListenerCllRcc?.onProgressChanged(field, isTouchedCllRcc)
            onProgressChangedCllRcc(field, isTouchedCllRcc)

            postInvalidate()
        }

    private var scaledDataCllRcc: ByteArray = byteArrayOf()
        set(value) {
            field = if (value.size <= chunksCountCllRcc) {
                ByteArray(chunksCountCllRcc).pasteCllRcc(value)
            } else {
                value
            }

            redrawDataCllRccCllRcc()
        }

    private var expansionDurationCllRcc: Long = 400
        set(value) {
            field = max(400, value)
            expansionAnimatorCllRcc.duration = field
        }

    var isExpansionAnimatedCllRcc: Boolean = true

    var isTouchableCllRcc = true

    var isTouchedCllRcc = false
        private set

    private val chunksCountCllRcc: Int
        get() = wCllRcc / chunkStepCllRcc

    private val chunkStepCllRcc: Int
        get() = chunkWidthCllRcc + chunkSpacingCllRcc

    private val centerYCllRcc: Int
        get() = hCllRcc / 2

    private val progressFactorCllRcc: Float
        get() = progressCllRcc / 100F

    private val initialDelayCllRcc: Long = 50

    private val expansionAnimatorCllRcc = ValueAnimator.ofFloat(0.0F, 1.0F).apply {
        duration = expansionDurationCllRcc
        interpolator = OvershootInterpolator()
        addUpdateListener {
            redrawDataCllRccCllRcc(factorCllRcc = it.animatedFraction)
        }
    }

    private var wavePaintCllRcc = smoothPaintCllRccCllRcc(waveColorCllRcc.withAlphaCllRcc(0xAA))
    private var waveFilledPaintCllRcc = filterPaintCllRcc(waveColorCllRcc)
    private var waveBitmapCllRcc: Bitmap? = null

    private var wCllRcc: Int = 0
    private var hCllRcc: Int = 0

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val cvCllRcc = canvas ?: return

        cvCllRcc.transformCllRcc {
            clipRect(0, 0, wCllRcc, hCllRcc)
            drawBitmap(waveBitmapCllRcc!!, 0F, 0F, wavePaintCllRcc)
        }

        cvCllRcc.transformCllRcc {

            clipRect(0F, 0F, wCllRcc * progressFactorCllRcc, hCllRcc.toFloat())
            drawBitmap(waveBitmapCllRcc!!, 0F, 0F, waveFilledPaintCllRcc)
        }
    }

    // suppressed here since we allocate only once,
    // when the wave bounds have been just calculated(it happens once)
    @SuppressLint("DrawAllocation")
    override fun onLayout(
        changedCllRcc: Boolean,
        leftCllRcc: Int,
        topCllRcc: Int,
        rightCllRcc: Int,
        bottomCllRcc: Int
    ) {
        wCllRcc = rightCllRcc - leftCllRcc
        hCllRcc = bottomCllRcc - topCllRcc

        if (waveBitmapCllRcc.fitsCllRcc(wCllRcc, hCllRcc)) {
            return
        }


        if (changedCllRcc) {
            waveBitmapCllRcc.safeRecycleCllRcc()
            waveBitmapCllRcc = Bitmap.createBitmap(wCllRcc, hCllRcc, Bitmap.Config.ARGB_8888)

            // absolutely ridiculous hack to draw wave in RecyclerView items
            scaledDataCllRcc = when (scaledDataCllRcc.size) {
                0 -> byteArrayOf()
                else -> scaledDataCllRcc
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(eventCllRcc: MotionEvent?): Boolean {
        eventCllRcc ?: return super.onTouchEvent(eventCllRcc)

        if (!isTouchableCllRcc || !isEnabled) {
            return false
        }

        when (eventCllRcc.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouchedCllRcc = true
                progressCllRcc = eventCllRcc.toProgressCllRcc()

                // these paired calls look ugly, but we need them for Java
                onProgressListenerCllRcc?.onStartTracking(progressCllRcc)
                onStartTrackingCllRcc(progressCllRcc)

                return true
            }
            MotionEvent.ACTION_MOVE -> {
                isTouchedCllRcc = true
                progressCllRcc = eventCllRcc.toProgressCllRcc()
                return true
            }
            MotionEvent.ACTION_UP -> {
                isTouchedCllRcc = false
                onProgressListenerCllRcc?.onStopTracking(progressCllRcc)
                onStopTrackingCllRcc(progressCllRcc)
                return false
            }
            else -> {
                isTouchedCllRcc = false
                return super.onTouchEvent(eventCllRcc)
            }
        }
    }


    @JvmOverloads
    fun setRawDataCllRcc(rawCllRcc: ByteArray, callbackCllRcc: () -> Unit = {}) {
        MAIN_THREADCllRcc.postDelayed({
            SamplerCllRcc.downSampleAsyncCllRcc(rawCllRcc, chunksCountCllRcc) {
                scaledDataCllRcc = it
                callbackCllRcc()

                if (isExpansionAnimatedCllRcc) {
                    animateExpansionCllRcc()
                }
            }
        }, initialDelayCllRcc)
    }

    private fun MotionEvent.toProgressCllRcc() =
        this@toProgressCllRcc.x.clampCllRcc(0F, wCllRcc.toFloat()) / wCllRcc * 100F

    private fun redrawDataCllRccCllRcc(
        canvasCllRcc: Canvas? = waveBitmapCllRcc?.inCanvasCllRcc(),
        factorCllRcc: Float = 1.0F
    ) {
        if (waveBitmapCllRcc == null || canvasCllRcc == null) return

        waveBitmapCllRcc.flushCllRcc()

        scaledDataCllRcc.forEachIndexed { iCllRcc, chunkCllRcc ->
            val chunkHeightCllRcc =
                ((chunkCllRcc.absCllRcc.toFloat() / Byte.MAX_VALUE) * chunkHeightCllRcc).toInt()
            val clampedHeightCllRcc = max(chunkHeightCllRcc, minChunkHeightCllRcc)
            val heightDiffCllRcc = (clampedHeightCllRcc - minChunkHeightCllRcc).toFloat()
            val animatedDiffCllRcc = (heightDiffCllRcc * factorCllRcc).toInt()

            canvasCllRcc.drawRoundRect(
                rectFOfCllRcc(
                    leftCllRcc = chunkSpacingCllRcc / 2 + iCllRcc * chunkStepCllRcc,
                    topCllRcc = centerYCllRcc - minChunkHeightCllRcc - animatedDiffCllRcc,
                    rightCllRcc = chunkSpacingCllRcc / 2 + iCllRcc * chunkStepCllRcc + chunkWidthCllRcc,
                    bottomCllRcc = centerYCllRcc + minChunkHeightCllRcc + animatedDiffCllRcc
                ),
                chunkRadiusCllRcc.toFloat(),
                chunkRadiusCllRcc.toFloat(),
                wavePaintCllRcc
            )
        }

        postInvalidate()
    }

    private fun animateExpansionCllRcc() {

        expansionAnimatorCllRcc.start()
    }

    private fun inflateAttrsCllRcc(attrs: AttributeSet?) {
        val resAttrsCllRcc = context.theme.obtainStyledAttributes(
            attrs,
            rm.com.audiowave.R.styleable.AudioWaveView,
            0,
            0
        )

        with(resAttrsCllRcc) {
            chunkHeightCllRcc = getDimensionPixelSize(
                rm.com.audiowave.R.styleable.AudioWaveView_chunkHeight,
                chunkHeightCllRcc
            )
            chunkWidthCllRcc = getDimensionPixelSize(
                rm.com.audiowave.R.styleable.AudioWaveView_chunkWidth,
                chunkWidthCllRcc
            )
            chunkSpacingCllRcc = getDimensionPixelSize(
                rm.com.audiowave.R.styleable.AudioWaveView_chunkSpacing,
                chunkSpacingCllRcc
            )
            minChunkHeightCllRcc = getDimensionPixelSize(
                rm.com.audiowave.R.styleable.AudioWaveView_minChunkHeight,
                minChunkHeightCllRcc
            )
            chunkRadiusCllRcc = getDimensionPixelSize(
                rm.com.audiowave.R.styleable.AudioWaveView_chunkRadius,
                chunkRadiusCllRcc
            )
            isTouchableCllRcc =
                getBoolean(rm.com.audiowave.R.styleable.AudioWaveView_touchable, isTouchableCllRcc)
            waveColorCllRcc =
                getColor(rm.com.audiowave.R.styleable.AudioWaveView_waveColor, waveColorCllRcc)
            progressCllRcc =
                getFloat(rm.com.audiowave.R.styleable.AudioWaveView_progress, progressCllRcc)
            isExpansionAnimatedCllRcc = getBoolean(
                rm.com.audiowave.R.styleable.AudioWaveView_animateExpansion,
                isExpansionAnimatedCllRcc
            )
            recycle()
        }
    }
}