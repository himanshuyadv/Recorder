package com.ansh.recorder.ui.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteException
import android.support.v4.media.session.MediaSessionCompat
import com.ansh.recorder.ui.recording.receiver_cll_rec.MediaNotificationReceiver
import com.ansh.recorder.R

class RemoteControlsService : Service() {
    private var mSessionCllRcc: MediaSessionCompat? = null
    private var mMediaNotificationManagerCllRcc: MediaNotificationReceiver? = null

    override fun onCreate() {
        mSessionCllRcc = MediaSessionCompat(this, getString(R.string.music_service))
        mSessionCllRcc!!.setCallback(MediaSessionCallbackCllRcc())
        try {
            mMediaNotificationManagerCllRcc = MediaNotificationReceiver(this)
        } catch (eCllRcc: RemoteException) {
            throw IllegalStateException(getString(R.string.cant_create), eCllRcc)
        }
        mMediaNotificationManagerCllRcc?.startNotificationCllRcc()
    }

    override fun onDestroy() {
        mSessionCllRcc?.release()
        mMediaNotificationManagerCllRcc?.stopNotificationCllRcc()
    }

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    private class MediaSessionCallbackCllRcc : MediaSessionCompat.Callback()
}