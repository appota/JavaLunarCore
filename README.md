# JavaLunarCore 
[![JitPack release](https://img.shields.io/badge/JitPack-v1.0-green.svg)](https://github.com/appota/JavaLunarCore/releases/)
[![Language](https://img.shields.io/badge/language-java-orange.svg)](https://developer.android.com/guide/)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/appota/JavaLunarCore/blob/master/LICENSE)

JavaLunarCore is a library for Android to help you convert between the Gregorian Calendar (or the Western Solar Calendar) and the Lunar Calendar (or the Traditional Chinese/Vietnamese Calendar). The library does not contain any UI implemetation.

# Lịch Như Ý
This library is used in [Lịch
 Như Ý](https://play.google.com/store/apps/details?id=com.material.lichnhuy) by Appota.
 
  <p align="center"> 
    <img src="https://lh3.ggpht.com/OJqe8eU5repOIZZ-fVpT1pqPlYlaUi_mwvqNRWINKUZLyu1dvI2wEbzbFMuEqioEwPU=w1280-h665-rw" alt="Image of Lịch Như Ý">
 </p>
 

 
## Install
 
Add JitPack repository in your project's `build.gradle` at the end of repositories:

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add the dependency in your app's `build.gradle`
The `latestVersion` is  [![JitPack release](https://img.shields.io/badge/JitPack-v1.0-green.svg)](https://github.com/appota/JavaLunarCore/releases/)
```
dependencies {
  implementation 'com.github.appota:JavaLunarCore:${latestVersion}'
}
```

## Usage

```java
int[] lunarDay = LunarCoreHelper.convertSolar2Lunar(day, month, year, timeZone);
Log.d(TAG, "Lunar date:\n" +
            "Day: " + lunarDay[0] + "\n" +
            "Month: " + lunarDay[1] + "\n" +
            "Year: " + lunarDay[2] + "\n" +
            "Leap Month: " + (lunarDay[3]==1 ? "Yes" : "No"));


int[] solarDay = LunarCoreHelper.convertLunar2Solar(day, month, year, lunarLeapMonth, timeZone);
Log.d(TAG, "Solar date:\n" +
           "Day: " + solarDay[0] + "\n" +
           "Month: " + solarDay[1] + "\n" +
           "Year: " + solarDay[2] + "\n");
```
