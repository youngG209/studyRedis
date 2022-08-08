package com.studyredis.service;

import com.studyredis.Entity.Member;
import com.studyredis.repository.MemberRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberSevice {

	private final MemberRedisRepository memberRedisRepository;

	public void addMember() {
		Member member = new Member("lee", 30);

		memberRedisRepository.save(member);
	}
}
