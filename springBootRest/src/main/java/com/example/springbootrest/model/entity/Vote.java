package com.example.springbootrest.model.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Vote {

    private String voteId;        // 투표 id
    private long articleId;       // 게시글 id
    private String userId;        // 사용자 id
    private String title;         // 투표 제목
    private String description;   // 투표 설명
    private String deadline;      // 마감시간
    private int totalCnt;         // 총 투표수

    private List<VoteItem> items; // 투표항목
}
