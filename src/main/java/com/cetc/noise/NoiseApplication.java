package com.cetc.noise;

import com.cetc.noise.config.TokenUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.HashMap;

@SpringBootApplication
public class NoiseApplication {

	public static void main(String[] args) {

		SpringApplication.run(NoiseApplication.class, args);
		Date now = new Date();
		Date expiration = new Date(now.getTime() + 5*6000);
		System.out.println("*******************当前生成密钥为："+ TokenUtil.createToken("tuofengkeji","1qaz@WSX3edc",expiration));
	}

}
