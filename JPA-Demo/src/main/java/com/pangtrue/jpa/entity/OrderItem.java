package com.pangtrue.jpa.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter @Setter
@Entity
@SequenceGenerator(
        name = "ORDERITEM_SEQ_GENERATOR",
        sequenceName = "ORDERITEM_SEQ",
        initialValue = 1, allocationSize = 50
)
public class OrderItem extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORDERITEM_SEQ_GENERATOR")
    @Column(name = "order_item_id")
    private Long orderItemId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer orderPrice;
    private Integer count;
}
