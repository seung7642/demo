package com.demo.jpashop.service;

import com.demo.jpashop.domain.Member;
import com.demo.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // jUnit과 Spring을 엮어줍니다.
@SpringBootTest // SpringBoot가 제공하는 @Autowired 와 같은 기능을 사용하기 위해 적어줍니다.
@Transactional // Test에 @Transactional이 있으면 커밋이 아닌 롤백을 합니다.
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
//    @Rollback(value = false)
    public void 회원가입() throws Exception {
        // given (이런게 주어졌을 때)
        Member member = new Member();
        member.setName("홍길동");

        // when (이렇게 하면)
        Long savedId = memberService.join(member);

        // then (이렇게 된다.)
        em.flush();
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("홍길동");
        Member member2 = new Member();
        member2.setName("홍길동");

        // when
        memberService.join(member1);
        memberService.join(member2); // 여기서 예외가 발생해야 합니다!

        // then
        fail("예외가 발생해야 합니다.");
    }
}