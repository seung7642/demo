package com.example.springbootrest.controller;

import com.example.springbootrest.exception.AlreadyRegisteredException;
import com.example.springbootrest.model.dto.CreateRequestDto;
import com.example.springbootrest.model.dto.RetrieveRequestDto;
import com.example.springbootrest.model.dto.RetrieveResponseDto;
import com.example.springbootrest.model.dto.SelectRequestDto;
import com.example.springbootrest.model.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RequiredArgsConstructor
@RestController
@RequestMapping("/rest")
public class VoteRestController {

    private static final Logger logger = LoggerFactory.getLogger(VoteRestController.class);
    private final VoteService voteService;

    @PostMapping("/create")
    public ResponseEntity<String> create(@RequestBody CreateRequestDto dto, @RequestHeader HttpHeaders header) {
        ResponseEntity<String> entity = null;
        try {
            if (header.containsKey("x-user-id")) {
                String userId = Objects.requireNonNull(header.get("x-user-id")).get(0);
                dto.setUserId(userId);
                String voteId = voteService.create(dto);
                entity = new ResponseEntity<>(voteId, HttpStatus.OK);
            }
        } catch (AlreadyRegisteredException e) {
            logger.debug(e.getMessage());
            entity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            entity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return entity;
    }

    @GetMapping("/retrieve")
    public ResponseEntity<RetrieveResponseDto> retrieve(@RequestParam long articleId, @RequestParam String voteId, @RequestHeader HttpHeaders header) {
        ResponseEntity<RetrieveResponseDto> entity = null;
        try {
            if (header.containsKey("x-user-id")) {
                String userId = Objects.requireNonNull(header.get("x-user-id").get(0));
                RetrieveRequestDto dto = new RetrieveRequestDto(userId, articleId, voteId);
                RetrieveResponseDto res = voteService.retrieve(dto);
                entity = new ResponseEntity<>(res, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            entity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return entity;
    }

    @PostMapping("/select")
    public ResponseEntity<Boolean> select(@RequestBody SelectRequestDto dto, @RequestHeader HttpHeaders header) {
        ResponseEntity<Boolean> entity = null;
        try {
            if (header.containsKey("x-user-id")) {
                String userId = Objects.requireNonNull(header.get("x-user-id").get(0));
                dto.setUserId(userId);
                boolean res = voteService.select(dto);
                entity = new ResponseEntity<>(res, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.debug(e.getMessage());
            entity = new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return entity;
    }
}
