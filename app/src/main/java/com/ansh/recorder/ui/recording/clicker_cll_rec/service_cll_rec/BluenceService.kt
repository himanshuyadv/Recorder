package com.ansh.recorder.ui.recording.clicker_cll_rec.service_cll_rec

import android.accessibilityservice.AccessibilityService
import android.content.ComponentName
import android.content.Intent
import android.view.accessibility.AccessibilityEvent


class BluenceService : AccessibilityService() {

    override fun startForegroundService(service: Intent?): ComponentName? {
        return super.startForegroundService(service)
    }



    override fun onCreate() {
        super.onCreate()

        instanceCllRcc = this
    }

    override fun onDestroy() {
        super.onDestroy()

        instanceCllRcc = null
    }

    override fun onAccessibilityEvent(eventCllRcc: AccessibilityEvent?) {
        //Log.e("logs", "lpokijhugytfrdesza")
//        val source = event!!.source ?: return
////        Log.e("wnd", source.window.toString())
////        source.
////        val findAccessibilityNodeInfosByViewId =
////            source.findAccessibilityNodeInfosByViewId("com.samsung.android.incallui:id/incall_button_name")
//        for(i in 0 until source.childCount){
//            Log.e("wnd", source.getChild(i).toString())
//        }
//        if (findAccessibilityNodeInfosByViewId.size > 0) {
//            // You can also traverse the list if required data is deep in view hierarchy.
//            val requiredText = findAccessibilityNodeInfosByViewId[0].text.toString()
//            Log.i("Required Text", requiredText)
//        }

    }

    override fun onInterrupt() {
        instanceCllRcc = null
    }

    companion object {
        var instanceCllRcc: BluenceService? = null
            private set
    }

}