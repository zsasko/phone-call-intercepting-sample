package com.zsasko.phonecallinterceptingsample.commons.base

import androidx.appcompat.app.AppCompatActivity
import com.zsasko.phonecallinterceptingsample.commons.events.SingleLiveEvent
import com.zsasko.phonecallinterceptingsample.commons.events.UiEvent

abstract class BaseActivity : AppCompatActivity() {

    /**
     * Event that can be received in every activity that extends [BaseActivity]
     */
    val uiEvent = SingleLiveEvent<UiEvent>()

}