package com.zsasko.phonecallinterceptingsample.commons.events

/**
 * Class that is a base class for all the events in the app.
 *
 * @author Zoran Sasko
 * @version 1.0.0
 */
sealed class UiEvent()

data class PermissionDenied(val requestCode: Int, val perms: MutableList<String>) : UiEvent()
data class PermissionGranted(val requestCode: Int, val perms: MutableList<String>) : UiEvent()
object PhoneManifestPermissionsEnabled : UiEvent()
object PhoneCapabilityEnabled : UiEvent()
