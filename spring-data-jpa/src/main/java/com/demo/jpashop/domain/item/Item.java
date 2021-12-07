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

    // setter를 사용하는게 아니라, 아래처럼 변경 메서드를 Entity쪽에 만들어두면, 해당 메서드를 사용하는 곳만 찾으면
    // 어디서 변경이 일어나는지 바로 추적할 수 있습니다. (setter를 남발해 값을 변경하면 어디서 값 변경이 이뤄지는지 추적이 힘듭니다.)
    public void changeCommonField(String name, int price, int stockQuantity) {
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }
}