package com.example.springbootrest.model.service;

import com.example.springbootrest.model.dto.CreateRequestDto;
import com.example.springbootrest.model.dto.RetrieveRequestDto;
import com.example.springbootrest.model.dto.RetrieveResponseDto;
import com.example.springbootrest.model.dto.SelectRequestDto;

import java.rmi.AlreadyBoundException;

public interface VoteService {

    // 특정 투표의 내용을 조회한다.
    RetrieveResponseDto retrieve(RetrieveRequestDto dto);

    // 새로운 투표를 생성한다.
    String create(CreateRequestDto dto) throws AlreadyBoundException, Exception;

    // 투표한다.
    boolean select(SelectRequestDto dto);
}
