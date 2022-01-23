package com.demo.datajpa.entity;

import com.demo.datajpa.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 테스트_entity() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Team team1 = new Team("재무팀"); em.persist(team1);
        Team team2 = new Team("회계팀"); em.persist(team2);

        Member member1 = new Member("홍길동", 10, team1); em.persist(member1);
        Member member2 = new Member("김길동", 20, team1); em.persist(member2);
        Member member3 = new Member("김철수", 30, team2); em.persist(member3);
        Member member4 = new Member("이철수", 40, team2); em.persist(member4);

        em.flush();
        em.clear();

        // when (시나리오_이렇게 하면)
        List<Member> members = em.createQuery("select m from Member m", Member.class)
                .getResultList();

        // then (기대결과_이렇게 된다.)
        assertThat(members.get(0).getUsername()).isEqualTo(member1.getUsername());
        assertThat(members.get(1).getUsername()).isEqualTo(member2.getUsername());
        assertThat(members.get(2).getUsername()).isEqualTo(member3.getUsername());
        assertThat(members.get(3).getUsername()).isEqualTo(member4.getUsername());

        assertThat(members)
                .extracting("username")
                .containsExactly("홍길동", "김길동", "김철수", "이철수");
    }

    @Test
    public void JpaEventBaseEntity() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member1 = new Member("member1");
        memberRepository.save(member1); // @PrePersist 가 호출됩니다.

        Thread.sleep(100);
        member1.setUsername("member2");

        em.flush(); // @PreUpdate
        em.clear();

        // when (시나리오_이렇게 하면)
        Member findMember = memberRepository.findById(member1.getId()).get();

        // then (기대결과_이렇게 된다.)
        System.out.println("findMember.getCreatedDate() = " + findMember.getCreatedDate());
        System.out.println("findMember.getLastModifiedDate() = " + findMember.getLastModifiedDate());
        System.out.println("findMember.getCreatedBy() = " + findMember.getCreatedBy());
        System.out.println("findMember.getLastModifiedBy() = " + findMember.getLastModifiedBy());
    }
}