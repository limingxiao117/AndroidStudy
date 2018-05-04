package com.eddy.base.event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by eddyli on 2016/8/27.
 */
public class EventMsgUtils {
    public static void postEventMsg(EventMsg eventMsg) {
        EventBus.getDefault().post(eventMsg);
    }
}
