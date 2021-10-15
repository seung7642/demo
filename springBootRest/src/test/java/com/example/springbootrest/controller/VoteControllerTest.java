package com.example.springbootrest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

// JUnit4는 @RunWith(SpringRunner.class)를 사용하지만,
// JUnit5는 @ExtendWith(SpringExtension.class)를 사용한다. (버전 업되면서 변경된 부분)
// 하지만 아래의 @WebMvcTest에 포함되어져있기 때문에 따로 적어줄 필요는 없다.
@WebMvcTest
public class VoteControllerTest  {

    @Autowired
    private MockMvc mvc;

    @Test
    public void 투표를_생성한다() throws Exception {
        mvc.perform(post("/rest/create")
                .header("x-user-id", "test"));
    }

    @Test
    public void 투표를_검색한다() {

    }

    @Test
    public void 투표한다() {

    }
}
