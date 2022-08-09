package com.studyredis.service;

import com.studyredis.Entity.Member;
import com.studyredis.repository.MemberRedisRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemberService {

	private final MemberRedisRepository memberRedisRepository;

	public Member addMember(Member member) {

		isMember(member);

		return memberRedisRepository.save(member);
	}

	public Member updateMember(Member member) {
		System.out.println(member);
		Member findMember = isNotMember(member);
		findMember.updateMember(member);
		return findMember;
	}

	public void removeMember(Member member) {
		Member findMember = isNotMember(member);
		memberRedisRepository.deleteById(findMember.getId());
	}

	public Member findMember(Member member) {
			return isNotMember(member);
		}

	private void isMember(Member member) {
		Optional<Member> isMember = memberRedisRepository.findByMemberId(member.getMemberId());
		if (isMember.isPresent()) {
			throw new RuntimeException("등록된 멤버입니다.");
		}
	}

	private Member isNotMember(Member member) {
		Member findMember = memberRedisRepository.findByMemberId(member.getMemberId())
			.orElseThrow(() -> new RuntimeException("등록되지 않은 멤버입니다."));
		System.out.println("findMember : " + findMember);

		return findMember;
	}
}
