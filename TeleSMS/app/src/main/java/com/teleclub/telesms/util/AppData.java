package com.teleclub.telesms.util;

import com.teleclub.telesms.helper.DatabaseHelper;
import com.teleclub.telesms.model.Account;
import com.teleclub.telesms.model.CardInfo;
import com.teleclub.telesms.model.ContactData;

import java.util.ArrayList;

public class AppData {
    public static CardInfo cardInfo;
    public static int accessNumber = -1;
    public static String token = "";
    public static Account account;
    public static double balance = 0.0;
    public static boolean useDevIp = false;

    public static DatabaseHelper dbHelper = null;

    public static boolean called = false;
    public static int callCount = 0;
    public static boolean canTopup = false;

    public static ArrayList<ContactData> contacts = new ArrayList<>();
}
