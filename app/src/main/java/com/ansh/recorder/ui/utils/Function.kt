package com.ansh.recorder.ui.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.view.accessibility.AccessibilityNodeInfo
import androidx.cardview.widget.CardView
import com.ansh.recorder.ui.recording.clicker_cll_rec.service_cll_rec.BluenceService

private val settingScreensCllRcc = arrayOf(
    "com.android.settings", "com.miui.securitycenter"
)

fun iterateCurrentScreenCllRcc(
    onFailedCllRcc: () -> Unit = {},
    delayCllRcc: Long = 1000,
    timeoutCllRcc: Long = 2000,
    ignoreScreenCllRcc: Boolean = false,
    onSuccessCllRcc: () -> Unit
) = postThreadCllRcc(delayCllRcc) {
    iterateConditionallyCllRcc(
        onCompleteCllRcc = onSuccessCllRcc,
        screenCllRcc = if (!ignoreScreenCllRcc) isSettingsOpenedCllRcc else true,
        timeoutCllRcc = timeoutCllRcc,
        onTimeOutCllRcc = onFailedCllRcc
    )

}


private val isSettingsOpenedCllRcc: Boolean
    get() {

        val currentWindowCllRcc = BluenceService.instanceCllRcc?.rootInActiveWindow
        return if (currentWindowCllRcc == null) {
            false
        } else {
            val packageNameCllRcc = currentWindowCllRcc.packageName.toString()
//            Log.d("open1", packageName)
            settingScreensCllRcc.any { it == packageNameCllRcc }
        }
    }

fun iterateConditionallyCllRcc(
    screenCllRcc: Boolean,
    timeoutCllRcc: Long,
    onTimeOutCllRcc: () -> Unit,
    onCompleteCllRcc: () -> Unit
) {

    iterateDelayedCllRcc(
        timeoutCllRcc, onTimeOutCllRcc
    ) {
        if (screenCllRcc) {
            onCompleteCllRcc()
            LoopStateCllRcc.BreakCllRcc
        } else LoopStateCllRcc.ContinueCllRcc
    }
}

val currentTimeCllRcc: Long
    get() = System.currentTimeMillis()

fun iterateDelayedCllRcc(
    timeoutCllRcc: Long, onTimeoutCllRcc: () -> Unit, onCompleteCllRcc: () -> LoopStateCllRcc
) {
    val mTimeCllRcc = currentTimeCllRcc

    iterateDelayedCllRcc {
        if (currentTimeCllRcc - mTimeCllRcc > timeoutCllRcc) {
            onTimeoutCllRcc()
            LoopStateCllRcc.BreakCllRcc
        } else onCompleteCllRcc()
    }
}

private const val ACTION_DELAYCllRcc = 240L
private fun iterateDelayedCllRcc(onCompleteCllRcc: () -> LoopStateCllRcc) {

    lateinit var next: () -> Unit
    next = {
        if (onCompleteCllRcc() == LoopStateCllRcc.ContinueCllRcc) postThreadCllRcc(
            ACTION_DELAYCllRcc,
            next
        )
    }
    postThreadCllRcc(ACTION_DELAYCllRcc, next)
}

enum class LoopStateCllRcc {
    ContinueCllRcc, BreakCllRcc
}

class NoNodeExceptionCllRcc : Exception()

fun searchAndClickCllRcc(
    contextCllRcc: Context,
    buttonCllRcc: Int,
    delayAfterClickCllRcc: Long = 250,
    onTimeOutCllRcc: () -> Unit,
    timeoutCllRcc: Long = 1500,
    onCompleteCllRcc: () -> Unit
) {
    iterateDelayedCllRcc(timeoutCllRcc, onTimeOutCllRcc) {

        try {
            val neededNodeCllRcc = findNodeCllRcc(contextCllRcc, buttonCllRcc)
            // Log.e("logs", neededNode.toString())
            postThreadCllRcc(delayAfterClickCllRcc) {
                neededNodeCllRcc.clickCllRcc()
                iterateCurrentScreenCllRcc(
                    onCompleteCllRcc, delayAfterClickCllRcc, onSuccessCllRcc = onCompleteCllRcc
                )
            }
            LoopStateCllRcc.BreakCllRcc
        } catch (eCllRcc: NoNodeExceptionCllRcc) {
//            Log.d(INNER_TAGCllRcc, "Node Not Found")
            LoopStateCllRcc.ContinueCllRcc
        } catch (eCllRcc: Exception) {
            eCllRcc.printStackTrace()
            LoopStateCllRcc.ContinueCllRcc
        }
    }
}

private fun AccessibilityNodeInfo.clickCllRcc() {

    performAction(AccessibilityNodeInfo.ACTION_CLICK)
    if (parent != null) parent.clickCllRcc()
}

//fun Any?.logCllRcc(message: Any?, isPrint: Boolean = true) =
//    if (isPrint) Log.d(this?.javaClass?.simpleName ?: "null", message.toString())
//    else 0

@Throws(NoNodeExceptionCllRcc::class)
private fun findNodeCllRcc(context: Context, buttonCllRcc: Int): AccessibilityNodeInfo {

    val windowCllRcc = BluenceService.instanceCllRcc!!.rootInActiveWindow
    return windowCllRcc.findNodeCllRcc(context, buttonCllRcc)
}

private fun AccessibilityNodeInfo.findNodeCllRcc(
    context: Context, buttonCllRcc: Int
): AccessibilityNodeInfo {

    val checkerCllRcc = ButtonCondition(context, this)
//    Log.e("logs", this.toString())
    if (checkerCllRcc(buttonCllRcc)) {
        return this
    }
    for (iCllRcc in 0 until childCount) try {
        return getChild(iCllRcc).findNodeCllRcc(context, buttonCllRcc)
    } catch (_: NoNodeExceptionCllRcc) {
    } catch (_: IllegalStateException) {
    }

    throw NoNodeExceptionCllRcc()
}

fun postThreadCllRcc(delayCllRcc: Long = 1000, actionCllRcc: () -> Unit) {
    com.ansh.recorder.ApplicationR.handlerCllRccCllRcc!!.postDelayed(actionCllRcc, delayCllRcc)
}

fun pulseAnimation(target: CardView): ObjectAnimator {
    val scaleDown: ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
        target,
        PropertyValuesHolder.ofFloat("scaleX", 1.05f),
        PropertyValuesHolder.ofFloat("scaleY", 1.05f)
    )
    scaleDown.duration = 640
    scaleDown.repeatCount = ObjectAnimator.INFINITE
    scaleDown.repeatMode = ObjectAnimator.REVERSE
    return scaleDown
}