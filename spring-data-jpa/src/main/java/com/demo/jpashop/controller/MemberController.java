package com.demo.jpashop.controller;

import com.demo.jpashop.domain.Address;
import com.demo.jpashop.domain.Member;
import com.demo.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    // @Valid를 사용한 파라미터로 넘어온 값(여기선 MemberForm)이 유효하지 않을 때, 컨트롤러 로직이 정상적으로 실행되지 않고, 바로 예외를 던집니다.
    // 하지만 Spring에서 제공하는 BindingResult를 사용하면, 에러 정보를 해당 변수에 담은 후 컨트롤러 로직을 그대로 실행할 수 있습니다.
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        // 만약 BindingResult에 에러가 담겨 아래 로직이 실행된다면, 뷰에 BindingResult 객체를 같이 넘겨줍니다.(thymeleaf-spring 모듈 덕분)
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/"; // 리다이렉트로 이동하면 화면 새로고침이 발생하지 않습니다.
    }

    // 간단한 예제라 Entity를 response로 반환했는데, 실무에선 DTO를 만들어 사용하는게 권장됩니다.
    // API를 개발할 때는 절대 Entity를 request, response 객체로 사용해선 안됩니다.
    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
