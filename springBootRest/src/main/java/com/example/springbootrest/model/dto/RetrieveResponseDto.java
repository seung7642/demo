package com.example.springbootrest.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RetrieveResponseDto {

    private String userId;
    private String title;
    private String description;
    private List<VoteItemDto> items;
    private String deadline;
    private boolean vote;

    public RetrieveResponseDto(String userId, String title,
                               String description, String deadline) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
    }
}
