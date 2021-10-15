package com.example.springbootrest.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class VoteItem {

    private long itemId;
    private String voteId;
    private String item;
    private int cnt;
    private List<VoteItemDetail> details;
}
