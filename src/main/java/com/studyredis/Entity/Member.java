package com.studyredis.Entity;

import javax.persistence.GeneratedValue;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.GeneratorType;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@ToString
@Getter
@RedisHash
public class Member {

	@Id
	@GeneratedValue()
	private Long id;
	private String name;
	private int age;

	public Member(String name, int age) {
		this.name = name;
		this.age = age;
	}
}
