package com.example.springbootrest.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SelectRequestDto {

    private String userId;
    private long articleId;
    private String voteId;
    private long itemId;
}
