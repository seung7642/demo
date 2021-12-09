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
