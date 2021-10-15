package com.example.springbootrest.model.mapper;

import com.example.springbootrest.model.dto.CreateRequestDto;
import com.example.springbootrest.model.dto.RetrieveRequestDto;
import com.example.springbootrest.model.entity.Vote;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface VoteMapper {

    Vote getVoteByArticleId(long articleId);

    // 특정 투표의 내용을 조회한다.
    Vote getVote(RetrieveRequestDto dto);

    // 새로운 투표를 생성한다.
    long createVote(CreateRequestDto dto);

    void countPlus(String voteId);
}
