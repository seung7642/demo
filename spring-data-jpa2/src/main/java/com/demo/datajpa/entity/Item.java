package com.demo.datajpa.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Item implements Persistable<String> {

    // 어떠한 이유로 인해 Long 타입이 아닌, UUID와 같은 String 타입을 PK로 설정해야한다고 가정해보겠습니다.
    // String 타입이기 때문에 @GeneratedValue 또한 없는데요,
    // 이때 리포지토리의 .save() 를 호출하면 어떤 일이 일어날까요?
    // Spring Data JPA가 만들어주는 JpaRepository 의 구현체는 .save() 시에 isNew() 를 호출해
    // 해당 엔티티가 DB에 있는 엔티티인지, 새로운 엔티티인지를 판별합니다.
    // 이때, id값이 null인지 여부로 판별하는데요, 내가 PK 값을 설정해서 넣어줘야 하는 경우라면 이 판별이 오작동하게 됩니다.
    // 따라서, 아래와 같이 isNew() 메서드를 재정의해줘야 합니다.
    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    public Item(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return createdDate == null;
    }
}
