# Querydsl 데모
## 1. 사용법

가장 네이티브한 기본 사용법은 다음과 같습니다. 

```java
@Autowired
EntityManager em;

JPAQueryFactory queryFactory = new JPAQueryFactory(em);
QMember m = new QMember("m"); // 변환될 JPQL의 alias를 설정합니다.
Member findMember = queryFactory
        .select(m)
        .from(m)
        .where(m.username.eq("member1"))
        .fetchOne();
```
하지만, 보통 위와 같이 QMember를 직접 생성해주기보다는 이미 QMember에 정의된 <code>QMember.member</code>를 사용합니다.
```java
// QMember에 static QMember member = new QMember("member1"); 이 정의되어있습니다.
// import static QMember.*; 한 후 아래와 같이 사용합니다.
Member findMember = queryFactory
        .select(member)
        .from(member)
        .where(member.username().eq("member1"))
        .fetchOne();
```

### 1.1 검색
```java
member.username.isNotNull() // 이름이 is not null

member.age.in(10, 20) // age in (10, 20)
member.age.notIn(10, 20) // age not in (10, 20)

member.age.goe(30) // age >= 30 (great or equal)
member.age.gt(30) // age > 30
member.age.lot(30) // age <= 30 (low or equal)
member.age.lt(30) // age < 30

member.username.contains("member") // like '%member%' 검색
member.username.startsWith("member") // like 'member%' 검색
```

### 1.2 동적 쿼리
Querydsl에서 동적 쿼리를 해결하는 방법은 두 가지가 있습니다.
- BooleanBuilder
- Where 다중 파라미터 사용

우선 BooleanBuilder를 사용하는 방법은 다음과 같습니다.
```java
private List<Member> searchMember1(String usernameCondition, Integer ageCondition) {
        BooleanBuilder builder = new BooleanBuilder();
        if (usernameCondition != null) {
            builder.and(member.username.eq(usernameCondition));
        }
        if (ageCondition != null) {
            builder.and(member.age.eq(ageCondition));
        }
        return queryFactory
                .selectFrom(member)
                .where(builder)
                .fetch();
    }
```
다음으로 Where 다중 파라미터 방법은 다음과 같습니다.
```java
private List<Member> searchMember2(String usernameCondition, Integer ageCondition) {
        return queryFactory
                .selectFrom(member)
                .where(usernameEq(usernameCondition), ageEq(ageCondition))
                .fetch();
    }

private BooleanExpression usernameEq(String usernameCondition) {
    return usernameCondition != null ? member.username.eq(usernameCondition) : null;
}

private BooleanExpression ageEq(Integer ageCondition) {
    return ageCondition != null ? member.age.eq(ageCondition) : null;
}
```
BooleanBuilder와 비교했을 때 위 방법이 더 가독성이 뛰어납니다. 추가로 조건들을 조립할 수 있다는 아주 큰 장점이 있습니다.
```java
private BooleanExpression allEq(String usernameCond, Integer ageCond) {
    return usernameEq(usernameCond).and(ageEq(ageCond));
}
```

### 1.3 사용자 정의 리포지토리
<p align="center"><img src="https://user-images.githubusercontent.com/31037742/145519432-8ba30137-c1a2-49dc-89b2-3b191e6e875e.png" width="500"></p>


