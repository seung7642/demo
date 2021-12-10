package com.demo.querydsl.repository;

import com.demo.querydsl.dto.MemberSearchCondition;
import com.demo.querydsl.dto.MemberTeamDto;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberTeamDto> search(MemberSearchCondition condition);
}
