package com.demo.core.member;

/**
 * 구현체가 하나만 있을 경우 관례상 인터페이스명 뒤에 'Impl' 을 붙입니다.
 */
public class MemberServiceImpl implements MemberService {

    // 아래의 코드는 OCP, DIP를 위반하고 있습니다. 기능을 확장한 구현체를 새로 갈아끼울려면 클라이언트인 아래 코드도 변경해줘야하기 때문입니다.
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
