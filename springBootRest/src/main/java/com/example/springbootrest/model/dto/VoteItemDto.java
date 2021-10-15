package com.example.springbootrest.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class VoteItemDto {

    private String voteId;
    private String item;
    private int cnt;

    public VoteItemDto(String voteId, String item, int cnt) {
        this.voteId = voteId;
        this.item = item;
        this.cnt = cnt;
    }
}
