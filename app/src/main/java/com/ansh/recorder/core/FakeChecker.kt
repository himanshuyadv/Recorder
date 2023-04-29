package com.ansh.recorder.core

data class FakeChecker(
    var isAccessibilityRequestedCllRcc: Boolean,
    var isOverlayRequestedCllRcc: Boolean
) {


    companion object {
        fun createDisabledCllRcc(): FakeChecker = FakeChecker(
            isAccessibilityRequestedCllRcc = false,
            isOverlayRequestedCllRcc = false
        )
    }
}