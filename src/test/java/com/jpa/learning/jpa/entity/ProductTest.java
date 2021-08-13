package com.jpa.learning.jpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductTest {

    @Autowired
    EntityManager em;

    @Test
    void save() {
//        Product productA = new Product();
//        productA.setName("상품A");
//        em.persist(productA);
//
//        Member member1 = new Member();
//        member1.setUsername("회원1");
//        member1.getMember().add(productA);
//        em.persist(member1);
//
//        em.flush();
    }

}