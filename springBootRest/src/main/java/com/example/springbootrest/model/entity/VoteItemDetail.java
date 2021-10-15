package com.example.springbootrest.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public class VoteItemDetail {

    private long itemId;
    private String userId;

    public VoteItemDetail(long itemId, String userId) {
        this.itemId = itemId;
        this.userId = userId;
    }
}
