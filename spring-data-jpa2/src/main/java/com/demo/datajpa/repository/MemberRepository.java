package com.demo.datajpa.repository;

import com.demo.datajpa.dto.MemberDto;
import com.demo.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {

    List<Member> findByUsernameAndAgeGreaterThan(String name, int age);

    // @Query 애너테이션을 이용해 Named 쿼리를 가져오지만, 생략해도 Named 쿼리를 가져옵니다.
    // Spring Data JPA의 규칙으로 'Entity명.메서드명' 이름을 가진 Named 쿼리가 있는지 먼저 찾습니다.
    @Query(name = "Member.findByUsername")
    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select new com.demo.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    List<Member> findByUsernameIn(List<String> names);

    Optional<Member> findOptionalByUsername(String name);

    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

    // select 쿼리는 JPA 코드 마지막에 .getResultList()나 .getSingleResult() 등이 들어가는 반면,
    // update 쿼리는 JPA 코드 마지막에 .executeQuery()를 호출해야 합니다.
    // @Modifying 은 .executeQuery()를 호출하라는 의미입니다.
    @Modifying(clearAutomatically = true)
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    // fetch join을 사용하면 조회해온 member와 연관된 team 도 같이 조회해옵니다.
    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();

    @Override
    @EntityGraph(attributePaths = {"team"})
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

    @EntityGraph(attributePaths = {"team"})
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
