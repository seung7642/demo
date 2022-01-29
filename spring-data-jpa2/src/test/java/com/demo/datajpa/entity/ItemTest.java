package com.demo.datajpa.entity;

import com.demo.datajpa.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    void save() {
        Item item = new Item("A");
        itemRepository.save(item);
    }
}