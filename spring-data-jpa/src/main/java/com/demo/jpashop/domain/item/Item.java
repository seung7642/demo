package com.demo.jpashop.domain.item;

import com.demo.jpashop.domain.Category;
import com.demo.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Table(name = "item")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    // 비즈니스 로직
    // DDD 관점에서 Entity 내에서 해결할 수 있는 비즈니스 로직이라면, Entity 내에 넣어주는 게 응집도 측면에서 좋습니다.
    // 즉, 데이터(여기서는 stockQuantity 필드)를 가지고 있는 쪽에 비즈니스 로직을 넣어주는게 응집도가 높아집니다.
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}