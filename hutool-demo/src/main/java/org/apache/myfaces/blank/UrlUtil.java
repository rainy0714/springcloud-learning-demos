package org.apache.myfaces.blank;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @Author tongzhenke
 * @Date 2020/12/30 14:50
 */
public class UrlUtil {

    public static void main(String[] args) throws UnsupportedEncodingException {
        String url = "http://d.firim.top/qx5h?utm_source=fir&utm_medium=qr&release_id=5fec11b423389f0c39b27199";
        System.out.println(URLEncoder.encode(url, "UTF-8"));
    }
}
