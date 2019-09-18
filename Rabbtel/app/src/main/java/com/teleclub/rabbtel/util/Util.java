package com.teleclub.rabbtel.util;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Util {
    public static String getRealPhoneNumber(String number) {
        return number.replaceAll("[^\\d]", "");
    }

    public static String getDecimalString(double f, int decimal) {
        String s;
        double floatValue = f - (int)f;
        if (floatValue > 0) {

            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < decimal; i ++) {
                builder.append("#");
            }

            String pattern = "#." + builder.toString();
            DecimalFormat decimalFormat = new DecimalFormat(pattern);
            s = decimalFormat.format(f);
        } else {
            s = String.valueOf((int)f);
        }

        return s;
    }

    public static String getUniqueIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }

            String imei = telephonyManager.getDeviceId();
            if (imei != null && !imei.isEmpty()) {
                return imei;
            } else {
                return android.os.Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getSimCardNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            String simNumber = telephonyManager.getLine1Number();
            if (simNumber == null)
                return "";
            else
                return simNumber;
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }

        return "";
    }

    public static String getSqlDateString(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy#H:mm");
        return df.format(date);
    }

    public static boolean isBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) {}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }
}
