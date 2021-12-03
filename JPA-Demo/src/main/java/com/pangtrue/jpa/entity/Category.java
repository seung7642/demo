package com.pangtrue.jpa.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
public class Category extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long categoryId;
    private String name;

    // 상위 카테고리를 나타냅니다. 자기 자신을 참조로 가지는 것도 가능합니다.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    // 실무에서 N:M 관계는 1:N 두 개로 풀어서 해결해야 합니다.
    // 아래의 @ManyToMany 는 공부를 위해 예시로 사용해본 것입니다.
    @ManyToMany
    @JoinTable(
            name = "Category_Item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();
}
