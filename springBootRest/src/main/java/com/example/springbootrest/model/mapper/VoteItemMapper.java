package com.example.springbootrest.model.mapper;

import com.example.springbootrest.model.dto.VoteItemDto;
import com.example.springbootrest.model.entity.VoteItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VoteItemMapper {

    void createItem(VoteItemDto dto);

    List<VoteItem> getVoteItem(String voteId);

    void countPlus(long itemId);
}
