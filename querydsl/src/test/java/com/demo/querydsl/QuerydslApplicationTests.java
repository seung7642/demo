package com.demo.querydsl;

import com.demo.querydsl.entity.Hello;
import com.demo.querydsl.entity.QHello;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class QuerydslApplicationTests {

    @Autowired EntityManager em;

    @Test
    void contextLoads() {
        Hello hello = new Hello();
        em.persist(hello);

        JPAQueryFactory query = new JPAQueryFactory(em);
//        QHello qHello = new QHello("h");
        QHello qHello = QHello.hello; // 위와 같이 new로 생성할 필요없이 static으로 이미 만들어져 있습니다.
        Hello result = query
                .selectFrom(qHello)
                .fetchOne();

        assertThat(result).isEqualTo(hello); // querydsl 테스트
        assertThat(result.getId()).isEqualTo(hello.getId()); // lombok 테스트
    }

}
