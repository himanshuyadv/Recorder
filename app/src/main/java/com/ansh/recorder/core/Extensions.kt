package com.ansh.recorder.core

import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.ShareCompat
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.ansh.recorder.R
import com.ansh.recorder.core.data.model.RecordModel
import com.ansh.recorder.core.models.Call
import com.ansh.recorder.core.models.ContactData
import com.ansh.recorder.core.utils.DATE_FORMAT_CllRcc
import com.ansh.recorder.core.utils.PathUtil
import com.ansh.recorder.core.utils.showToastCllRcc
import com.ansh.recorder.core.utils.toUriCllRcc
import com.ansh.recorder.ui.MainActivity
import com.ansh.recorder.ui.recording.clicker_cll_rec.service_cll_rec.BluenceService
import java.io.File
import java.util.concurrent.TimeUnit


fun Context.checkPermissionsCllRcc(permissionCllRcc: String, callbackCllRcc: (Boolean) -> Unit) {

    Dexter.withContext(this)
        .withPermission(permissionCllRcc)
        .withListener(object : PermissionListener {
            override fun onPermissionGranted(p0CllRcc: PermissionGrantedResponse?) {

                callbackCllRcc(true)
            }

            override fun onPermissionDenied(p0CllRcc: PermissionDeniedResponse?) {

            }

            override fun onPermissionRationaleShouldBeShown(
                pCllRcc: PermissionRequest?,
                p1CllRcc: PermissionToken?
            ) {

            }

        })
        .check()

}

@BindingAdapter("setRecordDuration")
fun TextView.setRecordDurationCllRcc(recordModel: RecordModel) {

//    text = DateFormat.format(
//        "mm:ss", recordCllRcc.durationCllRcc
//    ).toString()

    text = String.format(
        "%02d : %02d ",
        TimeUnit.MILLISECONDS.toMinutes(recordModel.durationCllRcc),
        TimeUnit.MILLISECONDS.toSeconds(recordModel.durationCllRcc) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(recordModel.durationCllRcc))
    )
}

@BindingAdapter("setRecordDate")
fun TextView.setRecordDateCllRcc(recordModel: RecordModel) {


    val pathCllRcc =
        Environment.getExternalStorageDirectory().toString() + "/Call/" + recordModel.nameCllRcc
    text = File(pathCllRcc).getDateCllRcc(context)
}

fun File.getDateCllRcc(context: Context) = DateFormat.format(
    DATE_FORMAT_CllRcc, File(
        PathUtil.getPathCllRcc(
            context,
            this.toUri()
        )!!
    ).lastModified()
).toString()


@BindingAdapter("setRecordAvatar")
fun ShapeableImageView.setRecordAvatarCllRcc(recordModel: RecordModel) {


    val imageUriCllRcc = retrieveCallPhotoCllRcc(recordModel.numberCllRcc)
    val noImageUri = "content://com.android.contacts/phone_lookup/"
    Log.e("imageUri", imageUriCllRcc.toString())
    if (imageUriCllRcc.toString().isEmpty() || imageUriCllRcc.toString() == noImageUri) {
        Glide.with(this).load(R.drawable.sample_avatar).into(this)
    } else Glide.with(this).load(imageUriCllRcc).into(this)

}

@BindingAdapter("setContactAvatar")
fun ImageView.setContactAvatarCllRcc(contactCllRcc: ContactData) {

    Glide.with(this).load(contactCllRcc.avatarCllRcc).placeholder(R.drawable.sample_avatar)
        .into(this)
}

@BindingAdapter("setCallAvatar")
fun ImageView.setCallAvatarCllRcc(call: Call) {

    Glide.with(this).load(call.avatarCllRcc).placeholder(R.drawable.sample_avatar).into(this)
}

@TargetApi(Build.VERSION_CODES.M)
private fun Activity.openOverlayCllRcc() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (Build.MANUFACTURER.lowercase() == getString(R.string.xiaomi_cll_rcc)) {
            val intentCllRcc = Intent("miui.intent.action.APP_PERM_EDITOR")
            intentCllRcc.setClassName(
                getString(R.string.com_miuiu_sec_cll_rcc),
                "com.miui.permcenter.permissions.PermissionsEditorActivity"
            )
            intentCllRcc.putExtra("extra_pkgname", packageName)
            AlertDialog.Builder(this)
                .setTitle("Please Enable the additional permissions")
                .setMessage("You will not receive notifications while the app is in background if you disable these permissions")
                .setPositiveButton("Go to Settings",
                    DialogInterface.OnClickListener { dialogCllRcc, whichCllRcc ->
                        startActivity(
                            intentCllRcc
                        )
                    })
                .setIcon(android.R.drawable.ic_dialog_info)
                .setCancelable(false)
                .show()
        } else {
            val uriCllRcc = Uri.parse("package:$packageName")
            val intentCllRcc = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uriCllRcc)
            startActivityForResult(intentCllRcc, RC_OVERLAYCllRcc)
        }
    } else {
        val uriCllRcc = Uri.parse("package:$packageName")
        val intentCllRcc = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uriCllRcc)
        startActivityForResult(intentCllRcc, RC_OVERLAYCllRcc)

    }
}

