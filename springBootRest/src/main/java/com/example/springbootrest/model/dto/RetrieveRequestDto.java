package com.example.springbootrest.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RetrieveRequestDto {

    private String userId;
    private long articleId;
    private String voteId;

    public RetrieveRequestDto(String userId, long articleId, String voteId) {
        this.userId = userId;
        this.articleId = articleId;
        this.voteId = voteId;
    }
}
