package com.demo.jpashop.api;

import com.demo.jpashop.domain.Address;
import com.demo.jpashop.domain.Order;
import com.demo.jpashop.domain.OrderItem;
import com.demo.jpashop.domain.OrderStatus;
import com.demo.jpashop.repository.OrderRepository;
import com.demo.jpashop.repository.OrderSearch;
import com.demo.jpashop.repository.order.query.OrderQueryDto;
import com.demo.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RequiredArgsConstructor
@RestController
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;

    // 방법1: Entity 클래스 그대로 반환하기 (절대 사용하면 안되는 방법)
    @GetMapping("/api/v1/orders")
    private List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());

        // Lazy 로딩을 초기화해주기 위한 과정
        for (Order order : all) {
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(o -> o.getItem().getName());
        }
        return all;
    }

    // 방법2: DTO로 반환하기
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        return orderRepository.findAllByString(new OrderSearch()).stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    // 방법3: 일대다 에서 페치 조인을 사용하기. (일대다 관계에서 페치 조인을 사용하면 페이징을 사용할 수 없다는 치명적인 단점이 있습니다.)
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        return orderRepository.findAllWithItem().stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_paging(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(toList());

        return result;
    }

    // 방법4: 일대다 관계(컬렉션) 에서 DTO로 조회하기.
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
    }

    @Getter
    static class OrderDto {

        // API가 Entity를 반환하게 하지 말고, DTO로 감싸서 보내라는 말의 의미는, 완전히 Entity와의 종속을 끊어내라는 것을 의미합니다.
        // 따라서, DTO에 아래와 같이 Entity를 그대로 필드에 넣어주면 DTO로 감싸는 의미가 없습니다.
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
//        private List<OrderItem> orderItems; // DTO에 Entity 클래스를 직접적으로 사용하면 마찬가지로 의존이 생기기 때문에 절대 지양합니다.
        private List<OrderItemDto> orderItems;

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream()
                    .map(OrderItemDto::new)
                    .collect(toList());
        }
    }

    @Getter
    static class OrderItemDto {
        private String itemName; // 상품명
        private int orderPrice;  // 주문 가격
        private int count;       // 주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }

}