fun Activity?.checkAccessibilityCllRcc(
    fakeChecker: FakeChecker,
    onCompletionCllRcc: () -> Unit
) {

    safeApplyCllRcc {
        if (accessibilityEnabledCllRcc) {
            onCompleteWholeActionCllRcc(this, fakeChecker, onCompletionCllRcc)
        } else
            if (!fakeChecker.isAccessibilityRequestedCllRcc) {
                fakeChecker.isAccessibilityRequestedCllRcc = true
                openAccessibilitySettingsCllRcc()
            } else onCompletionCllRcc()
    }
}

fun Activity?.checkOverlayCllRcc(
    fakeChecker: FakeChecker,
    onCompletionCllRcc: () -> Unit
) {

    safeApplyCllRcc {
        if (!overlayEnabledCllRcc) {
            openOverlayCllRcc()
        } else onCompletionCllRcc()
    }
}

enum class LoopStateCllRcc {
    ContinueCllRcc, BreakCllRcc
}

val currentTimeCllRcc: Long
    get() = System.currentTimeMillis()


fun iterateDelayedCllRcc(
    timeoutCllRcc: Long,
    onTimeoutCllRcc: () -> Unit,
    onCompleteCllRcc: () -> LoopStateCllRcc
) {


    val mTimeCllRcc = currentTimeCllRcc
    iterateDelayedCllRcc {
        if (currentTimeCllRcc - mTimeCllRcc > timeoutCllRcc) {
            onTimeoutCllRcc()
            LoopStateCllRcc.BreakCllRcc
        } else onCompleteCllRcc()
    }
}

const val RC_ACCESSIBILITYCllRcc = 228

private const val ACTION_DELAYCllRcc = 240L
private fun iterateDelayedCllRcc(onCompleteCllRcc: () -> LoopStateCllRcc) {
    lateinit var nextCllRcc: () -> Unit
    nextCllRcc = {
        if (onCompleteCllRcc() == LoopStateCllRcc.ContinueCllRcc)
            postThreadCllRcc(ACTION_DELAYCllRcc, nextCllRcc)
    }

    postThreadCllRcc(ACTION_DELAYCllRcc, nextCllRcc)
}

fun postThreadCllRcc(delayCllRcc: Long = 0, actionCllRcc: () -> Unit) {

    com.ansh.recorder.ApplicationR.handlerCllRccCllRcc!!.postDelayed(actionCllRcc, delayCllRcc)
}

private fun Activity.openAccessibilitySettingsCllRcc() {


    val intentCllRcc = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
    if (intentCllRcc.resolveActivity(packageManager) != null) {
        startActivityForResult(intentCllRcc, RC_ACCESSIBILITYCllRcc)
    }
}


val Context.overlayEnabledCllRcc: Boolean
    get() = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        true else Settings.canDrawOverlays(this)
const val RC_OVERLAYCllRcc = 322


private fun connectDeepServiceCllRcc(
    context: Context,
    onConnectedCllRcc: () -> Unit,
    onFailureCllRcc: () -> Unit
) = iterateDelayedCllRcc(1000, onFailureCllRcc) {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        startForegroundService(context, Intent(context, BluenceService::class.java))
//    }else{
//        context.startService(Intent(context, BluenceService::class.java))
//    }


    if (BluenceService.instanceCllRcc == null)
        LoopStateCllRcc.ContinueCllRcc
    else {
        onConnectedCllRcc()
        LoopStateCllRcc.BreakCllRcc
    }
}

private fun onCompleteWholeActionCllRcc(
    context: Context,
    fakeChecker: FakeChecker,
    onCompletionCllRcc: () -> Unit
) {

    connectDeepServiceCllRcc(context, {
        onCompletionCllRcc()
    }) {
        fakeChecker.isAccessibilityRequestedCllRcc = false
    }
}

