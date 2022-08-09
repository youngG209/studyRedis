package com.studyredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
@EnableCaching
public class StudyRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyRedisApplication.class, args);
	}

}
