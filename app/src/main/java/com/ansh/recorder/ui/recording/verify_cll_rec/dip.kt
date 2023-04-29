package com.ansh.recorder.ui.recording.verify_cll_rec

import android.graphics.*
import android.view.View


/**
 * Created by alex
 */


internal fun View.dipCllRcc(value: Int): Int = (value * resources.displayMetrics.density).toInt()

internal fun smoothPaintCllRccCllRcc(colorCllRcc: Int = Color.WHITE): Paint =
    Paint().apply {

//      isAntiAlias = true
//      this.color = color
        shader =
            LinearGradient(0f, 0f, 0f, 100f, Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR)
    }

internal fun filterPaintCllRcc(colorCllRcc: Int = Color.BLACK): Paint =
    Paint().apply {
        //      isAntiAlias = true
//      colorFilter = filterOf(color)

        shader =
            LinearGradient(0f, 0f, 0f, 100f, Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR)
    }

internal inline fun Canvas.transformCllRcc(crossinline initCllRcc: Canvas.() -> Unit) {
    if (false) {
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
    save()
    initCllRcc()
    restore()
}

internal fun rectFOfCllRcc(leftCllRcc: Int, topCllRcc: Int, rightCllRcc: Int, bottomCllRcc: Int) =
    RectF(
        leftCllRcc.toFloat(), topCllRcc.toFloat(), rightCllRcc.toFloat(), bottomCllRcc.toFloat()
    )

internal fun Int.withAlphaCllRcc(alphaCllRcc: Int): Int {

    require(alphaCllRcc in 0x00..0xFF)
    return this and 0x00FFFFFF or (alphaCllRcc shl 24)
}

internal fun Float.clampCllRcc(minCllRcc: Float, maxCllRcc: Float) =
    Math.min(maxCllRcc, Math.max(this, minCllRcc))

internal fun Bitmap.inCanvasCllRcc(): Canvas = Canvas(this)

internal fun Bitmap?.safeRecycleCllRcc() =
    if (this != null && !isRecycled) recycle() else Unit

internal fun Bitmap?.flushCllRcc() = this?.eraseColor(0)

internal fun Bitmap?.fitsCllRcc(neededWCllRcc: Int, neededHCllRcc: Int): Boolean =
    this?.let {

        it.height == neededHCllRcc && it.width == neededWCllRcc
    } ?: false
