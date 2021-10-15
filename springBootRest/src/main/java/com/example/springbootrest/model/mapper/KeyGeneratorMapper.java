package com.example.springbootrest.model.mapper;

import com.example.springbootrest.model.entity.KeyGenerator;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KeyGeneratorMapper {

    List<KeyGenerator> getKeyIdxList();

    void updateKeyIdx(@Param("idx") int idx, @Param("cnt") int cnt);
}
