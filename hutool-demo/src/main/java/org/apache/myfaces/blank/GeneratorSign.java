package org.apache.myfaces.blank;

import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

/**
 * @Author tongzhenke
 * @Date 2021/4/8 15:08
 */
public class GeneratorSign {

    public static void main(String[] args) {
        String clientSecret = "hiLifeTown";
        MessageDigestPasswordEncoder passwordEncoder = new MessageDigestPasswordEncoder("MD5");
        passwordEncoder.setEncodeHashAsBase64(true);
        String mySign = passwordEncoder.encodePassword(clientSecret + "123456" + "321", null);
        System.out.println(mySign);
    }



}
