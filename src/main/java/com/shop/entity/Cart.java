package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Getter @Setter
@ToString
public class Cart extends BaseEntity{

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // Cart가 Member를 일방적으로 매핑. = 일대일 단방향.
    // 일대일, 다대일로 매핑할 경우 즉시 로딩(EAGER)을 기본 Fetch 전략으로 한다.
    @OneToOne(fetch = FetchType.LAZY) // 회원과 일대일로 매핑.
    @JoinColumn(name = "member_id") // 매핑할 회래키 지정.
    private Member member;

}
