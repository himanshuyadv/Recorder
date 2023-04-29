package com.ansh.recorder.ui

import android.app.ActivityManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.ansh.recorder.R
import com.ansh.recorder.core.utils.showToastCllRcc
import com.ansh.recorder.databinding.ActivityMainBinding
import com.ansh.recorder.ui.recording.RecordFragmentDirections
import com.ansh.recorder.ui.services.NotificationService
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var bindingCllRcc: ActivityMainBinding
    private val viewModelCllRcc: RecorderViewModel by viewModels()
    private var TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instCllRcc = this
        bindingCllRcc = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingCllRcc.root)
        val navControllerCllRcc = findNavController(R.id.nav_host_fragment_am)
        bindingCllRcc.bottomNavigationAm.setupWithNavController(navControllerCllRcc)


        val appBarConfigurationCllRcc = AppBarConfiguration(
            setOf(
                R.id.action_contacts,
                R.id.action_record,
                R.id.action_call_history,
                R.id.action_recording_history
            )
        )
        bindingCllRcc.bottomNavigationAm.setOnNavigationItemReselectedListener(
            BottomNavigationView.OnNavigationItemReselectedListener {

            })


        val telMgrCllRcc = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        val simStateCllRcc = telMgrCllRcc.simState
        if (simStateCllRcc == TelephonyManager.SIM_STATE_ABSENT || simStateCllRcc == TelephonyManager.SIM_STATE_UNKNOWN) {
            showToastCllRcc(getString(R.string.no_sim))
        }

        NavigationUI.setupWithNavController(
            bindingCllRcc.toolbarAm,
            findNavController(R.id.nav_host_fragment_am),
            appBarConfigurationCllRcc
        )


        bindingCllRcc.icThreeDots.setOnClickListener {


//            mInterstitialAd?.show(this@MainActivityCllRcc)
            //todo implement settings ?!
//            Toast.makeText(this, "BURGER CLICKED", Toast.LENGTH_SHORT).show()
            navControllerCllRcc.navigate(R.id.moreFragmentAN)
        }

        setupWakelockCllRcc()
        setWindowSettingsCllRcc()
        batteryOptimizationCllRcc()
        startServiceCllRcc()


        if (intent != null) {
            val callIntentCllRcc = intent
            if (callIntentCllRcc.getBooleanExtra(getString(R.string.calling), false)) {
                navControllerCllRcc.navigate(R.id.action_record)
                navControllerCllRcc.navigate(
                    RecordFragmentDirections.actionActionRecordToVerifyRecordFragment()
                )
            } else
                if (callIntentCllRcc.getBooleanExtra(getString(R.string.restart_cll_rcc), false)) {
                    navControllerCllRcc.navigate(R.id.verifyRecordFragment)
                }
        }

        NotificationService._finishRecordingEventCllRcc.observe(this) {
//            Toast.makeText(this, "LKJHG", Toast.LENGTH_SHORT).show()
//            finish()
//            startActivity(
//                Intent(
//                    this,
//                    MainActivity::class.java
//                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("restart", true))
//            recreate()
            //reload()

        }

    }


    private fun setupWakelockCllRcc() {
        val powerManagerCllRcc = getSystemService(POWER_SERVICE) as PowerManager
        val wakeLockCllRcc = powerManagerCllRcc.newWakeLock(
            PowerManager.PARTIAL_WAKE_LOCK,
            getString(R.string.call_rec_ta)
        )
        wakeLockCllRcc.acquire(10 * 60 * 1000L /*10 minutes*/)
    }

    private fun setWindowSettingsCllRcc() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            this.setTurnScreenOn(true)
        } else {
            val windowCllRcc = window
            windowCllRcc.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
    }

    private fun startServiceCllRcc() {

        if (!isMyServiceRunningCllRcc(NotificationService::class.java)) {
            val serviceIntentCllRcc = Intent(this, NotificationService::class.java)
            serviceIntentCllRcc.putExtra(
                getString(R.string.input_extra),
                getString(R.string.recording_Enabled)
            )
            ContextCompat.startForegroundService(this, serviceIntentCllRcc)
        }
    }

    private fun isMyServiceRunningCllRcc(serviceClassCllRcc: Class<*>): Boolean {

        val managerCllRcc = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        for (serviceCllRcc in managerCllRcc.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClassCllRcc.name == serviceCllRcc.service.className) {
                return true
            }
        }
        return false
    }


    private fun batteryOptimizationCllRcc() {

        val intentCllRcc = Intent()
        val packageNameCllRcc = packageName
        val pmCllRcc = getSystemService(POWER_SERVICE) as PowerManager
        if (!pmCllRcc.isIgnoringBatteryOptimizations(packageNameCllRcc)) {
            intentCllRcc.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intentCllRcc.data = Uri.parse("package:$packageNameCllRcc")
            startActivity(intentCllRcc)
        }
    }

    fun hideSearchIconCllRcc() {

        bindingCllRcc.icSearch.visibility = View.INVISIBLE
    }

    fun showSearchIconCllRcc(blockCllRcc: () -> Unit) {

        bindingCllRcc.icSearch.visibility = View.VISIBLE
        bindingCllRcc.icSearch.setOnClickListener {
            blockCllRcc()
        }
    }

    fun hideOptionsCllRcc() {

        bindingCllRcc.bottomNavigationAm.visibility = View.GONE
        bindingCllRcc.icThreeDots.visibility = View.GONE
    }

    fun showOptionsCllRcc() {
        bindingCllRcc.bottomNavigationAm.visibility = View.VISIBLE
        bindingCllRcc.icThreeDots.visibility = View.VISIBLE
    }

    companion object {
        var instCllRcc: MainActivity? = null
    }

    override fun onDestroy() {
        super.onDestroy()
        instCllRcc = null
    }


}

