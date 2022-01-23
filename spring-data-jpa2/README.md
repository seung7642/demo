## Spring-Data-JPA

기본 형태는 다음과 같습니다.

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
	
}
```

위 코드에서 상속받는 JpaRepository의 상속 관계도는 다음과 같습니다. (모두 인터페이스)
JpaRepository ---> PagingAndSortingRepository ---> CrudRepository ---> Repository

JpaRepository 인터페이스에는 다음의 메서드가 정의되어 있습니다.

- findAll()
- findAllById()
- saveAll()
- getOne()



### 1. 쿼리 메서드 기능

---

Spring-Data-JPA에는 쿼리 메서드라고 하는 기능이 있습니다.
쿼리 메서드 기능은 총 세 가지가 있습니다.

- 메서드 이름으로 쿼리 생성
- 메서드 이름으로 JPA NamedQuery 호출
- `@Query` 애너테이션을 사용해서 리포지토리 인터페이스에 쿼리 직접 정의

**<u>메서드 이름으로 쿼리 생성</u>**
먼저, 메서드 이름으로 쿼리 생성하는 기능부터 살펴보겠습니다.
Repository 인터페이스 내에 특정 규칙에 맞춰 메서드를 정의하면 알아서 기능을 만들어주는(?) 기능입니다.
해당 방법은 where에 조건 파라미터가 많을수록 메서드명이 길어진다는 단점이 있습니다. 그래서 조건절에 걸 파라미터가 1~2개라면 해당 방법을 사용하면 되고, 그 이상이라면 3번째 기능인 `@Query` 애너테이션을 이용해 쿼리를 직접 정의하면 됩니다.

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  // select * from Member
  // where username = :name and 
	List<Member> findByUsernameAndAgeGreaterThan(String name, int age);
  
  List<Member> findTop3By(); // 'By' 뒤에 아무 것도 없으면 전체 조회, Top3는 'limit 3' 을 의미합니다.
  
  List<Member> findByUsernameIn(List<String> names); // `where m.username in :names` 을 의미합니다.
}
```

위와 같이 규칙에 맞춰 메서드명을 작성하면, Spring-Data-JPA는 사용자가 어떤 작업을 원하는지 유추할 수 있습니다.

**<u>메서드 이름으로 JPA NamedQuery 호출</u>**
JPA의 NamedQuery는 다음과 같이 자주 사용될법한 쿼리를 미리 정의해두는 기능(?)입니다.
(JPA의 NamedQuery는 실무에서는 잘 사용되지 않는 기능입니다.)
NamedQuery에서 진짜 중요한 한 가지는 정의한 쿼리에 오타가 있으면 컴파일 에러를 발생시킨다는 것입니다.

```java
@Entity
@NamedQuery(
		name = "Member.findByUsername",
  	query = "select m from Member m where m.username" = :username
)
public class Member {
  
}
```

위와 같이 Entity 클래스에 `@NamedQuery`를 정의해두고, 아래와 같이 리포지토리에서 사용합니다.

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  
  // Spring Data JPA는 'Entity클래스명.메서드명' 의 이름을 가진 NamedQuery가 있는지 우선적으로 찾기 때문에,
  // 지금 상황에선 @Query 를 생략할 수 있습니다.
  @Query(name = "Member.findByUsername")
  List<Member> findByUsername(@Param("username") String username);
}
```

**<u>@Query를 사용해서 리포지토리의 메서드에 쿼리를 직접 정의하기</u>**
실무에서 많이 사용되는 방법으로 다음과 같이 리포지토리 메서드 위에 쿼리를 바로 적어줄 수 있습니다.

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  @Query("select m from Member m where m.username = :username and m.age = :age")
  List<Member> findUser(@Param("username") String username, @Param("age") int age);
}
```

만약 DTO로 조회하고 싶다면 다음과 같이 조회합니다. (Querydsl을 사용하면 간략해집니다.)

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  @Query("select com.demo.dto.MemberDto(m.id, m.username, t.name) from Member m join Team t")
  List<MemberDto> findMemberDto();
}
```

### 2. 반환 타입

---

Spring Data JPA는 리포지토리 메서드가 여러 가지 반환 타입을 가질 수 있도록 지원합니다.
그 중, Optional 반환 타입의 반환에 대해서 살펴보겠습니다.

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findOptionalByUsername(String name);
}
```

Optional로 반환받으면 다음과 같이 테스트해볼 수 있습니다.

