package com.shop.entity;

import com.shop.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
public class Order extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    // 연관관계의 주인이 아닌 쪽은 mappedBy로 연관관계의 주인을 설정해준다.
    // 주인이 아닌 쪽은 읽기만 가능하다.
    // cascade : 영속성 전이. 부모의 엔티티 상태변화를 자식 엔티티에 모두 전이한다. > 주문엔티티를 저장하면서 주문 상품 엔티티도 함께 저장이 되는 경우..
    // orphanRemoval : 고아객체 제거. 고아객체란? 부모 엔티티와 연관관계가 끊어진 엔티티. 참조하는곳이 한 곳일때만 사용하길 권한다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

}
