package com.demo.datajpa.repository;

import com.demo.datajpa.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false) // 실제 테스트할 때에는 false로 할 경우 테스트 데이터가 남아있게 되니, 해당 옵션을 true로 줘야합니다.
class MemberJpaRepositoryTest {

    @Autowired
    MemberJpaRepository memberJpaRepository;

    @Test
    public void 테스트_멤버() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member = new Member("홍길동");
        Member savedMember = memberJpaRepository.save(member);

        // when (시나리오_이렇게 하면)
        Member findMember = memberJpaRepository.find(savedMember.getId());

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
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // when (시나리오_이렇게 하면)
        Member findMember1 = memberJpaRepository.findById(member1.getId())
                .orElseGet(() -> new Member(""));
        Member findMember2 = memberJpaRepository.findById(member2.getId())
                .orElseGet(() -> new Member(""));

        List<Member> all = memberJpaRepository.findAll();

        long count = memberJpaRepository.count();

        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long countAfterDelete = memberJpaRepository.count();

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
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // when (시나리오_이렇게 하면)
        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThan("홍길동", 15);

        // then (기대결과_이렇게 된다.)
        assertThat(result.get(0)).isEqualTo(member2);
    }

    @Test
    public void NamedQuery_테스트() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        Member member1 = new Member("홍길동", 10);
        Member member2 = new Member("이길동", 20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // when (시나리오_이렇게 하면)
        List<Member> result = memberJpaRepository.findByUsername("이길동");

        // then (기대결과_이렇게 된다.)
        assertThat(result.get(0)).isEqualTo(member2);
    }

    @Test
    public void paging() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 10));
        memberJpaRepository.save(new Member("member3", 10));
        memberJpaRepository.save(new Member("member4", 10));
        memberJpaRepository.save(new Member("member5", 10));

        int age = 10;
        int offset = 0;
        int limit = 3;

        // when (시나리오_이렇게 하면)
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // then (기대결과_이렇게 된다.)
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    public void buldUpdateTest() throws Exception {
        // given (전제조건_이런게 주어졌을 때)
        memberJpaRepository.save(new Member("member1", 10));
        memberJpaRepository.save(new Member("member2", 19));
        memberJpaRepository.save(new Member("member3", 20));
        memberJpaRepository.save(new Member("member4", 21));
        memberJpaRepository.save(new Member("member5", 40));

        // when (시나리오_이렇게 하면)
        int resultCnt = memberJpaRepository.bulkAgePlus(20);

        // then (기대결과_이렇게 된다.)
        assertThat(resultCnt).isEqualTo(3);
    }
}