```java
@Test
void 옵셔널_테스트() {
  Optional<Member> result = memberRepository.findOptionalByUsername("김철수");
  Member findMember = result.orElseGet(() -> new Member("", 0));
  assertThat(findMember.getUsername()).isEqualsTo("김철수");
}
```

### 3. 페이징과 정렬

---

순수 JPA를 사용했을 때의 페이징 방식과 Spring Data JPA를 사용했을 때의 페이징 방식의 차이를 살펴보겠습니다.

다음 조건으로 페이징과 정렬을 해야한다고 가정해보겠습니다.

- 검색 조건: 나이가 10살
- 정렬 조건: 이름으로 내림차순
- 페이징 조건: 첫 번째 페이지, 페이지당 보여줄 데이터는 3건



먼저, 순수 JPA를 사용했을 때의 페이징 쿼리와 총 갯수를 구하는 메서드는 다음과 같이 작성할 수 있습니다.

```java
public List<Member> findByPaging(int age, int offset, int limit) {
  return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
    			.setParameter("age", age)
    			.setFirstResult(offset)
    			.setMaxResults(limit)
    			.getResultList();
}

public long totalCount(int age) {
  return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
    			.setParameter("age", age)
    			.getSingleResult();
}
```



Spring Data 프로젝트는 페이징과 정렬 쿼리를 표준화 하였습니다. (Spring-Data-JPA든, Spring-Data-MongoDB든 다 똑같습니다!)

- `org.springframework.data.domain.Sort` : 정렬 기능
- `org.springframework.data.domain.Pageable` : 페이징 기능 (내부에 Sort 를 포함하고 있습니다.)

페이징과 정렬 쿼리에서 특별한 반환 타입을 가집니다.

- `org.springframework.data.domain.Page` : 추가 count 쿼리 결과를 포함하는 페이징
- `org.springframework.data.domain.Slice` : 추가 count 쿼리 없이 다음 페이지만 확인 가능 (내부적으로 limit + 1 조회)
- `List` (자바 컬렉션) : 추가 count 쿼리 없이 결과만 반환



Spring Data JPA를 이용한 페이징 쿼리 메서드는 다음과 같은 형태를 가집니다.

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  Page<Member> findByAge(int age, Pageable pageable);
}
```

위 메서드는 다음과 같이 테스트해볼 수 있습니다.

```java
@Test
public void findByAgeTest() {
  int age = 10;
  // 첫 번째 파라미터: 가져올 페이지 번호 (Spring Data는 페이지 번호를 0번부터 시작합니다.)
  // 두 번째 파라미터: 페이지당 갯수
  PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
  
  Page<Member> page = memberRepository.findByAge(age, pageRequest);
  
  assertThat(page.getContent().get(0).getUsername()).isEqualTo("member5");
  assertThat(page.getTotalElements()).isEqualTo(5);
  assertThat(page.getNumber()).isEqualTo(0); // 가져온 페이지의 페이지 번호를 확인합니다.
  assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 갯수를 확인합니다.
  assertThat(page.isFirst()).isTrue(); // 가져온 페이지가 첫 번째 페이지인지를 확인합니다.
  assertThat(page.hasNext()).isTrue(); // 가져온 페이지의 다음 페이지가 존재하는지 확인합니다.
}
```

### 4. 벌크성 수정 쿼리

---

먼저, 순수 JPA를 사용했을 때의 벌크성 수정 쿼리를 날리는 메서드는 다음과 같이 작성할 수 있습니다.

```java
@Repository
public interface MemberJpaRepository {
  public int bulkAgePlus(int age) {
    return em.createQuery("update Member m set m.age = m.age + 1 where m.age >= :age")
      			.setParameter("age", age)
      			.executeQuery();
  }
}
```

Spring Data JPA를 사용했을 때의 벌크성 수정 쿼리를 날리는 메서드는 다음과 같이 작성할 수 있습니다.

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  
  @Modifying(clearAutomatically = true)
  @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
  int bulkAgePlus(@Param("age") int age);
}
```

### 5. @EntityGraph

---

해당 기능을 이해하기 위해선 JPA의 `join fetch` 기능에 대한 이해가 필요합니다.
다음과 같이 `.findAll()` 로 Member를 모두 조회해온 후, `.getTeam()` 을 호출하면 LAZY 조회라는 가정하에 실행될 때마다 쿼리가 나갑니다.

