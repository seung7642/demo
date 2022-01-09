package com.demo.core.order;

import com.demo.core.annotation.MainDiscountPolicy;
import com.demo.core.discount.DiscountPolicy;
import com.demo.core.discount.FixDiscountPolicy;
import com.demo.core.discount.RateDiscountPolicy;
import com.demo.core.member.Member;
import com.demo.core.member.MemberRepository;
import com.demo.core.member.MemoryMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;

    // 아래와 같이 new 키워드를 사용하게 되면 추상화뿐만 아니라 구현체에도 관심(의존)을 가지게 됩니다.
    // 이렇게 되면 OCP, DIP를 위반하게 됩니다. 따라서, 추상화에만 의존하도록 만들어야합니다.
    // 누군가 클라이언트인 OrderServiceImpl에 구현 객체를 주입해줘야합니다.
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
    private final DiscountPolicy discountPolicy;

    @Autowired
    public OrderServiceImpl(MemberRepository memberRepository,
                            @MainDiscountPolicy DiscountPolicy rateDiscountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = rateDiscountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }

    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
