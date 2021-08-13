# 프록시와 연관관계 관리

## 프록시

> 지연 로딩 : JPA 는 엔티티가 실제 사용될 때까지 데이터베이스 조회를 지연하는 것

엔티티의 값을 실제 사용하는 시점에 데이터베이스에서 조회한다.

EntityManager.getReference() 메서드를 사용하면 엔티티를 실제 사용하는 시점까지 데이터베이스 조회를 미루게 된다.

이 메서드는 데이터베이스 접근을 위임한 프록시 객체를 반환한다.

![delegate](https://images.velog.io/images/ljinsk3/post/c21f77e8-03c1-40d7-83be-07255b7e1aeb/image.png)

프록시 객체는 실제 객체에 대한 참조(target)를 보관하고 프록시 객체의 메서드를 호출하면 실제 객체의 메서드를 호출하게 된다.

![initialize](https://images.velog.io/images/ljinsk3/post/89820fd3-9b03-4d13-bd47-ec09425e17fb/image.png)

1. 프록시 객체에 member.getName() 을 호출한다.
2. 프록시 객체에 엔티티가 생성되어 있지 않으면 영속성 컨텍스트에 엔티티 생성을 요청한다.
3. 영속성 컨텍스트는 데이터베이스를 조회하여 실제 엔티티 객체를 생성한다.
4. 프록시 객체는 생성된 엔티티 객체의 참조를 Member target 멤버변수에 보관한다.
5. 프록시 객체는 실제 엔티티 객체의 getName() 을 호출해서 결과를 반환한다.

### 특징

- 처음 사용할 때 한 번만 초기화된다.
- 프록시 객체가 초기화되면 프록시 객체를 통해 실제 엔티티에 접근할 수 있다.
- 프록시 객체는 원본 엔티티를 상속받은 객체다. 타입 체크시 주의
- 영속성 컨텍스트에 엔티티가 있다면 getReference() 를 호출해도 실제 엔티티를 반환한다.
- 초기화는 영속성 컨텍스트가 필요하다. 따라서 준영속 상태의 프록시를 초기화하면 문제가 생긴다.

### 식별자

프록시 객체는 식별자 값을 가지고 있으므로 식별자를 조회하는 getId() 는 호출해도 프록시를 초기화하지 않는다.

이를 이용하면 연관관계를 설정할 때 유용하다.

```java
Member member = em.find(Member.class, "member1");
Team team = em.getReference(Team.class, "team1");
member.setTeam(team);
```

프록시를 사용하면 데이터베이스 접근 횟수를 줄일 수 있다.

### 프록시 확인

PersistenceUnitUtil.isLoaded(entity) 를 사용하면 인스턴스의 초기화 여부를 확인 할 수 있다.

```java
boolean isLoaded = em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(entity);
```

## 즉시 로딩, 지연 로딩

> 엔티티의 조회 시점을 선택할 수 있다.

### 즉시 로딩

엔티티를 조회할 때 연관된 엔티티도 함께 조회된다.

em.find(Member.class, "member1") 에서 회원 엔티티와 연관된 팀 엔티티도 함께 조회된다.

### 지연 로딩

![lazy loading](https://3.bp.blogspot.com/-DLtFdGVo9Lg/WvRKKLCo_DI/AAAAAAAACHE/poVjl0TKphQlmHqplyNnLKCdgTfaQpAnwCLcBGAs/s400/lazy-loading.png)


연관된 엔티티를 실제 사용할 때 조회한다.

Member member = em.find(Member.class, "member1") 에서 반환된 엔티티를 이용하여

member.getTeam().getName() 처럼 조회한 팀 엔티티를 실제 사용하는 시점에 팀 엔티티를 조회한다.

```java
Member member = em.find(Member.class, "member1");
Team team = member.getTeam(); // 객체 그래프 탐색, 프록시 객체 반환
team.getName(); // 팀 객체 사용
```

member.getTeam() 에서는 프록시 객체를 반환해서 넣어준다.

만약 영속성 컨텍스트에 로딩되어 있다면 실제 객체를 반환한다.

### 프록시와 컬렉션 래퍼

> 엔티티에 컬렉션이 있을 경우 컬렉션을 추적하고 관리할 목적으로 제공하는 하이버네이트 내장 컬렉션

```java
Member member = em.find(Member.class, "member1");
List<Order> orders = member.getOrders(); // 컬렉션 래퍼 반환, 지연 로딩
Order order = orders.get(0); // 실제 데이터 조회, 엔티티 초기화
```

### 페치 전략과 조인 전략

> 컬렉션에 하나 이상 즉시 로딩하는 것은 권장하지 않는다.
> - 너무 많은 데이터를 반환할 수 있다.

> 컬렉션 즉시 로딩은 항상 외부조인을 사용한다.
> - 내부 조인을 사용하면 조인한 테이블의 데이터가 없는 경우 조회한 테이블조차 나오지 않는 문제가 발생하게 된다. 

모든 연관관계에 지연 로딩을 사용하고, 필요한 경우 즉시 로딩으로 바꾸는 것이 좋다.

> @ManyToOne, @OneToOne
> - (optional = false) : 내부 조인
> - (optional = true) : 외부 조인

> @OneToMany, @ManyToMany
> - (optional = false) : 외부 조인
> - (optional = true) : 외부 조인

### 영속성 전이 (Transitive Persistence) : Cascade

JPA는 cascade 옵션으로 영속성 전이를 제공한다. 부모 엔티티를 저장할 때 자식 엔티티도 함께 저장할 수 있다.

```java
@Entity
public class Parent {
    ...
    @OneToMany(mappedBy = "parent", cascade = CascadeType.PERSIST)
    private List<Child> children = new ArrayList<>();
}
```

cascade = CascadeType.PERSIST 속성을 사용하여 부모와 자식 엔티티를 한 번에 영속화할 수 있게 했다.

```java
Child child1 = new Child();
Child child2 = new Child();

Parent parent = new Parent();
child1.setParent(parent);
child2.setParent(parent);
parent.getChildren().add(child1);
parent.getChildren().add(child2);

em.persist(parent); // 연관된 자식까지 모두 저장
```

![cascade](https://images.velog.io/images/ljinsk3/post/3bafeed4-4595-40d6-abd0-aa42ec4c387c/image.png)

parent 영속화하면 CascadeType.PERSIST 로 설정한 자식 엔티티까지 함께 영속화해서 저장한다.

```java
Parent foundParent = em.find(Parent.class, "parent");
em.remove(foundParent);
```

CascadeType.REMOVE 설정 시 삭제에도 적용할 수 있다.

## 고아 객체 (Orphan)

> 고아 객체 제거 : 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제하는 기능

@OneToMany(mappedBy = "parent", orphanRemoval = true) 로 설정한다.

```java
Parent parent1 = em.find(Parent.class, id);
parent1.getChildren().remove(0);
```

컬렉션에서 제거하면 참조가 없어져 연관관계가 끊어진 고아 객체가 만들어지므로, 플러시 시점에 데이터베이스에서도 삭제된다. 

이 기능은 참조하는 곳이 하나일 때만 사용해야 한다. 따라서 orphanRemoval 은 @OneToOne, @OneToMany 에서만 사용할 수 있다.

