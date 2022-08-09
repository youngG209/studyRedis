package com.studyredis.repository;

import com.studyredis.Entity.Member;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface MemberRedisRepository extends CrudRepository<Member, Long> {

	Optional<Member> findByMemberId(String name);

}
