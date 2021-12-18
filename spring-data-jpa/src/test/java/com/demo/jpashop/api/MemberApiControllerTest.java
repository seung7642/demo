package com.demo.jpashop.api;

import com.demo.jpashop.domain.Member;
import com.demo.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static com.demo.jpashop.api.MemberApiController.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MemberApiControllerTest extends BaseControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void 멤버_저장하기() throws Exception {
        // given (이런게 주어졌을 때)
        CreateMemberRequest request = new CreateMemberRequest();
        request.setName("홍길동");

        // when (이렇게 하면)
        ResultActions resultActions = this.mockMvc.perform(post("/api/v2/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        // then (이렇게 된다.)
        List<Member> findMembers = memberRepository.findByName(request.getName());
        assertThat(findMembers)
                .extracting("name")
                .containsExactly("홍길동");
    }

    @Test
    public void 멤버_수정하기() throws Exception {
        // given (이런게 주어졌을 때)
        Member member = new Member();
        member.setName("홍길동");
        memberRepository.save(member);

        UpdateMemberRequest request = new UpdateMemberRequest();
        request.setName("김길동");

        // when (이렇게 하면)
        Long id = member.getId();
        ResultActions resultActions = this.mockMvc.perform(put("/api/v2/members/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(this.objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());

        // then (이렇게 된다.)
        Member findMember = memberRepository.findOne(id);
        assertThat(findMember.getName()).isEqualTo("김길동");
    }
}