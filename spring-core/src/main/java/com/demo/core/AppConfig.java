package com.demo.core;

import com.demo.core.discount.DiscountPolicy;
import com.demo.core.discount.FixDiscountPolicy;
import com.demo.core.discount.RateDiscountPolicy;
import com.demo.core.member.MemberRepository;
import com.demo.core.member.MemberService;
import com.demo.core.member.MemberServiceImpl;
import com.demo.core.member.MemoryMemberRepository;
import com.demo.core.order.OrderService;
import com.demo.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// @Configuration 애너테이션을 붙이면 바이트조작 기술인 CGLIB을 사용해서 싱글톤을 보장합니다.
@Configuration
public class AppConfig {

    // memberService 빈과 orderService 빈을 호출했다고 가정해보겠습니다.
    // memberService 빈을 호출하면 memberRepository 빈을 또 호출하고, memberRepository 빈은 new 키워드를 이용해 MemoryMemberRepository 객체를 생성합니다.
    // 그런데 orderService 빈을 호출하면 내부적으로 또 다시 memberRepository 빈을 호출하고 내부 로직을 보면 또 다시 new 키워드로 객체를 생성합니다.
    // 이런 상황에서 Spring 컨테이너는 어떻게 싱글톤을 보장하는 것일까요?
    @Bean
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        return new RateDiscountPolicy();
    }
}
