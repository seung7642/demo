package com.pangtrue.jpa.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR", // 해당 generator 이름
        sequenceName = "MEMBER_SEQ",
        initialValue = 1, allocationSize = 50
)
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEMBER_SEQ_GENERATOR")
    @Column(name = "member_id")
    private Long memberId;
    private String name;
    @Embedded
    private Address address;

    // 특정 회원의 주문을 조회하기 위해 아래의 리스트를 보는게 아니라, Order 의 member 필드를 보는게 낫습니다.
    // 객체 그래프 탐색을 기준으로 생각하면 한도 끝도 없기 때문에 설계를 잘 해서 연결 고리를 어디에서 끊을건지 결정하는게 중요합니다.
    // 여하튼 따라서, 아래의 Member 에서 주문 리스트를 가지고 있는건 설계가 잘못된 것입니다. (별로 안좋음)
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    // 연관관계 편의 메서드. (연관관계 두 곳 중 한 곳에만 작성합니다.)
    public void addOrder(Order order) {
        this.orders.add(order);
        order.setMember(this);
    }
}
