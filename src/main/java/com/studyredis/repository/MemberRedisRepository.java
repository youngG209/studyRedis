package com.studyredis.repository;

import com.studyredis.Entity.Member;
import org.springframework.data.repository.CrudRepository;

public interface MemberRedisRepository extends CrudRepository<Member, Long> {

}
