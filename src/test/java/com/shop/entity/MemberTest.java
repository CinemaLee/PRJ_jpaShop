package com.shop.entity;

import com.shop.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;


    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "강우", roles = "USER")
    public void auditTest(){
        Member member = new Member();
        memberRepository.save(member);

        em.flush();
        em.clear();

        Member member1 = memberRepository.findById(member.getId()).orElseThrow(EntityExistsException::new);
        System.out.println("regId : "+member1.getCreatedBy());
        System.out.println("regDt : "+member1.getRegTime());
    }

}