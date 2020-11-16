package com.zsasko.phonecallinterceptingsample

import android.content.Intent
import android.os.Bundle
import com.zsasko.phonecallinterceptingsample.commons.base.BaseActivity
import com.zsasko.phonecallinterceptingsample.commons.events.MessageEvent
import com.zsasko.phonecallinterceptingsample.commons.events.PermissionDenied
import com.zsasko.phonecallinterceptingsample.commons.events.PhoneManifestPermissionsEnabled
import com.zsasko.phonecallinterceptingsample.commons.utils.CapabilitiesRequestorImpl
import com.zsasko.phonecallinterceptingsample.commons.utils.ManifestPermissionRequesterImpl
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import pub.devrel.easypermissions.AppSettingsDialog
import java.lang.ref.WeakReference

class MainActivity : BaseActivity() {

    private val manifestPermissionRequestor = ManifestPermissionRequesterImpl()

    private val capabilitiesRequestor = CapabilitiesRequestorImpl()

    // flag that restarts checking capabilities dialog, after user enables manifest permissions
    // via app settings page
    private var checkCapabilitiesOnResume = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listenUiEvents()
        //
        manifestPermissionRequestor.activity = WeakReference(this)
        capabilitiesRequestor.activityReference = WeakReference(this)
        //
        manifestPermissionRequestor.getPermissions()
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onResume() {
        super.onResume()
        if (checkCapabilitiesOnResume) {
            capabilitiesRequestor.invokeCapabilitiesRequest()
            checkCapabilitiesOnResume = false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        manifestPermissionRequestor.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        capabilitiesRequestor.onActivityResult(requestCode, resultCode, data)
    }

    private fun listenUiEvents() {
        uiEvent.observe(this, {
            when (it) {
                is PermissionDenied -> {
                    checkCapabilitiesOnResume = true
                    // This will display a dialog directing them to enable the permission in app settings.
                    AppSettingsDialog.Builder(this).build().show()
                }
                is PhoneManifestPermissionsEnabled -> {
                    // now we can load phone dialer capabilities requests
                    capabilitiesRequestor.invokeCapabilitiesRequest()
                }
                else -> {
                    // NOOP
                }
            }
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent) {
        val newText = String.format("%s\n%s", event.message, textLog.text.toString())
        textLog.setText(newText)
    }

}