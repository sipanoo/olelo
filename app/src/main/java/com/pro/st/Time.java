package com.pro.st;

import android.provider.ContactsContract;

import java.util.Calendar;

public class Time {

public static String getTime(){

    String time = Calendar.getInstance().getTime().toString();
return time;
}



}
