package com.demo.querydsl.dto;

import lombok.Data;

@Data
public class MemberSearchCondition {

    private String username;
    private String teamName;
    private Integer ageGoe; // 크거나 같다. (great or equal)
    private Integer ageLoe; // 작거나 같다. (low or equal)
}
