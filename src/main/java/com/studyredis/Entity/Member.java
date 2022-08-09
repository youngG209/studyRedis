package com.studyredis.Entity;

import javax.persistence.GeneratedValue;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.GeneratorType;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@ToString
@Getter
@RedisHash("member")
public class Member {

	@Id
	@GeneratedValue()
	private Long id;

	@Indexed
	private String memberId;

	private String name;

	private int age;

	public Member(String memberId, String name, int age) {
		this.memberId = memberId;
		this.name = name;
		this.age = age;
	}

	public void updateMember(Member change) {
		this.name = change.name;
		this.age = change.age;
	}
}
