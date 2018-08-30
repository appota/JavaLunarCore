package com.appota.lunarcore;


import android.util.Log;

import java.text.Normalizer;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

public class LunarCoreHelper {
    private static final double PI = Math.PI;
    private static String TAG ="LunarCoreHelper";

    /**
     * In China, Vietnam and other East Asian countries,
     * we use the sexagenary cycle, also known as the Stems-and-Branches.
     * It appears as a means of recording days in the first Chinese written texts.
     * We have 10 Heavenly Stems and 12 Earthly Branches which make 60 Stem-Branch pairs.
     * From those pairs, we can "guess" which day is good, which day is not, based on some traditional rules.
     * For more info: https://en.wikipedia.org/wiki/Sexagenary_cycle
     *
     * Yin Yang name will be added later
     */

    // 10 Heavenly Stems
    // Vietnamese Heavenly Stems (or "Thiên Can")
    private static final String[] CAN = { "Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh",
            "Tân", "Nhâm", "Quý" };
    // Chinese Heavenly Stems (or "天干")
    private static final String[] STEMS = { "甲", "乙", "丙", "丁", "戊", "己", "庚",
            "辛", "壬", "癸" };

    // 12 Earthly Branches
    // Vietnamese Earthly Branches (or "Địa Chi")
    private static final String[] CHI = { "Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ",
            "Mùi", "Thân", "Dậu", "Tuất", "Hợi" };
    // Chinese Earthly Branches (or "地支")
    private static final String[] BRANCHES = { "子", "丑", "寅", "卯", "辰", "巳", "午",
            "未", "申", "酉", "戌", "亥" };

    /**
     * Based on some traditional rules (again!), on each month,
     * we have 4 Chi(s)/Branches which is called "good", and 4 Chi(s)/Branches which called "bad".
     * The next 2-dimensional arrays are the lists of "good days" and "bad days".
     * We Vietnamese actually have other terms for those two. But we haven't found any English word for that.
     * So let's just call them "good days" and "bad days".
     * Reference will be added later.
     *
     * We use Chi/Branch with unaccented syllables to make it easy when comparing string.
     */
    private static final String[][] goodDays = { new String[] { "tys", "suu", "tyj", "mui" },
            new String[] { "dan", "mao", "mui", "dau" },
            new String[] { "thin", "tyj", "dau", "hoi" },
            new String[] { "ngo", "mui", "suu", "dau" },
            new String[] { "than", "dau", "suu", "mao" },
            new String[] { "tuat", "hoi", "mao", "tyj" },
            new String[] { "tys", "suu", "tyj", "mui" },
            new String[] { "dan", "mao", "mui", "dau" },
            new String[] { "thin", "tyj", "dau", "hoi" },
            new String[] { "ngo", "mui", "suu", "dau" },
            new String[] { "than", "dau", "suu", "mao" },
            new String[] { "tuat", "hoi", "mao", "tyj" } };

    private static final String[][] badDays = { new String[] { "ngo", "mao", "hoi", "dau" },
            new String[] { "than", "tyj", "suu", "hoi" },
            new String[] { "tuat", "mui", "suu", "hoi" },
            new String[] { "tys", "dau", "tyj", "mao" },
            new String[] { "dan", "hoi", "mui", "tyj" },
            new String[] { "thin", "suu", "dau", "mui" },
            new String[] { "ngo", "mao", "hoi", "dau" },
            new String[] { "than", "tyj", "suu", "hoi" },
            new String[] { "tuat", "mui", "suu", "hoi" },
            new String[] { "tys", "dau", "tyj", "mao" },
            new String[] { "dan", "hoi", "mui", "tyj" },
            new String[] { "thin", "suu", "dau", "mui" } };



