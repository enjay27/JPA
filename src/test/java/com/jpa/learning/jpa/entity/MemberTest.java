package com.jpa.learning.jpa.entity;

import org.apache.catalina.core.ApplicationContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional
class MemberTest {

    @Autowired
    EntityManagerFactory emf;

    @Autowired
    EntityManager em;

//    @Test
//    void save() {
//        Team team1 = new Team("team1");
//        em.persist(team1);
//
//        Member member1 = new Member("member1");
//        member1.setTeam(team1);
//        em.persist(member1);
//
//        Member member2 = new Member("member2");
//        member2.setTeam(team1);
//        em.persist(member2);
//    }
//
//    @Test
//    void find() {
//        Team team1 = new Team("team1");
//        em.persist(team1);
//
//        Member member1 = new Member("member1");
//        member1.setTeam(team1);
//        em.persist(member1);
//    }
//
//    @Test
//    void edit() {
//        Team team1 = new Team("team1");
//        em.persist(team1);
//
//        Team team2 = new Team("team2");
//        em.persist(team2);
//
//        Member member1 = new Member("member1");
//        member1.setTeam(team1);
//        em.persist(member1);
//
//        member1.setTeam(team2);
//
//        Assertions.assertThat(member1.getTeam()).isEqualTo(team2);
//    }
//
//    @Test
//    void findBidirectional() {
//        Team team1 = new Team("team1");
//        em.persist(team1);
//
//        Member member1 = new Member("member1");
//        member1.setTeam(team1);
//        em.persist(member1);
//
//        Member member2 = new Member("member2");
//        member2.setTeam(team1);
//        em.persist(member2);
//
//        em.flush();
//        em.clear();
//
//        Team team = em.find(Team.class, team1.getId());
//        List<Member> members = team.getMembers();
//
//        System.out.println("members.size() = " + members.size());
//        System.out.println("team.getName() = " + team.getName());
//
//        for (Member member : members) {
//            System.out.println("member.getUsername() = " + member.getUsername());
//        }
//    }
//
//    @Test
//    @Rollback(false)
//    void testSave() {
//        Team team1 = new Team("team1");
//        em.persist(team1);
//
//        Member member1 = new Member("member1");
//        member1.setTeam(team1);
//        em.persist(member1);
//
//        Member member2 = new Member("member2");
//        member2.setTeam(team1);
//        em.persist(member2);
//    }
//
//    @Test
//    @Rollback(false)
//    void failSave() {
//        Member member1 = new Member("member1");
//        em.persist(member1);
//
//        Member member2 = new Member("member2");
//        em.persist(member2);
//
//        Team team1 = new Team("team1");
//        team1.getMembers().add(member1);
//        team1.getMembers().add(member2);
//        em.persist(team1);
//    }
//
//    @Test
//    void saveBiDirection() {
//        Team team1 = new Team("team1");
//        em.persist(team1);
//
//        Member member1 = new Member("member1");
//        member1.setTeam(team1);
//        team1.getMembers().add(member1);
//        em.persist(member1);
//
//        Member member2 = new Member("member2");
//        member2.setTeam(team1);
//        team1.getMembers().add(member2);
//        em.persist(member2);
//    }

    @Test
    void unidirectionalTest() {

        Member member1 = new Member("member1");
        Member member2 = new Member("member2");

        Team team1 = new Team("team1");
        team1.getMembers().add(member1);
        team1.getMembers().add(member2);
        em.persist(member1);
        em.persist(member2);
        em.persist(team1);

        em.flush();
    }
}