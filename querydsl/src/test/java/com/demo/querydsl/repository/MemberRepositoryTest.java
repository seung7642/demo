package com.demo.querydsl.repository;

import com.demo.querydsl.dto.MemberSearchCondition;
import com.demo.querydsl.dto.MemberTeamDto;
import com.demo.querydsl.entity.Member;
import com.demo.querydsl.entity.QMember;
import com.demo.querydsl.entity.Team;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    public void before() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

    }

    @Test
    public void basicTest() {
        Member member = new Member("member", 25);
        memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember).isEqualTo(member);

        List<Member> result1 = memberRepository.findAll();
//        assertThat(result1).containsExactly(member);
        assertThat(result1).containsAnyOf(member);

        List<Member> result2 = memberRepository.findByUsername(member.getUsername());
//        assertThat(result2).containsExactly(member);
        assertThat(result2).containsAnyOf(member);
    }

    @Test
    public void search_WhereParam() throws Exception {
        // given (이런게 주어졌을 때)

        // when (이렇게 하면)
        MemberSearchCondition condition = new MemberSearchCondition();
        condition.setAgeGoe(35);
        condition.setAgeLoe(40);
        condition.setTeamName("teamB");
        List<MemberTeamDto> result = memberRepository.search(condition);

        // then (이렇게 된다.)
        assertThat(result)
                .extracting("username")
                .containsExactly("member4");
    }

    @Test
    public void search_paging() throws Exception {
        // given (이런게 주어졌을 때)

        // when (이렇게 하면)
        MemberSearchCondition condition = new MemberSearchCondition();
        PageRequest pageRequest = PageRequest.of(0, 3);

        Page<MemberTeamDto> result = memberRepository.searchPageSimple(condition, pageRequest);

        // then (이렇게 된다.)
        assertThat(result.getSize()).isEqualTo(3);
        assertThat(result.getContent())
                .extracting("username")
                .containsExactly("member1", "member2", "member3");
    }

    @Test
    public void querydslPredicateExecutorTest() {
        QMember member = QMember.member;
        Iterable<Member> result =
                memberRepository.findAll(member.age.between(10, 40).and(member.username.eq("member1")));

        for (Member findMember : result) {
            System.out.println("member1 = " + findMember);
        }
    }
}
