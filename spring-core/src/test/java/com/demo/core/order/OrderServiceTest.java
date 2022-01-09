package com.demo.core.order;

import com.demo.core.AppConfig;
import com.demo.core.member.Grade;
import com.demo.core.member.Member;
import com.demo.core.member.MemberService;
import com.demo.core.member.MemberServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class OrderServiceTest {

    MemberService memberService;
    OrderService orderService;

    @BeforeEach
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
        orderService = appConfig.orderService();
    }

    @Test
    public void createOrder() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Long memberId = 1L;
        Member member = new Member(memberId, "홍길동", Grade.VIP);
        memberService.join(member);

        // when (시나리오_이렇게 하면)
        Order order = orderService.createOrder(memberId, "itemA", 10000);

        // then (기대결과_이렇게 된다.)
        assertThat(order.getDiscountPrice()).isEqualTo(1000);
    }
}
