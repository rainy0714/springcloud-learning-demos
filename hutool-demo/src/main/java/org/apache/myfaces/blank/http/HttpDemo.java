package org.apache.myfaces.blank.http;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author tongzhenke
 * @Date 2021/1/19 17:20
 */
public class HttpDemo {

    public static void main(String[] args) {
//        String httpurl = "http://mp.weixin.qq.com/mp/homepage?__biz=MzU1ODg4MjE2NA==&hid=2&sn=82b6a2d8660ed023b1fa300a561ecc94&scene=18#wechat_redirect";
//        String resStr = HttpClientUtil.doGet(httpurl);
//        System.out.println(resStr);
//        System.out.println(HttpParseFileUtil.extractFilename(resStr));

        String url = "http://sg.digitalgd.com.cn/ebus/tif/sso/connect/page/oneoffcode2yrzuserinfo";
        String paasid = "yxq_gzyzt";
        String token = "VSe0ysApBwWQFk9dpiQs5f6ZBS8oTXoH";
        String nonce = "123456789abcdefg";

        Map<String, String> headersMap = new HashMap<String, String>();
        headersMap.put("x-tif-paasid", paasid);
        Long nowTime = DateUtil.current(false)/1000;
        headersMap.put("x-tif-timestamp", nowTime.toString());
        headersMap.put("x-tif-signature", SHA256Utils.getSHA256(nowTime + token + nonce + nowTime).toUpperCase());
        headersMap.put("x-tif-nonce", nonce);

        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setRyz_access_token("tif:yrz:userdata:1cqpkk6t6cj7e58f8a3358ee9486ebfb2f0648785aeae");
        String paramJson = JSONUtil.toJsonStr(tokenInfo);
        String resStr = HttpClientUtil.doPostWithHeadersAndJson(url, headersMap, paramJson);
        System.out.println(resStr);

    }

}
