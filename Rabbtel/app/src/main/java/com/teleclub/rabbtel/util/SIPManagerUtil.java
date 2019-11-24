package com.teleclub.rabbtel.util;

import android.content.Context;
import android.net.sip.SipManager;

public class SIPManagerUtil {




    public static SipManager instanceSipManager(Context context){
        SipManager sipManager = null;
        sipManager = SipManager.newInstance(context);

        return sipManager;
    }



}