```java
@Test
void findMemberLazy() {
  Team teamA = new Team("teamA"); teamRepository.save(teamA);
  Team teamB = new Team("teamB"); teamRepository.save(teamB);
  
  Member member1 = new Member("member1", 10, teamA); memberRepository.save(member1);
  Member member2 = new Member("member2", 20, teamB); memberRepository.save(member2);
  
  em.flush();
  em.clear();
  
  List<Member> members = memberRepository.findAll();
  
  for (Member member : members) {
    System.out.println("member = " + member.getTeam()); // 이때마다 team을 조회해오기 위해 쿼리를 날립니다.(N+1문제)
  }
}
```



위의 N+1 문제를 방지하기 위해 fetch join을 사용하는데, fetch join을 활용하면 **연관된 엔티티 값을 한번에 세팅**해줍니다. (프록시 객체가 아니게 됨)

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  
  @Query("select m from Member m left join fetch m.team")
  List<Member> findMemberFetchJoin();
}
```



위와 같이 매번 @Query에 fetch join 관련 쿼리를 작성하는걸 간소화한 기능이 @EntityGraph 입니다.
attributePaths의 속성 값으로 같이 조회해올 연관 엔티티를 명시합니다. (당연히 @EntityGraph도 내부적으로는 fetch join입니다.)

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  
  @Override
  @EntityGraph(attributePaths = {"team"})
  List<Member> findAll();
}
```

### 6. JPA Hint & Lock

---

SQL이 아니라 JPA 구현체에게 힌트를 제공해줄 수 있습니다. (패스)

이것도 JPA의 트랜잭션과 Lock 매커니즘이란게 있구나.. 선에서 넘기겠습니다.

```java
public interface MemberRepository extends JpaRepository<Member, Long> {
  
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  List<Member> findLockByUsername(String username);
}
```

### 7. 사용자 정의 리포지토리 구현 (Querydsl 쓸 때 실무에서 많이 사용됨)

---

Spring Data JPA 리포지토리는 인터페이스만 정의하면 구현체는 Spring이 자동으로 생성해줍니다.
그런데 구현체를 직접 커스텀하고 싶다면 어떻게 해야 할까요? 예를 들면 다음과 같은 이유로 구현체를 사용자가 직접 만들어주고 싶을 수 있습니다.

- JPA 직접 사용하기 위해 (`EntityManager`)
- Spring JDBC Template 사용하기 위해
- MyBatis 사용하기 위해
- DB 커넥션 직접 사용하기 위해
- Querydsl 사용하기 위해
- 등등 ...



새로 추가해줄 메서드를 정의할 인터페이스를 만듭니다.

```java
public interface MemberRepositoryCustom {
  List<Member> findMemberCustom();
}
```

이제 Spring Data JPA가 알아서 만들어주는 구현체가 아닌, 직접 구현체를 정의해줍니다.
여기서 중요한 규칙이 있는데, 위 인터페이스명은 아무렇게나 해도 상관없지만, 위 인터페이스를 구현하는 클래스명은 리포지토리명+Impl 이어야합니다.

```java
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {
  
  private final EntityManager em;
  
  public List<Member> findMemberCustom() {
    return em.createQuery("select m from Member m", Member.class)
      				.getResultList();
  }
}
```

마지막으로 리포지토리에 연결시켜줍니다.

```java
public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
  
}
```

### 8. Auditing

---

애플리케이션 운영을 위해 Entity 클래스를 생성, 변경할 때 변경한 사람과 시간을 추적하고 싶을 수 있습니다.

- 등록일
- 수정일
- 등록자
- 수정자



먼저, 순수 JPA를 사용할 경우 아래와 같이 Entity 클래스를 만든 후, Member와 같은 다른 Entity에서 상속을 받도록 합니다.

```java
@MappedSuperclass
public class JpaBaseEntity {
  
  @Column(updateable = false)
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
  
  @PrePersist
  public void prePersist() {
    LocalDateTime now = LocalDateTime.now();
    createdDate = now;
    updatedDate = now;
  }
  
  @PreUpdate
  public void preUpdate() {
    LocalDateTime now = LocalDateTime.now();
    updatedDate = now;
  }
}
```



마찬가지로 순수 JPA 코드는 Spring Data JPA를 활용해 간소화할 수 있습니다.
우선, 스프링 부트 설정 클래스에 `@EnableJpaAuditing` 을 명시합니다.

```java
@EnableJpaAuditing
@SpringBootApplication
public class DemoApplication {
  //
}
```

그 후, 다음과 같이 작성할 수 있습니다.

```java
@Getter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntity {

	@CreatedDate
	@Column(updateable = false)
	private LocalDateTime createdDate;
	
	@LastModifiedDate
	private LocalDateTime lastModifiedDate;
}
```

