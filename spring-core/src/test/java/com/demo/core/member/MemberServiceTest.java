package com.demo.core.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class MemberServiceTest {

    MemberService memberService = new MemberServiceImpl();

    @Test
    public void 회원가입() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member = new Member(1L, "홍길동", Grade.VIP);

        // when (시나리오_이렇게 하면)
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        // then (기대결과_이렇게 된다.)
        assertThat(member).isEqualTo(findMember);
    }
}
