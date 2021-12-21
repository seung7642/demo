package com.demo.jpashop.api;

import com.demo.jpashop.domain.Address;
import com.demo.jpashop.domain.Order;
import com.demo.jpashop.domain.OrderStatus;
import com.demo.jpashop.repository.OrderRepository;
import com.demo.jpashop.repository.OrderSearch;
import com.demo.jpashop.repository.OrderSimpleQueryDto;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 주문을 조회하는 API. 이때 해당 주문과 연관된 회원, 배달 정보도 같이 조회합니다.
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    // 아래와 같이 API를 작성할 경우, 이전에 회원 API에서 얘기했던 문제말고 또 다른 큰 문제가 발생합니다.
    // json으로 변환해주는 잭슨 라이브러리가 Order 클래스에는 Member가 있고, Member에는 다시 Order가 있다는걸 알지 못해 무한 루프에 빠지게됩니다.
    // Hibernate5Module 을 사용하면 아래 API가 정상적으로 값을 반환하긴 합니다.
    // Hibernate5Module 을 사용하면 Lazy 로딩 객체를 null로 세팅한 json 을 반환합니다.
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    // 아래와 같이 API를 수정하면 API가 Entity 클래스에 의존하는 문제는 해결했습니다.
    // 그런데, Lazy 로딩 쿼리를 모두 날려야해서 성능 상 문제가 있습니다. (Entity->DTO 변환과정에서 2개의 테이블을 건드립니다.)
    // JPA에선 N+1 문제라고 하며, 아래의 경우 Member와 Delivery 2개를 건드리기 때문에 1 + N + N 쿼리가 나가게 됩니다.
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    // 아래는 페치 조인을 활용해 위의 N+1 문제를 극복합니다. (실무에서 사용되는 방법)
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        return orders.stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderRepository.findOrderDtos();
    }

    @Data
    private class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // Lazy 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // Lazy 초기화
        }
    }
}
