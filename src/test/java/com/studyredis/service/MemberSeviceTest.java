package com.studyredis.service;

import com.studyredis.Entity.Member;
import com.studyredis.repository.MemberRedisRepository;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberSeviceTest {

	@Autowired
	private MemberRedisRepository memberRedisRepository;

	@Test
	void addMember() {
		Member member = new Member("lee", 30);

		memberRedisRepository.save(member);
		System.out.println("key : " + member.getId());

		Iterable<Member> getMember = memberRedisRepository.findAll();

		getMember.forEach(member1 -> System.out.println(member1));
	}
}