package com.demo.core.discount;

import com.demo.core.member.Grade;
import com.demo.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {

    RateDiscountPolicy discountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP는 10% 할인이 적용되어야 한다.") // JUnit5 부터 테스트 이름을 붙일 수 있는 @DisplayName 이 추가되었습니다.
    public void vip_o() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member = new Member(1L, "memberVIP", Grade.VIP);

        // when (시나리오_이렇게 하면)
        int discount = discountPolicy.discount(member, 10000);

        // then (기대결과_이렇게 된다.)
        assertThat(discount).isEqualTo(1000);
    }

    @Test
    @DisplayName("VIP가 아니면 할인이 적용되지 않아야 한다.")
    public void vip_x() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member = new Member(2L, "memberVIP", Grade.BASIC);

        // when (시나리오_이렇게 하면)
        int discount = discountPolicy.discount(member, 10000);

        // then (기대결과_이렇게 된다.)
        assertThat(discount).isEqualTo(0);
    }
}