package com.demo.jpashop.api;

import com.demo.jpashop.domain.Member;
import com.demo.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 템플릿 엔진으로 화면을 반환해주는 api와 json을 반환하는 Rest API는 반환 타입부터 성격이 살짝씩 다르기 때문에
 * 패키지를 구분하는게 좋습니다.
 */
@RequiredArgsConstructor
@RestController
public class MemberApiController {

    private final MemberService memberService;

    // ========== 저장 API 시작 ==========
    // 아래와 같이 Entity 클래스를 request로 받아버리면, 해당 API 스펙이 Entity에 의존하는 상황이 발생합니다.
    // Entity 클래스는 아래 API 외에도 여러 곳에서 사용될 여지가 있는데, API 스펙을 이렇게 Entity 클래스에 의존하게 만들어 버리면,
    // Entity 클래스에 약간의 변화만 와도 해당 API는 에러를 내뱉습니다. 따라서, 요청/응답 DTO 객체를 별도로 만들어주어야 합니다.
    // Entity 클래스를 요청 객체로 사용할 경우 또 안 좋은 점은, 개발자 입장에서 어떤 파라미터가 넘어올지 알 수 없다는 것입니다.
    // Entity 클래스 내 필드와 이름이 동일한 파라미터가 있다면 Spring이 알아서 매핑시켜주는데, 정확히 어떤 파라미터가 넘어오는지는 API 스펙 문서를 봐야합니다.
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    // 아래와 같이 요청/응답 DTO 객체를 사용할 경우, Entity 클래스를 변경해도 API 스펙이 바뀌지 않습니다.
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class CreateMemberResponse {
        private Long id;
    }
    // ========== 저장 API 끝  ==========

    // ========== 수정 API 시작 ==========
    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
    // ========== 수정 API 끝  ==========

    // ========== 조회 API 시작 ==========
    // 아래는 안 좋은 예입니다.
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    // 아래는 좋은 예입니다.
    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(x -> new MemberDto(x.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
    // ========== 조회 API 끝  ==========

}
