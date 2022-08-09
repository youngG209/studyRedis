package com.studyredis.service;

import com.studyredis.member.Entity.Member;
import com.studyredis.member.repository.MemberRedisRepository;
import com.studyredis.member.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MemberSeviceTest {

	@Autowired
	private MemberRedisRepository memberRedisRepository;

	@Autowired
	private MemberService memberService;

	@Test
	void addMember() {
		Member member = new Member("lee", "young", 30);

		memberRedisRepository.save(member);
		System.out.println("key : " + member.getId());

		Iterable<Member> getMember = memberRedisRepository.findAll();

		getMember.forEach(member1 -> System.out.println(member1));
	}

	@Test
	void updateMember() {
		Member member = new Member("lee", "young2", 30);

		Member updateMember = memberService.updateMember(member);
		System.out.println("updateMember : " + updateMember);
	}

	@Test
	public void removeMember() {
		Member member = new Member("lee", "young2", 30);

		memberService.removeMember(member);

		Member findMember = memberService.findMember(member);
	}

	@Test
	void findMember() {
		Member member = new Member("lee", "young2", 30);
		Member findMember = memberService.findMember(member);

		Assertions.assertThat(findMember.getName()).isEqualTo(member.getName());
	}
}