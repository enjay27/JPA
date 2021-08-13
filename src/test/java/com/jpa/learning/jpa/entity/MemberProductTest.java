package com.jpa.learning.jpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberProductTest {

    @Autowired
    EntityManager em;

    @Test
    @Rollback(false)
    void save() {
        Member member1 = new Member();
        member1.setUsername("회원1");
        em.persist(member1);

        Product productA = new Product();
        productA.setName("상품1");
        em.persist(productA);

        MemberProduct memberProduct = new MemberProduct();
        memberProduct.setMember(member1);
        memberProduct.setProduct(productA);
        memberProduct.setOrderAmount(2);
        memberProduct.setDateTime(LocalDateTime.now());
        em.persist(memberProduct);
    }
}