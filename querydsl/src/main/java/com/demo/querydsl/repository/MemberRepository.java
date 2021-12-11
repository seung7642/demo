package com.demo.querydsl.repository;

import com.demo.querydsl.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface MemberRepository
        extends JpaRepository<Member, Long>,
        MemberRepositoryCustom,
        QuerydslPredicateExecutor<Member> // Spring-Data-JPA가 제공하는 Querydsl 기능을 사용합니다. (실무에선 사용을 지양합니다.)
{

    // select m from Member m where m.username = :username
    List<Member> findByUsername(String username);
}
