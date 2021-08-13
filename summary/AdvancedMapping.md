# 고급 매핑

## 상속 관계 매핑

> 객체 상속 구조와 데이터베이스의 슈퍼타입 서브타입 관계를 매핑한다.

### 조인 전략

![inheritance mapping](https://lar542.github.io/img/post_img/JPA-2019-09-17-1.png)

엔티티 각각을 테이블로 만들고 자식 테이블이 부모 테이블의 기본 키를 받아서
기본 키 + 외래 키로 사용하는 전략이다.

따라서 타입을 구분하는 컬럼을 추가해야 한다. (위에서는 DTYPE)

```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;
    
    private String name;
    private int price;
}
```
```java
@Entity
@DiscriminatorValue("A")
public class Album extends Item {
    
    private String artist;
}

@Entity
@DiscriminatorValue("M")
@PrimaryKeyJoinColumn(NAME = "MOVIE_ID")
public class Movie extends Item {

    private String director;
    private String actor;
}
```

- @Inheritance(strategy = InheritanceType.JOINED) : 상속 어노테이션에 Join 전략 속성을 사용했다.
- @DiscriminatorColumn(name = "DTYPE") : 구분 컬럼을 만들었고 컬럼 이름을 DTYPE 으로 지정했다.
- @DiscriminatorValue("M") : 하위 엔티티에서 구분 컬럼에 입력할 값을 지정한다.
- @PrimaryKeyJoinColumn(NAME = "MOVIE_ID") : PK 컬럼 이름을 재정의한다.

#### 장점?
- 테이블이 정규화된다.
- 외래 키 참조 무결성 제약조건을 활용한다.
- 저장공간을 효율적으로 사용한다.

#### 단점?
- 조인이 많이 사용된다.
- 조회 쿼리가 복잡하다.
- 데이터를 등록할 때 INSERT SQL 이 두 번 실행된다.

### 단일 테이블 전략

![one table strategy](https://blog.kakaocdn.net/dn/canNt2/btq36R0tLBQ/5YdDyc8ZxXRRysZGWdZ1Lk/img.png)

테이블 하나만을 사용하여 필요한 모든 컬럼을 넣는 전략이다.

```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;
    
    private String name;
    private int price;
}
```

- @Inheritance(strategy = InheritanceType.SINGLE_TABLE) : 위의 Join 전략에서 strategy 만 바꿔주면 된다.

#### 장점?
- 조인이 필요 없어 성능이 좋다.
- 조회 쿼리가 단순하다.

#### 단점?
- 자식 엔티티가 매핑한 컬럼은 모두 null 을 허용해야 한다.
- 단일 테이블에 컬럼들이 들어가므로 테이블이 커질 수 있다. 상황에 따라 조회 성능이 안나오게 된다.

> 구분 컬럼을 꼭 사용해야 한다. @DiscriminatorColumn(name = "DTYPE")
>
> 지정하지 않으면 엔티티의 이름을 사용한다 (Movie, Album ...)

### 구현 클래스마다 테이블 전략

![table per class](https://user-images.githubusercontent.com/43127088/109960417-15a37900-7d2c-11eb-9a9b-498e164a3cd4.PNG)

자식 엔티티마다 테이블을 만든다. 부모 테이블이 따로 없고 자식 테이블마다 컬럼이 추가되어 있다.

```java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@DiscriminatorColumn(name = "DTYPE")
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;
    
    private String name;
    private int price;
}
```

일반적으론 추천하지 않는다.

#### 장점?
- 서브타입을 구분해서 처리할 때 효과적이다.
- not null 제약조건을 사용할 수 있다.

#### 단점?
- 여러 자식 테이블을 함께 조회할 때 성능이 안 좋다.
- 자식 테이블을 통합해서 쿼리하기 어렵다.

## @MappedSuperclass

![mapped superclass](https://blog.kakaocdn.net/dn/43Uc3/btqAYpykMVx/qjkJQw17eH8N354KSFDvQ0/img.png)

부모 클래스를 상속 받는 자식 클래스에게 매핑 정보만 제공한다.

```java
@MappedSuperclass
abstract public class BaseEntity {
    
    @Id @GeneratedValue
    private Long id;
    
    @ManyToOne
    Address address;
    
    private String name;
}
```
```java
@Entity
@AttributeOverride(name = "id", column = @Column(name = "SUB_A_ID"))
public class SubEntityA extends BaseEntity {
    
    private String email;
}

@Entity
@AssociationOverride(name="address",
        joinColumns=@JoinColumn(name="ADDR_ID"))
public class SubEntityB extends BaseEntity {

    private String shopName;
}
```

- @AttributeOverride(name = "id", column = @Column(name = "SUB_A_ID")) : 매핑 정보를 재정의 한다.
- @AssociationOverride(name="address", joinColumns=@JoinColumn(name="ADDR_ID")) : 연관 관계를 재정의 한다.

#### 특징?
- 테이블과 매핑되지 않고 자식 클래스에 엔티티의 매핑 정보를 상속하기 위해 사용한다.
- MappedSuperclass 는 엔티티가 아니다.
- abstract class 로 만드는 것이 권장된다.

## 식별 관계 매핑과 복합 키

참조 : [[Database] 식별관계와 비 식별관계](https://deveric.tistory.com/108#:~:text=%EB%B9%84%20%EC%8B%9D%EB%B3%84%20%EA%B4%80%EA%B3%84%EB%9E%80%20%EB%B6%80%EB%AA%A8,%EC%9C%BC%EB%A1%9C%20%EC%83%9D%EC%84%B1%EB%90%A0%20%EC%88%98%20%EC%9E%88%EC%8A%B5%EB%8B%88%EB%8B%A4.)

### 식별 관계 (Identifying Relationship)


![identification relationship](https://leejaedoo.github.io/assets/img/identifying_relationship.jpg)
- 부모 테이블의 기본 키를 내려받아서 자식 테이블의 기본 키 + 외래 키로 사용하는 관계

### 비식별 관계
![non identification relationship](https://leejaedoo.github.io/assets/img/non_identifying_relationship.jpg)
- 부모 테이블의 기본 키를 자식 테이블의 외래 키로만 사용하는 관계

> 필수적 비식별 관계 (Mandatory) : 외래 키에 Null 을 허용하지 않아 연관관계를 필수적으로 맺어야 한다.

> 선택적 비식별 관계 (Optional) : 외래 키에 Null 을 허용한다.

최근엔 비식별 관계를 주로 사용하며 꼭 필요한 경우에만 식별 관계를 사용한다.

### 복합 키 : 비식별 관계 매핑
```java
@Entity
public class Hello {
    @Id
    private String id;
    
    @Id
    private Long id2; // 예외 발생
}
```
하나의 엔티티에 @Id 를 두개 두면 매핑 예외가 발생한다.

JPA 에서는 @IdClass 와 @EmbeddedId 2가지 방법이 있다.

### @IdClass

```java
@Entity
@IdClass(ParentId.class)
public class Parent {

    @Id
    @Column(name = "PARENT_ID1")
    private String id1;

    @Id
    @Column(name = "PARENT_ID2")
    private String id2;
    
    private String name;
}
```
```java
@NoArgsConstructor
public class ParentId implements Serializable {
    private String id1;
    private String id2;

    public ParentId(String id1, String id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    ...equals, hashCode 구현...
}
```

#### 조건?
- 식별자 클래스의 속성명과 엔티티에서 사용하는 식별자의 속성명이 같아야 한다. (id1, id2)
- Serializable 을 구현해야 한다.
- equals, hashCode 구현해야 한다.
- 기본 생성자가 있어야 한다.
- 식별자 클래스는 public 이어야 한다.

```java
Parent parent = new Parent();
parent.setId1("myId1");
parent.setId2("myId2");
parent.setName("parentName");
em.persist(parent);

em.flush();
em.clear();

ParentId parentId = new ParentId("myId1", "myId2");
Parent foundParent = em.find(Parent.class, parentId);
Assertions.assertThat(foundParent).isNotEqualTo(parent);
Assertions.assertThat(foundParent.getName()).isEqualTo("parentName");
```

복합 키를 가진 클래스를 사용하여 조회가 가능하다.

```java
@Entity
public class Child {
    
    @Id
    private String id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "PARENT_ID1",
                    referencedColumnName = "PARENT_ID1"),
            @JoinColumn(name = "PARENT_ID2",
                    referencedColumnName = "PARENT_ID2"),
    })
    private Parent parent;
}
```

자식 엔티티에서는 여러 컬럼을 매핑해야 하므로 @JoinColumns 어노테이션을 사용한다.

### @EmbeddedId

