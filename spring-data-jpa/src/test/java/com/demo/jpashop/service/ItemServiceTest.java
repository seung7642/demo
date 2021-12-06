package com.demo.jpashop.service;

import com.demo.jpashop.domain.item.Book;
import com.demo.jpashop.domain.item.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired ItemService itemService;
    @Autowired EntityManager em;

    @Test
    public void 아이템을_저장한다() throws Exception {
        // given (이런게 주어졌을 때)
        Item item = new Book();
        item.setPrice(30000);

        // when (이렇게 하면)
        itemService.saveItem(item);

        // then (이렇게 된다.)
        em.persist(item);
        assertEquals(item, itemService.findOne(item.getId()));
    }
}