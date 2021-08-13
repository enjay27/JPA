package com.jpa.learning.jpa.entity.idclass;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class IdClassTest {

    @Autowired
    EntityManager em;

    @Test
    void save() {
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
    }
}