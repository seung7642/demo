package com.demo.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * 연습을 위해 @Setter도 넣었지만, 실무에서는 @Setter를 넣으면 안돼고, @Getter만 써야합니다.
 * 값을 수정할 일이 있으면, Entity 내에 수정을 위한 의미있는 '행위'를 담은 이름으로 메서드를 만들어줘야합니다.
 * Setter를 남발하면 어디에서 값을 수정하는지 추적하기가 힘듭니다.
 */
@Getter @Setter
@Entity
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    // 아래와 같은 @Valid용 애너테이션은 Entity 클래스에 넣으면 안되고,
    // 별도의 요청/응답 DTO 객체를 만들어 그 안에 넣어줘야합니다.
//    @NotEmpty
    private String name;

    @Embedded
    private Address address;

    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
