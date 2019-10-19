package com.teleclub.rabbtel.util;

import android.content.Context;
import android.net.sip.SipManager;

public class SIPManager {

    public SipManager sipManager = null;


    public SipManager instanceSipManager(Context context){
        if (sipManager == null) {
            sipManager = SipManager.newInstance(context);
        }
        return sipManager;
    }



}
