package com.ansh.recorder

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.SharedPreferences
import android.os.*
import com.ansh.recorder.core.data.database.AppDatabase
import com.ansh.recorder.core.data.database.repositories_cll_rec.SettingsRepository
import com.ansh.recorder.core.data.model.Settings
import com.ansh.recorder.core.utils.ifNullCllRcc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


open class ApplicationR : Application() {


    @SuppressLint("BatteryLife")
    override fun onCreate() {
        super.onCreate()


        appPackageCllRcc = packageName
        handlerCllRccCllRcc = Handler(Looper.getMainLooper())

        prepareDatabaseCllRcc()
        createNotificationChannelCllRcc()


//        PowerManager. .isIgnoringBatteryOptimizations(packageName)

    }


    private fun prepareDatabaseCllRcc() {
        AppDatabase.initCllRcc(applicationContext)
        CoroutineScope(Dispatchers.IO).launch {
            SettingsRepository.getCurrentSettingsCllRcc().ifNullCllRcc {
                AppDatabase.instanceCllRcc.settingsDaoCllRcc().saveCllRcc(Settings())
            }
        }
    }

    private fun createNotificationChannelCllRcc() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannelCllRcc = NotificationChannel(
                com.ansh.recorder.ApplicationR.Companion.CHANNEL_IDCllRcc,
                "Example Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val managerCllRcc = getSystemService(
                NotificationManager::class.java
            )
            managerCllRcc.createNotificationChannel(serviceChannelCllRcc)
        }
    }


    companion object {
        var subscriptionStatus: String? = null
        const val CHANNEL_IDCllRcc = "exampleServiceChannel"
        var handlerCllRccCllRcc: Handler? = null
            private set
        private lateinit var appPackageCllRcc: String
        val CONTENT_PROVIDER_AUTHCllRcc: String
            get() = "$appPackageCllRcc.fileprovider"

        lateinit var sharedPreferencesCR: SharedPreferences
    }
}