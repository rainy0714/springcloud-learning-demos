package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Oauth2SatokenClient1Application {

	public static void main(String[] args) {
		SpringApplication.run(Oauth2SatokenClient1Application.class, args);
		System.out.println("\nSa-Token-OAuth Client1端启动成功\n\n" + str);
	}

	static String str = "-------------------- Sa-Token-OAuth2 示例 --------------------\n\n" +
			"首先在host文件 (C:\\windows\\system32\\drivers\\etc\\hosts) 添加以下内容: \r\n" +
			"	127.0.0.1 sa-oauth-server.com \r\n" +
			"	127.0.0.1 sa-oauth-client.com \r\n" +
			"	127.0.0.1 sa-oauth-client1.com \r\n" +
			"再从浏览器访问：\r\n" +
			"	http://sa-oauth-client1.com:8003";

}
