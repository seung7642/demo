package com.demo.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    // Item 공통 속성
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;

    // Book 관련 속성
    private String author;
    private String isbn;
}
