package com.zsasko.phonecallinterceptingsample.services

import android.telecom.Call
import android.telecom.CallScreeningService
import com.zsasko.phonecallinterceptingsample.commons.FORBIDDEN_PHONE_CALL_NUMBER
import com.zsasko.phonecallinterceptingsample.commons.events.MessageEvent
import com.zsasko.phonecallinterceptingsample.commons.extensions.parseCountryCode
import com.zsasko.phonecallinterceptingsample.commons.extensions.removeTelPrefix
import com.zsasko.phonecallinterceptingsample.commons.utils.NotificationManagerImpl
import org.greenrobot.eventbus.EventBus

class MyCallScreeningService : CallScreeningService() {

    private val notificationManager = NotificationManagerImpl()

    override fun onScreenCall(callDetails: Call.Details) {
        val phoneNumber = getPhoneNumber(callDetails)
        var response = CallResponse.Builder()
        response = handlePhoneCall(response, phoneNumber)

        respondToCall(callDetails, response.build())
    }

    private fun handlePhoneCall(
        response: CallResponse.Builder,
        phoneNumber: String
    ): CallResponse.Builder {
        if (phoneNumber == FORBIDDEN_PHONE_CALL_NUMBER) {
            response.apply {
                setRejectCall(true)
                setDisallowCall(true)
                setSkipCallLog(false)
                //
                displayToast(String.format("Rejected call from %s", phoneNumber))
            }
        } else {
            displayToast(String.format("Incoming call from %s", phoneNumber))
        }
        return response
    }

    private fun getPhoneNumber(callDetails: Call.Details): String {
        return callDetails.handle.toString().removeTelPrefix().parseCountryCode()
    }

    private fun displayToast(message: String) {
        notificationManager.showToastNotification(applicationContext, message)
        EventBus.getDefault().post(MessageEvent(message))
    }

}