package com.appota.lunarcoresample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.appota.lunarcore.LunarCoreHelper;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lunarCoreTest(2, 5, 1992, 1, 7.00);
        lunarCoreTest(29, 2, 1996, 1, 7.00);
        lunarCoreTest(1, 3, 1996, 1, 7.00);
        lunarCoreTest(2, 5, 1997, 1, 7.00);
        lunarCoreTest(2, 5, 2016, 1, 7.00);
    }

    private void lunarCoreTest(int day, int month, int year, int lunarLeapMonth, double timeZone){

        int[] lunarDay = LunarCoreHelper.convertSolar2Lunar(day, month, year, timeZone);
        Log.d(TAG, "Lunar date (dd/mm/yyyy): " + lunarDay[0] + "/" + lunarDay[1] + "/" + lunarDay[2] + "\n" +
                "Leap Month: " + (lunarDay[3]==1 ? "Yes" : "No") + "\n" +
                "Can Chi: " + LunarCoreHelper.getCanDayLunar(day, month, year) + " " + LunarCoreHelper.getChiDayLunar(day, month, year) + "\n" +
                "Chinese 干支: " + LunarCoreHelper.getChineseCanDayLunar(day, month, year) + LunarCoreHelper.getChineseChiDayLunar(day, month, year) + "\n" +
                "Good/Bad Day: " + LunarCoreHelper.rateDay(LunarCoreHelper.getChiDayLunar(day, month, year), lunarDay[1]));


        int[] solarDay = LunarCoreHelper.convertLunar2Solar(day, month, year, lunarLeapMonth, timeZone);
        Log.d(TAG, "Solar date (dd/mm/yyyy): " + solarDay[0] + "/" + solarDay[1] + "/" + solarDay[2] + "\n" +
                "Can Chi: " + LunarCoreHelper.getCanDayLunar(solarDay[0],solarDay[1],solarDay[2]) + " " + LunarCoreHelper.getChiDayLunar(solarDay[0],solarDay[1],solarDay[2]) + "\n" +
                "Chinese 干支: " + LunarCoreHelper.getChineseCanDayLunar(solarDay[0],solarDay[1],solarDay[2]) + LunarCoreHelper.getChineseChiDayLunar(solarDay[0],solarDay[1],solarDay[2]) + "\n" +
                "Good/Bad Day: " + LunarCoreHelper.rateDay(LunarCoreHelper.getChiDayLunar(solarDay[0],solarDay[1],solarDay[2]), month));
    }
}
