package com.jpa.learning.jpa.entity.embeddedId;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@SpringBootTest
@Transactional
class EmbeddedIdTest {

    @Autowired
    EntityManager em;

    @Test
    void save() {
        EmbeddedParent parent = new EmbeddedParent();
        parent.setId(new EmbeddedParentId("myId1", "myId2"));
        parent.setName("parentName");
        em.persist(parent);

        em.flush();
        em.clear();

        EmbeddedParentId parentId = new EmbeddedParentId("myId1", "myId2");
        EmbeddedParent foundParent = em.find(EmbeddedParent.class, parentId);
        Assertions.assertThat(foundParent).isNotEqualTo(parent);
        Assertions.assertThat(foundParent.getName()).isEqualTo("parentName");
    }
}