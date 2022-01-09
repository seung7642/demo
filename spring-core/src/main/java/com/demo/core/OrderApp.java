package com.demo.core;

import com.demo.core.member.Grade;
import com.demo.core.member.Member;
import com.demo.core.member.MemberService;
import com.demo.core.member.MemberServiceImpl;
import com.demo.core.order.Order;
import com.demo.core.order.OrderService;
import com.demo.core.order.OrderServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class OrderApp {

    public static void main(String[] args) {
//        AppConfig appConfig = new AppConfig();
//        MemberService memberService = appConfig.memberService();
//        OrderService orderService = appConfig.orderService();

        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService = ctx.getBean("memberService", MemberService.class);
        OrderService orderService = ctx.getBean("orderService", OrderService.class);

        Long memberId = 1L;
        Member member = new Member(memberId, "홍길동", Grade.VIP);
        memberService.join(member);

        Order order = orderService.createOrder(memberId, "itemA", 10000);
        System.out.println("order = " + order);
    }
}
