package com.example.springbootrest.model.service;

import com.example.springbootrest.model.dto.*;
import com.example.springbootrest.model.entity.KeyGenerator;
import com.example.springbootrest.model.entity.Vote;
import com.example.springbootrest.model.entity.VoteItem;
import com.example.springbootrest.model.entity.VoteItemDetail;
import com.example.springbootrest.model.mapper.KeyGeneratorMapper;
import com.example.springbootrest.model.mapper.VoteItemDetailMapper;
import com.example.springbootrest.model.mapper.VoteItemMapper;
import com.example.springbootrest.model.mapper.VoteMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.AlreadyBoundException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class VoteServiceImpl implements VoteService {

    private static final Logger logger = LoggerFactory.getLogger(VoteServiceImpl.class);
    private static int[] arr = new int[10];
    private static char[] ch = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '!', '@', '#', '$', '%', '^', '&', '*', '-', '=',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
    }; // size: 72

    private final VoteMapper voteMapper;
    private final VoteItemMapper itemMapper;
    private final VoteItemDetailMapper detailMapper;
    private final KeyGeneratorMapper keyMapper;

    @Override
    public RetrieveResponseDto retrieve(RetrieveRequestDto dto) {
        Vote vote = voteMapper.getVote(dto);
        RetrieveResponseDto retrieveResponseDto = convertVoteToResponseDto(vote);
        return retrieveResponseDto;
    }

    private RetrieveResponseDto convertVoteToResponseDto(Vote vote) {
        RetrieveResponseDto dto = new RetrieveResponseDto(vote.getUserId(),
                vote.getTitle(), vote.getDescription(), vote.getDeadline());
        List<VoteItemDto> list = new ArrayList<>();
        for (VoteItem item : vote.getItems()) {
            if (item.getDetails() != null) {
                for (VoteItemDetail detail : item.getDetails()) {
                    if (vote.getUserId().equals(detail.getUserId())) {
                        dto.setVote(true);
                    }
                }
            }
            list.add(new VoteItemDto(item.getVoteId(), item.getItem(), item.getCnt()));
        }
        dto.setItems(list);
        return dto;
    }

    @Transactional
    @Override
    public String create(CreateRequestDto dto) throws Exception {
        // 1. 하나의 게시글에는 하나의 투표만 등록될 수 있다. 이를 체크한다.
        Vote vote = voteMapper.getVoteByArticleId(dto.getArticleId());
        if (vote != null) {
            throw new AlreadyBoundException();
        }

        // 1. 마감시간 설정
        if ("".equals(dto.getDeadline())) {
            LocalDateTime ldt = LocalDateTime.now();
            ldt = ldt.plusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            dto.setDeadline(ldt.format(formatter));
        }

        // 2. vote_id 생성
        String voteId = createVoteId();
        dto.setVoteId(voteId);

        voteMapper.createVote(dto);
        for (String item : dto.getItems()) {
            VoteItemDto itemDto = new VoteItemDto();
            itemDto.setVoteId(voteId);
            itemDto.setItem(item);
            itemMapper.createItem(itemDto);
        }
        return voteId;
    }

    private String createVoteId() {
        List<KeyGenerator> keyList = keyMapper.getKeyIdxList();
        for (KeyGenerator key : keyList) {
            arr[key.getIdx()] = key.getCnt();
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 9; i >= 0; i--) {
            sb.append(ch[arr[i]]);
        }
        setCount();
        return sb.toString();
    }

    private void setCount() {
        int i = 0;
        while (true) {
            arr[i]++;
            if (arr[i] >= 72 && i < 10) {
                arr[i++] = 0;
            } else {
                break;
            }
        }
        for (i = 0; i < 10; i++) {
            keyMapper.updateKeyIdx(i, arr[i]);
        }
    }

    @Transactional
    @Override
    public boolean select(SelectRequestDto dto) {
        List<VoteItem> items = itemMapper.getVoteItem(dto.getVoteId());
        // 지금 투표하려는 유저가 해당 투표에 투표한 적이 있으면 false를 반환하는데,
        // 이게 아니라, 기존에 선택했던 아이템을 삭제하고 새로 추가하는게 맞다.
        // 선택한 투표 항목을 변경할 수 있어야한다.
        for (VoteItem item : items) {
            for (VoteItemDetail detail : item.getDetails()) {
                if (detail.getUserId().equals(dto.getUserId())) {
                    // TODO: 이미 투표한 항목이 있다면 삭제하기
                    return false;
                }
            }
        }

        // TODO: 마감시간이 다됬는지 이 타이밍에 검사해서 마감시간이 됬으면 DB에 값 수정하기
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = LocalDateTime.of(2021, 10, 15, 15, 30, 0);
        Duration duration = Duration.between(start, end);

        int res = detailMapper.insert(dto);
        itemMapper.countPlus(dto.getItemId());
        voteMapper.countPlus(dto.getVoteId());
        return res == 1;
    }
}
