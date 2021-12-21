package com.demo.jpashop.domain;

import com.demo.jpashop.domain.item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item")
@Entity
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; // 주문 가격(주문 당시, 주문 당시라는 말은 할인율이 적용될 수도 있다는 의미. 할인은 해당 예제에서 구현X)
    private int count; // 주문 수량(주문 당시)

    // ===== 생성 메서드 =====
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        item.removeStock(count);
        return orderItem;
    }

    // ===== 비즈니스 로직 =====
    public void cancel() {
        getItem().addStock(count);
    }

    // ===== 조회 로직 =====
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}