    private static boolean isGoodDay(String chiDay, int lunarMonth) {
        String[] data = goodDays[lunarMonth - 1];
        int tmp = data.length;
        for (int i = 0; i < tmp; i++) {
            if (data[i].equalsIgnoreCase(chiDay)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isBadDay(String chiDay, int lunarMonth) {
        if (isGoodDay(chiDay, lunarMonth))
            return false;
        String[] data = badDays[lunarMonth - 1];
        int tmp = data.length;
        for (String aData : data) {
            if (aData.equalsIgnoreCase(chiDay)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param solarDay
     * @param solarMonth
     * @param solarYear
     * @return days between March 1st 1996 and the input date. Why March 1st 1996, see the "processDayLunar" method below.
     */
    private static int getDateDurationBetweenInputAndPivotDate(int solarDay, int solarMonth, int solarYear) {
        Calendar currentCalendar = new GregorianCalendar();
        currentCalendar.set(solarYear, solarMonth - 1, solarDay, 0, 0, 0);

        Calendar checkCalendar = new GregorianCalendar();
        checkCalendar.set(1996, 2, 1, 0, 0, 0);
        return (int) ((currentCalendar.getTimeInMillis() / 1000L - checkCalendar
                .getTimeInMillis() / 1000L) / (60 * 60 * 24));
    }

    /**
     *
     * @param solarDay
     * @param solarMonth
     * @param solarYear
     * @return Can-Chi (or Stem-Branch) number of the input date.
     * We make March 1st 1996 a "pivot" date, and start counting Can-Chi from that date.
     * You can see we set iCan = 3, iChi = 9 means the "pivot" day is Đinh Dậu (or 丁酉, or Yin Fire Rooster).
     * So yeah, it's not a special date. Just a "pivot", a starting point to count. You can choose another day, which has another iCan and iChi.
     */
    private static int[] processDayLunar(int solarDay, int solarMonth, int solarYear) {
        int iCan = 3, iChi = 9;
        int numDays = getDateDurationBetweenInputAndPivotDate(solarDay, solarMonth, solarYear);
//        Log.d(TAG, "Test: " + iCan + "/" + iChi + "/" + numDays);
        if (numDays < 0) {
            iCan = (iCan + numDays % 10 + 10) % 10;
            iChi = (iChi + numDays % 12 + 12) % 12;
        } else if (numDays > 0) {
            iCan = (iCan + numDays % 10) % 10;
            iChi = (iChi + numDays % 12) % 12;
        }
//        Log.d(TAG, "Test2: " + iCan + "/" + iChi + "/" + numDays);
        return new int[] { iCan, iChi };
    }




    public static String getCanDayLunar(int solarDay, int solarMonth, int solarYear) {
        int[] tmp = processDayLunar(solarDay, solarMonth, solarYear);
        return CAN[tmp[0]];
    }

    public static String getChiDayLunar(int solarDay, int solarMonth, int solarYear) {
        int[] tmp = processDayLunar(solarDay, solarMonth, solarYear);
        return CHI[tmp[1]];
    }

    public static String getChineseCanDayLunar(int solarDay, int solarMonth, int solarYear) {
        int[] tmp = processDayLunar(solarDay, solarMonth, solarYear);
        return STEMS[tmp[0]];
    }

    public static String getChineseChiDayLunar(int solarDay, int solarMonth, int solarYear) {
        int[] tmp = processDayLunar(solarDay, solarMonth, solarYear);
        return BRANCHES[tmp[1]];
    }

    /**
     *
     * @param s
     * @return words unaccented syllables, with an exception of "Đ/đ"
     */
    private static String unAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
    }

    /**
     *
     * @param canchi
     * @return Can-Chi with unaccented syllables, with an exception of "Tý" and "Tỵ" which has the same result when unaccented.
     */
    private static String getUnAccentCanChi(String canchi){
        String rs = unAccent(canchi);
        if(canchi.equalsIgnoreCase("tý")){
            rs +="s";
        }else if(canchi.equalsIgnoreCase("tỵ")){
            rs +="j";
        }
        return rs.toLowerCase();
    }

    /**
     *
     * @param chiDay
     * @param lunarMonth
     * @return the string showing the input day is good or not
     * We use Can-Chi string with unaccented syllables to compare.
     * And yeah, "rateDay" is not a really good name for this method!
     */
    public static String rateDay(String chiDay, int lunarMonth) {
        chiDay = getUnAccentCanChi(chiDay);
        if (isGoodDay(chiDay, lunarMonth)) {
            return "Good";
        } else if (isBadDay(chiDay, lunarMonth)) {
            return "Bad";
        } else {
            return "Normal";
        }
    }




    /**
     *
     * @param dd
     * @param mm
     * @param yy
     * @return the number of days since 1 January 4713 BC (Julian calendar)
     */
    private static int jdFromDate(int dd, int mm, int yy) {
        int a = (14 - mm) / 12;
        int y = yy + 4800 - a;
        int m = mm + 12 * a - 3;
        int jd = dd + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400
                - 32045;
        if (jd < 2299161) {
            jd = dd + (153 * m + 2) / 5 + 365 * y + y / 4 - 32083;
        }
        // jd = jd - 1721425;
        return jd;
    }

    /**
     * http://www.tondering.dk/claus/calendar.html Section: Is there a formula
     * for calculating the Julian day number?
     *
     * @param jd
     *            - the number of days since 1 January 4713 BC (Julian calendar)
     * @return
     */
    private static int[] jdToDate(int jd) {
        int a, b, c;
        if (jd > 2299160) { // After 5/10/1582, Gregorian calendar
            a = jd + 32044;
            b = (4 * a + 3) / 146097;
            c = a - (b * 146097) / 4;
        } else {
            b = 0;
            c = jd + 32082;
        }
        int d = (4 * c + 3) / 1461;
        int e = c - (1461 * d) / 4;
        int m = (5 * e + 2) / 153;
        int day = e - (153 * m + 2) / 5 + 1;
        int month = m + 3 - 12 * (m / 10);
        int year = b * 100 + d - 4800 + m / 10;
        return new int[] { day, month, year };
    }

    /**
     * Solar longitude in degrees Algorithm from: Astronomical Algorithms, by
     * Jean Meeus, 1998
     *
     * @param jdn
     *            - number of days since noon UTC on 1 January 4713 BC
     * @return
     */
    private static double SunLongitude(double jdn) {
        // return CC2K.sunLongitude(jdn);
        return SunLongitudeAA98(jdn);
    }

    private static double SunLongitudeAA98(double jdn) {
        double T = (jdn - 2415021.0) / 36525; // Time in Julian centuries from
        // 2000-01-01 12:00:00 GMT
        double T2 = T * T;
        double dr = PI / 180; // degree to radian
        double M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048
                * T * T2; // mean anomaly, degree
        double L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2; // mean
        // longitude,
        // degree
        double DL = (1.914600 - 0.004817 * T - 0.000014 * T2)
                * Math.sin(dr * M);
        DL = DL + (0.019993 - 0.000101 * T) * Math.sin(dr * 2 * M) + 0.000290
                * Math.sin(dr * 3 * M);
        double L = L0 + DL; // true longitude, degree
        L = L - 360 * (INT(L / 360)); // Normalize to (0, 360)
        return L;
    }

    private static double NewMoon(int k) {
        // return CC2K.newMoonTime(k);
        return NewMoonAA98(k);
    }

    /**
     * Julian day number of the kth new moon after (or before) the New Moon of
     * 1900-01-01 13:51 GMT. Accuracy: 2 minutes Algorithm from: Astronomical
     * Algorithms, by Jean Meeus, 1998
     *
     * @param k
     * @return the Julian date number (number of days since noon UTC on 1
     *         January 4713 BC) of the New Moon
     */

    private static double NewMoonAA98(int k) {
        double T = k / 1236.85; // Time in Julian centuries from 1900 January
        // 0.5
        double T2 = T * T;
        double T3 = T2 * T;
        double dr = PI / 180;
        double Jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * T2
                - 0.000000155 * T3;
        Jd1 = Jd1 + 0.00033
                * Math.sin((166.56 + 132.87 * T - 0.009173 * T2) * dr); // Mean
        // new
        // moon
        double M = 359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347
                * T3; // Sun's mean anomaly
        double Mpr = 306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236
                * T3; // Moon's mean anomaly
        double F = 21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239
                * T3; // Moon's argument of latitude
        double C1 = (0.1734 - 0.000393 * T) * Math.sin(M * dr) + 0.0021
                * Math.sin(2 * dr * M);
        C1 = C1 - 0.4068 * Math.sin(Mpr * dr) + 0.0161 * Math.sin(dr * 2 * Mpr);
        C1 = C1 - 0.0004 * Math.sin(dr * 3 * Mpr);
        C1 = C1 + 0.0104 * Math.sin(dr * 2 * F) - 0.0051
                * Math.sin(dr * (M + Mpr));
        C1 = C1 - 0.0074 * Math.sin(dr * (M - Mpr)) + 0.0004
                * Math.sin(dr * (2 * F + M));
        C1 = C1 - 0.0004 * Math.sin(dr * (2 * F - M)) - 0.0006
                * Math.sin(dr * (2 * F + Mpr));
        C1 = C1 + 0.0010 * Math.sin(dr * (2 * F - Mpr)) + 0.0005
                * Math.sin(dr * (2 * Mpr + M));
        double deltat;
        if (T < -11) {
            deltat = 0.001 + 0.000839 * T + 0.0002261 * T2 - 0.00000845 * T3
                    - 0.000000081 * T * T3;
        } else {
            deltat = -0.000278 + 0.000265 * T + 0.000262 * T2;
        }
        ;
        return Jd1 + C1 - deltat;
    }

    private static int INT(double d) {
        return (int) Math.floor(d);
    }

    private static double getSunLongitude(int dayNumber, double timeZone) {
        return SunLongitude(dayNumber - 0.5 - timeZone / 24);
    }

    private static int getNewMoonDay(int k, double timeZone) {
        double jd = NewMoon(k);
        return INT(jd + 0.5 + timeZone / 24);
    }

    private static int getLunarMonth11(int yy, double timeZone) {
        double off = jdFromDate(31, 12, yy) - 2415021.076998695;
        int k = INT(off / 29.530588853);
        int nm = getNewMoonDay(k, timeZone);
        int sunLong = INT(getSunLongitude(nm, timeZone) / 30);
        if (sunLong >= 9) {
            nm = getNewMoonDay(k - 1, timeZone);
        }
        return nm;
    }

    private static int getLeapMonthOffset(int a11, double timeZone) {
        int k = INT(0.5 + (a11 - 2415021.076998695) / 29.530588853);
        int last=0; // Month 11 contains point of sun longutide 3*PI/2 (December
        // solstice)
        int i = 1; // We start with the month following lunar month 11
        int arc = INT(getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone) / 30);
//		int arc = INT(getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone) );

        do {
            last = arc;
            i++;
            arc = INT(getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone) / 30);
//			arc = INT(getSunLongitude(getNewMoonDay(k + i, timeZone), timeZone));

        } while (arc != last && i < 14);
        return i - 1;
    }

    /**
     *
     * @param solarDay
     * @param solarMonth
     * @param solarYear
     * @param timeZone
     * @return array of [lunarDay, lunarMonth, lunarYear, leapOrNot]
     */
    public static int[] convertSolar2Lunar(int solarDay, int solarMonth, int solarYear,
                                           double timeZone) {
        int lunarDay, lunarMonth, lunarYear, lunarLeap;
        int dayNumber = jdFromDate(solarDay, solarMonth, solarYear);
        int k = INT((dayNumber - 2415021.076998695) / 29.530588853);
        int monthStart = getNewMoonDay(k + 1, timeZone);
        if (monthStart > dayNumber) {
            monthStart = getNewMoonDay(k, timeZone);
        }
        int a11 = getLunarMonth11(solarYear, timeZone);
        int b11 = a11;
        if (a11 >= monthStart) {
            lunarYear = solarYear;
            a11 = getLunarMonth11(solarYear - 1, timeZone);
        } else {
            lunarYear = solarYear + 1;
            b11 = getLunarMonth11(solarYear + 1, timeZone);
        }
        lunarDay = dayNumber - monthStart + 1;
        int diff = INT((monthStart - a11) / 29);
        lunarLeap = 0;
        lunarMonth = diff + 11;
        if (b11 - a11 > 365) {
            int leapMonthDiff = getLeapMonthOffset(a11, timeZone);
//            Log.d(TAG,"xxxxxxxxxxxx- leapMonthDeff = "+leapMonthDiff);

            if (diff >= leapMonthDiff) {
                lunarMonth = diff + 10;
                if (diff == leapMonthDiff) {
                    lunarLeap = 1;
                }
            }
        }
        if (lunarMonth > 12) {
            lunarMonth = lunarMonth - 12;
        }
        if (lunarMonth >= 11 && diff < 4) {
            lunarYear -= 1;
        }
        return new int[] { lunarDay, lunarMonth, lunarYear, lunarLeap };
    }

    public static int[] convertLunar2Solar(int lunarDay, int lunarMonth,
                                           int lunarYear, int lunarLeap, double timeZone) {
        int a11, b11;
        if (lunarMonth < 11) {
            a11 = getLunarMonth11(lunarYear - 1, timeZone);
            b11 = getLunarMonth11(lunarYear, timeZone);
        } else {
            a11 = getLunarMonth11(lunarYear, timeZone);
            b11 = getLunarMonth11(lunarYear + 1, timeZone);
        }
        int k = INT(0.5 + (a11 - 2415021.076998695) / 29.530588853);
        int off = lunarMonth - 11;
        if (off < 0) {
            off += 12;
        }
        if (b11 - a11 > 365) {
            int leapOff = getLeapMonthOffset(a11, timeZone);
            int leapMonth = leapOff - 2;
//            Log.d(TAG,"xxxxxxxxxxxxxxxxxxxx-leapMonth ="+leapMonth);
            if (leapMonth < 0) {
                leapMonth += 12;
            }
            if (lunarLeap != 0 && lunarMonth != leapMonth) {
                Log.d(TAG,"Invalid input!");
                return new int[] { 0, 0, 0 };
            } else if (lunarLeap != 0 || off >= leapOff) {
                off += 1;
            }
        }
        int monthStart = getNewMoonDay(k + off, timeZone);
//        Log.d(TAG,"xxxxxxxxx - monthStart ="+monthStart);

        return jdToDate(monthStart + lunarDay - 1);
    }
}
