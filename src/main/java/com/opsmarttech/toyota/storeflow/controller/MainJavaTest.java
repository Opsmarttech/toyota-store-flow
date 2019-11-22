package com.opsmarttech.toyota.storeflow.controller;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainJavaTest {

    public static void main(String args[]) {
/*        //请注意月份是从0-11,天数是1， 2013-1-1 至 2013-12-31
        Calendar start = Calendar.getInstance();
        start.set(2019, 4, 17); //2013-1-1 开始
        Calendar end = Calendar.getInstance();
        end.set(2019, 10, 17); // 2014--0-0结束，2014-1-1不算

        int sumSunday = 0;
        int sumSat = 0;
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

        while(start.compareTo(end) <= 0) {
            int w = start.get(Calendar.DAY_OF_WEEK);
            if(w == Calendar.SUNDAY)
                sumSunday ++;
            if(w == Calendar.SATURDAY)
                sumSunday ++;
            //打印每天
            System.out.println(format.format(start.getTime()));
            //循环，每次天数加1
            start.set(Calendar.DATE, start.get(Calendar.DATE) + 1);
        }
        System.out.println("星期天总数为:" + sumSunday);
        System.out.println("星期六总数为:" + sumSunday);*/

        String str = "20190517";
        System.out.println(str.substring(6));

    }

}
