package com.pccw.fuseconsumer;

import com.pccw.fuseconsumer.client.KafkaBridge;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FuseConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FuseConsumerApplication.class, args);
	}

}
