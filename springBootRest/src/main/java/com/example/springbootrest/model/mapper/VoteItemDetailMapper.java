package com.example.springbootrest.model.mapper;

import com.example.springbootrest.model.dto.SelectRequestDto;
import com.example.springbootrest.model.entity.VoteItemDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface VoteItemDetailMapper {

    int insert(SelectRequestDto dto);

    List<VoteItemDetail> getVoteItemDetail(long itemId);
}
