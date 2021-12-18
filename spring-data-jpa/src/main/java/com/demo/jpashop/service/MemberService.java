package com.demo.jpashop.service;

import com.demo.jpashop.domain.Member;
import com.demo.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증(이름이 중복되면 안된다고 가정)
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원(이름)입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    // update 메서드에서 Member 타입을 반환해주면 안되나? 라는 의문을 가질 수도 있는데,
    // 그렇게 되면 커맨드와 쿼리를 모두 가지고 있는 형태가 됩니다. 무슨 말이냐면, update 메서드에서
    // 값을 수정도 하고, 값을 조회도 하는 형태가 됩니다. (역할을 짬뽕시키는 것보다, 철저히 분리하는게 좋습니다.)
    @Transactional
    public void update(Long id, String name) {
        Member findMember = memberRepository.findOne(id);
        findMember.setName(name);
    }
}
