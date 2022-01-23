package com.demo.datajpa.repository;

import com.demo.datajpa.dto.MemberDto;
import com.demo.datajpa.entity.Member;
import com.demo.datajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    EntityManager em;

    @Test
    public void 테스트_멤버() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member = new Member("홍길동");
        Member savedMember = memberRepository.save(member);

        // when (시나리오_이렇게 하면)
        Optional<Member> byId = memberRepository.findById(savedMember.getId()); // .findById()의 결과가 있을 수도 있고, 없을 수도 있으니 Optional로 반환합니다.
        Member findMember = byId.orElseGet(() -> new Member(""));

        // then (기대결과_이렇게 된다.)
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);
    }

    @Test
    public void basicCRUD() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member1 = new Member("홍길동");
        Member member2 = new Member("김길동");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when (시나리오_이렇게 하면)
        Member findMember1 = memberRepository.findById(member1.getId())
                .orElseGet(() -> new Member(""));
        Member findMember2 = memberRepository.findById(member2.getId())
                .orElseGet(() -> new Member(""));

        List<Member> all = memberRepository.findAll();

        long count = memberRepository.count();

        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long countAfterDelete = memberRepository.count();

        // then (기대결과_이렇게 된다.)
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        assertThat(all.size()).isEqualTo(2);

        assertThat(count).isEqualTo(2);

        assertThat(countAfterDelete).isEqualTo(0);
    }

    @Test
    public void findByUsernameAndAgeGreaterThenTest() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member1 = new Member("홍길동", 10);
        Member member2 = new Member("홍길동", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when (시나리오_이렇게 하면)
        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("홍길동", 15);

        // then (기대결과_이렇게 된다.)
        assertThat(result.get(0)).isEqualTo(member2);
    }

    @Test
    public void NamedQuery_테스트() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member1 = new Member("홍길동", 10);
        Member member2 = new Member("이길동", 20);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // when (시나리오_이렇게 하면)
        List<Member> result = memberRepository.findByUsername("이길동");

        // then (기대결과_이렇게 된다.)
        assertThat(result.get(0)).isEqualTo(member2);
    }

    @Test
    public void query() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member1 = new Member("홍길동", 10); memberRepository.save(member1);
        Member member2 = new Member("이길동", 20); memberRepository.save(member2);

        // when (시나리오_이렇게 하면)
        List<Member> find = memberRepository.findUser("이길동", 20);

        // then (기대결과_이렇게 된다.)
        assertThat(find.get(0).getUsername()).isEqualTo(member2.getUsername());
    }

    @Test
    public void dto로_조회하기() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Team teamA = new Team("teamA");
        teamRepository.save(teamA);

        Member m1 = new Member("홍길동", 10);
        m1.changeTeam(teamA);
        memberRepository.save(m1);

        // when (시나리오_이렇게 하면)
        List<MemberDto> memberDto = memberRepository.findMemberDto();

        // then (기대결과_이렇게 된다.)
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }
    }

    @Test
    public void findByNames() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member m1 = new Member("홍길동", 10); memberRepository.save(m1);
        Member m2 = new Member("김철수", 20); memberRepository.save(m2);

        // when (시나리오_이렇게 하면)
        List<Member> result1 = memberRepository.findByNames(Arrays.asList("홍길동", "김철수"));
        List<Member> result2 = memberRepository.findByUsernameIn(Arrays.asList("홍길동", "김철수"));

        // then (기대결과_이렇게 된다.)
        for (Member member : result1) {
            System.out.println("member = " + member);
        }
    }

    @Test
    public void findOptionalByUsernameTest() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member m1 = new Member("홍길동", 10); memberRepository.save(m1);
        Member m2 = new Member("김철수", 20); memberRepository.save(m2);

        // when (시나리오_이렇게 하면)
        Optional<Member> result = memberRepository.findOptionalByUsername("김철수");

        // then (기대결과_이렇게 된다.)
        assertThat(result.orElseGet(() -> new Member("", 0)))
                .isEqualTo(m2);
    }

    @Test
    public void paging() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 10));
        memberRepository.save(new Member("member3", 10));
        memberRepository.save(new Member("member4", 10));
        memberRepository.save(new Member("member5", 10));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // when (시나리오_이렇게 하면)
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // Page로 반환받은 엔티티를 DTO로 쉽게 변환할 수 있습니다.
        page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam().getName()));

        // then (기대결과_이렇게 된다.)
//        assertThat(page.getContent().get(0).getUsername()).isEqualTo("member5");
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호를 확인합니다. (Spring Data JPA는 페이지 번호를 0번부터 시작합니다.)
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    public void buldUpdateTest() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        memberRepository.save(new Member("member1", 10));
        memberRepository.save(new Member("member2", 19));
        memberRepository.save(new Member("member3", 20));
        memberRepository.save(new Member("member4", 21));
        memberRepository.save(new Member("member5", 40));

        // when (시나리오_이렇게 하면)
        int resultCnt = memberRepository.bulkAgePlus(20);

        // then (기대결과_이렇게 된다.)
        assertThat(resultCnt).isEqualTo(3);
    }

    @Test
    public void findMemberLazy() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Team teamA = new Team("teamA"); teamRepository.save(teamA);
        Team teamB = new Team("teamB"); teamRepository.save(teamB);

        Member member1 = new Member("member1", 10, teamA); memberRepository.save(member1);
        Member member2 = new Member("member2", 20, teamB); memberRepository.save(member2);

        em.flush();
        em.clear();

        // when (시나리오_이렇게 하면)
        List<Member> members = memberRepository.findAll();

        // then (기대결과_이렇게 된다.)
        for (Member member : members) {
            System.out.println("member.teamClass = " + member.getTeam().getClass());
            System.out.println("member.team = " + member.getTeam().getName());
        }
    }

    @Test
    public void queryHint() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member1 = new Member("member1", 10);
        memberRepository.save(member1);
        em.flush();
        em.clear();

        // when (시나리오_이렇게 하면)
        Member findMember = memberRepository.findById(member1.getId()).get();
        findMember.setUsername("member2");
        em.flush();

        // then (기대결과_이렇게 된다.)
    }

    @Test
    public void callCustom() throws Exception {
        List<Member> result = memberRepository.findMemberCustom();
        for (Member member : result) {
            System.out.println("member = " + member);
        }
    }
}
