package org.apache.myfaces.blank;

import cn.hutool.core.date.DateUtil;

/**
 * @Author tongzhenke
 * @Date 2021/3/11 11:10
 */
public class DateUtilDemo {

    public static void main(String[] args) {

        long nowtime = DateUtil.current(false)/1000;
        System.out.println(nowtime);
    }

}
