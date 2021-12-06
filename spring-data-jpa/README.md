# Shop (Spring-Data-JPA 데모)
## 1. 기능
- 회원 기능
  - 회원 등록/조회
- 상품 기능
  - 상품 등록/수정/조회
- 주문 기능
  - 상품 주문
  - 주문 내역 조회
  - 주문 취소
- 기타 요구사항
  - 상품은 재고 관리가 필요하다.
  - 상품의 종류는 도서, 음반, 영화가 있다.
  - 상품을 카테고리로 구분할 수 있다.
  - 상품 주문시 배송 정보를 입력할 수 있다.

예제를 단순화하기 위해 다음 기능은 구현하지 않습니다.
- 로그인과 권한 관리X 
- 파라미터 검증과 예외 처리 단순화
- 상품은 도서만 사용
- 카테고리는 사용X
- 배송 정보는 사용X 
## 2. 설계
### 2.1 엔티티 설계
Category와 Item 관계를 보면 N:M 관계로 나타냈는데, 이는 JPA를 학습하는 차원에서 나타낸 것일뿐 실무에서는 1:N, M:1 관계로 풀어서 나타내야 합니다. 
또한, Member와 Order를 보면 양방향 연관 관계로 설정을 해두었는데, 이또한 마찬가지로 학습하는 차원에서 양방향으로 설정해둔 것일뿐
실무에서는 되도록 단방향 연관 관계로 끝내는 것이 좋습니다. <br/>

<p align="center">
<img src="https://user-images.githubusercontent.com/31037742/144737239-0b341b40-1c0d-4240-ba79-56e46a9cda6f.png">
</p>

### 2.2 ERD 설계

<p align="center">
<img src="https://user-images.githubusercontent.com/31037742/144738947-3a8f9511-44c6-474b-9160-4812822561d3.png">
</p>

## 3. 주의사항
### 3.1 Entity 설계시 주의점
- Entity에는 가급적 Setter를 사용하지 말자. (변경 포인트가 너무 많아서 유지보수가 어렵다.)
- 모든 연관 관계는 지연로딩(LAZY)으로 설정하자.
  - 즉시로딩(EAGER)은 예측이 어렵고, 어떤 SQL이 실행될지 추적하기 어렵다. 특히 JPQL을 실행할 때 N+1 문제가 자주 발생한다.
  - 연관된 Entity를 함께 DB에서 조회해야 한다면, fetch join 또는 Entity 그래프 탐색을 사용하자.
- 컬렉션 필드는 기본적으로 new ArrayList<>()와 같이 초기화를 해두자.
  - NPE 방지 
  - Hibernate는 Entity를 영속화할 때, 컬렉션을 감싸서 Hibernate가 제공하는 내장 컬렉션으로 변경한다. 만약 <code>getOrders()</code>처럼 임의의 
  메서드에서 컬렉션을 잘못 생성하면 Hibernate 내부 메커니즘에 문제가 발생할 수 있다. 따라서 필드 레벨에서 생성하는 것이 가장 안전하고, 코드도 간결하다.

## 4. 애플리케이션 아키텍쳐
계층형 구조 사용
- controller, web: 웹 계층
- service: 비즈니스 로직, 트랜잭션 처리
- repository: JPA를 직접 사용하는 계층, EntityManager 사용
- domain: Entity가 모여있는 계층, 모든 계층에서 사용

패키지 구조
- com.demo.jpashop
  - domain
  - exception
  - repository
  - service
  - web

개발 순서
1. 서비스, 리포지토리 계층을 개발
2. 테스트 케이스를 작성해서 검증
3. 마지막에 웹 계층 적용





