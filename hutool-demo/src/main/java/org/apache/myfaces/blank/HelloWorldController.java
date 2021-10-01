package org.apache.myfaces.blank;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;

public class HelloWorldController {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String today = DateUtil.today();
        System.out.println(today);
        Date todayDate = DateUtil.parse(today, "yyyy-MM-dd");
        System.out.println("今天转换成Date类型：" + todayDate);

        String now = DateUtil.now();
        System.out.println(now);

        Date nowDate = DateUtil.parse(now, "yyyy-MM-dd HH:mm:ss");
        System.out.println("转换一下格式玩玩：" + nowDate);
        //一天的开始，结果：2017-03-01 00:00:00
        Date beginOfDay = DateUtil.beginOfDay(nowDate);
        //一天的结束，结果：2017-03-01 23:59:59
        Date endOfDay = DateUtil.endOfDay(nowDate);
        System.out.println(beginOfDay + "<--->" + endOfDay);

        System.out.println(beginOfDay.toString() + "<--->" + endOfDay.toString());

        String dateStr = "2017-03-01 22:33:23";
        Date date = DateUtil.parse(dateStr);
        Date nowDate1 = DateUtil.offsetDay(date, -1);
        Date beginOfDay1 = DateUtil.beginOfDay(nowDate1);
        Date endOfDay1 = DateUtil.endOfDay(nowDate1);

        System.out.println(nowDate1 + "->" + beginOfDay1 + "->" + endOfDay1);
        Date nowDate2 = DateUtil.offsetDay(date, -2);
        Date nowDate3 = DateUtil.offsetDay(date, -3);
        Date nowDate4 = DateUtil.offsetDay(date, -4);
        Date nowDate5 = DateUtil.offsetDay(date, -5);
        Date nowDate6 = DateUtil.offsetDay(date, -6);
        Date nowDate7 = DateUtil.offsetDay(date, -7);

        System.out.println(nowDate2);
        System.out.println(nowDate3);
        System.out.println(nowDate4);
        System.out.println(nowDate5);
        System.out.println(nowDate6);
        System.out.println(nowDate7);

        String eventDate = "2020-12-31";
        Date eDate = DateUtil.parse(eventDate);
        long betweenDay = DateUtil.between(eDate, DateUtil.parse(DateUtil.now(), "yyyy-MM-dd"), DateUnit.DAY);
        System.out.println("相差日期：" + betweenDay);
        for (int i = 0; i < betweenDay; i++) {
            Date currentDate = DateUtil.offsetDay(eDate, i);
            System.out.println("偏移日期：" + currentDate);
        }

        String urlDate = "2020-12-15 12:00:00";
        System.out.println(URLEncoder.encode(urlDate, "UTF-8"));
    }

}