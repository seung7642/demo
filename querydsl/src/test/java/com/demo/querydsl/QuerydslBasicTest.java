package com.demo.querydsl;

import com.demo.querydsl.entity.Member;
import com.demo.querydsl.entity.QMember;
import com.demo.querydsl.entity.Team;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static com.demo.querydsl.entity.QMember.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
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
    public void startJPQL() throws Exception {
        // given (이런게 주어졌을 때)

        // when (이렇게 하면)
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        // then (이렇게 된다.)
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    public void startQuerydsl() throws Exception {
        // given (이런게 주어졌을 때)

        // when (이렇게 하면)
        // 아래의 member는 QMember.member 와 같은 static 필드입니다. import static을 이용해 이렇게 깔끔하게 사용하는게 좋습니다.
        // 위 static 필드를 따라가보면 new QMember("member1") 처럼 "member1" 으로 alias가 잡혀있습니다. 따라서, 해당 Querydsl이 변환되는 JPQL은
        // "from Member member1" 과 같은 형태가 됩니다. 그런데 만약, 같은 테이블을 JOIN해야할 경우라면 QMember m1 = new QMember("m1"); 처럼
        // alias를 수정해줄 수 있습니다.
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        // then (이렇게 된다.)
        assert findMember != null;
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }
}