fun Activity.shareToDriveCllRcc(filePathCllRcc: String) {

    val recordUriCllRcc =
        FileProvider.getUriForFile(this, "$packageName.fileprovider", File(filePathCllRcc))

    val shareIntentCllRcc = ShareCompat.IntentBuilder(this as MainActivity)
        .setText("Share")
        .setStream(recordUriCllRcc)
        .setType("audio/m4a")
        .intent
        .setPackage("com.google.android.apps.docs")

    try {
        startActivity(shareIntentCllRcc)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this,"Google Drive not Installed",Toast.LENGTH_SHORT).show()
        Log.e("",e.toString())

    }

}

fun Context.checkPermissionsForMemoryCllRcc(): Boolean {

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
        return if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            true
        } else {
            ActivityCompat.requestPermissions(
                (this as MainActivity),
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                1337
            )
            false
        }
    } else {
        return if (Environment.isExternalStorageManager()) {
            true
        } else {
            val intentCllRcc = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uriCllRcc = Uri.fromParts(
                "package",
                packageName, null
            )
            intentCllRcc.data = uriCllRcc
            startActivity(intentCllRcc)
            false
        }
    }
}

val Context.accessibilityEnabledCllRcc: Boolean
    get() {
        val expectNameCllRcc = ComponentName(this, BluenceService::class.java)
        val settingsCllRcc = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false
        val splitterCllRcc = TextUtils.SimpleStringSplitter(':')
        splitterCllRcc.setString(settingsCllRcc)
        while (splitterCllRcc.hasNext()) {
            val componentNameCllRcc = splitterCllRcc.next()
            val enabledCllRcc = ComponentName.unflattenFromString(componentNameCllRcc)
            if (enabledCllRcc != null && enabledCllRcc == expectNameCllRcc) return true
        }

        return false
    }

inline fun <T, K> T?.safeApplyCllRcc(blockCllRcc: T.() -> K): K? = this?.let(blockCllRcc)

fun listfCllRcc(dCllRcc: String, filesCllRcc: ArrayList<File>): ArrayList<File>? {
    val directoryCllRcc = File(dCllRcc)


    // get all the files from a directory
    if (null != directoryCllRcc.listFiles(AudioFilter())) {
        val fListCllRcc: Array<File> = directoryCllRcc.listFiles(AudioFilter())
        for (fileCllRcc in fListCllRcc) {
            if (fileCllRcc.isFile) {
                filesCllRcc.add(fileCllRcc)
            } else if (fileCllRcc.isDirectory) {
                listfCllRcc(fileCllRcc.absolutePath, filesCllRcc)
            }
        }
    }
    return filesCllRcc
}

var lastModifiedFileCllRcc: File? = null
var filesCllRcc: ArrayList<File>? = null
fun checkLastmodCllRcc(audiofilesCllRcc: ArrayList<File>): File? {

    var latmodCllRcc = Long.MIN_VALUE
    lastModifiedFileCllRcc = null
    for (fileCllRcc in audiofilesCllRcc) {
        if (fileCllRcc.lastModified() > latmodCllRcc) {
            lastModifiedFileCllRcc = fileCllRcc
            latmodCllRcc = fileCllRcc.lastModified()
        }
    }
    return lastModifiedFileCllRcc
}

fun Context.checkSimCllRcc(): Boolean {

//    val telMgr = getSystemService(AppCompatActivity.TELEPHONY_SERVICE) as TelephonyManager
//    val simState = telMgr.simState
//    if (simState == TelephonyManager.SIM_STATE_ABSENT || simState == TelephonyManager.SIM_STATE_UNKNOWN) {
//        showToast("You haven't Sim Card")
//        return false
//    }
//    return true

    if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
        return true
    }
    showToastCllRcc("You haven't Sim Card")
    return false
}

fun Context.shareCllRcc(uriCllRcc: Uri) {

    val shareIntentCllRcc = Intent(Intent.ACTION_SEND).apply {
        type = getString(R.string.audio_slash_cll_rcc)
        putExtra(Intent.EXTRA_STREAM, uriCllRcc)
    }
    val chooserCllRcc =
        Intent.createChooser(shareIntentCllRcc, getString(R.string.share_to_cll_rcc)).apply {
            flags = Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
    startActivity(chooserCllRcc)
}

fun Activity.deleteFileCllRcc(recordModel: RecordModel) {


    val pathCllRcc =
        Environment.getExternalStorageDirectory().toString() + "/Call/" + recordModel.nameCllRcc
    contentResolver.delete(
        File(pathCllRcc).toUriCllRcc(this),
        null,
        null
    )
}



