package com.zsasko.phonecallinterceptingsample.commons.extensions

import android.app.Activity
import android.app.role.RoleManager
import android.os.Build
import android.telecom.TelecomManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity


/**
 * Invokes displaying Phone handling activity. It's used for call listening on Android API >= Android Q.
 * @param requestId Request ID
 */
@RequiresApi(Build.VERSION_CODES.Q)
fun Activity.startCallScreeningPermissionScreen(requestId: Int) {
    val roleManager = this.getSystemService(AppCompatActivity.ROLE_SERVICE) as RoleManager
    val intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING)
    this.startActivityForResult(intent, requestId)
}

/**
 * Checks if application has Dialer capability i.e. can take or place the calls.
 */
fun Activity.hasDialerCapability(): Boolean {
    val telecomManager = getSystemService(AppCompatActivity.TELECOM_SERVICE) as TelecomManager
    return packageName.equals(telecomManager.defaultDialerPackage)
}
