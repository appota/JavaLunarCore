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

        lunarCoreTest();
    }

    private void lunarCoreTest(){
        int day = 1, month = 1, year = 2018;
        int lunarLeapMonth = 1;
        double timeZone = 7.00;

        int[] lunarDay = LunarCoreHelper.convertSolar2Lunar(day,month,year,timeZone);
        Log.d(TAG, "Lunar date:\n" +
                "Day: " + lunarDay[0] + "\n" +
                "Month: " + lunarDay[1] + "\n" +
                "Year: " + lunarDay[2] + "\n" +
                "Leap Month: " + (lunarDay[3]==1 ? "Yes" : "No"));


        int[] solarDay = LunarCoreHelper.convertLunar2Solar(day,month,year, lunarLeapMonth,timeZone);
        Log.d(TAG, "Solar date:\n" +
                "Day: " + solarDay[0] + "\n" +
                "Month: " + solarDay[1] + "\n" +
                "Year: " + solarDay[2] + "\n");
    }
}
