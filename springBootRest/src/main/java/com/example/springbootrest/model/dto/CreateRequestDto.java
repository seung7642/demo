package com.example.springbootrest.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class CreateRequestDto {

    private String voteId;
    private String userId;
    private long articleId;
    private String title;
    private String description;
    private List<String> items;
    private String deadline;
}
