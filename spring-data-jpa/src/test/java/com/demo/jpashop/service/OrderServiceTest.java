package com.demo.jpashop.service;

import com.demo.jpashop.domain.Address;
import com.demo.jpashop.domain.Member;
import com.demo.jpashop.domain.Order;
import com.demo.jpashop.domain.OrderStatus;
import com.demo.jpashop.domain.item.Book;
import com.demo.jpashop.domain.item.Item;
import com.demo.jpashop.exception.NotEnoughStockException;
import com.demo.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

// DB와 연동하는 통합 테스트 (좋은 테스트는 DB 의존없이 테스트하는 것입니다.)
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        // given (이런게 주어졌을 때)
        Member member = createMember();
        Item book = createBook("JPA 프로그래밍", 10000, 10);

        int orderCount = 2;

        // when (이렇게 하면)
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        // then (이렇게 된다.)
        Order findOrder = orderRepository.findOne(orderId);
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, findOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 합니다.", 1, findOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량입니다.", 10000 * orderCount, findOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 합니다.", 8, book.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        // given (이런게 주어졌을 때)
        Member member = createMember();
        Item item = createBook("JPA 프로그래밍", 10000, 10);

        int orderCount = 11;

        // when (이렇게 하면)
        orderService.order(member.getId(), item.getId(), orderCount);

        // then (이렇게 된다.)
        fail("재고 수량 부족 예외가 발생해야 합니다.");
    }

    @Test
    public void 주문취소() throws Exception {
        // given (이런게 주어졌을 때)
        Member member = createMember();
        Item item = createBook("JPA 프로그래밍", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when (이렇게 하면)
        orderService.cancelOrder(orderId);

        // then (이렇게 된다.)
        Order findOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL 입니다.", OrderStatus.CANCEL, findOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 합니다.", 10, item.getStockQuantity());
    }

    private Item createBook(String name, int price, int stockQuantity) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("홍길동");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }
}