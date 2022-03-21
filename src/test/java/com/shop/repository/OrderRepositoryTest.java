package com.shop.repository;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.Member;
import com.shop.entity.Order;
import com.shop.entity.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager em;

    public Item createItem(){
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(9999);
        item.setItemDetail("상세설명1");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        return item;
    }

    public Order createOrder(){
        Order order = new Order();

        // Item생성 > OrderItem생성 > Order생성
        for(int i=0; i<3; i++){
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(){

        Order order = new Order();

        // Item생성 > OrderItem생성 > Order생성
        for(int i=0; i<3; i++){
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order); // order 엔티티가 먼저 저장되고 이후 영속성 전이되면서 orderItems들도 저장됨.
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);
        assertEquals(3, savedOrder.getOrderItems().size());
    }


    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest(){
        Order order = createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }


    @Test
    @DisplayName("지연로딩 테스트")
    public void lazyLoadingTest(){
        Order order = createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new); // 지연로딩으로 설정하면 OrderItem 엔티티만 조회함.
        System.out.println("Order Class : "+orderItem.getOrder().getClass()); // OrderItem과 연관관계에 있는 Order 엔티티는 아직 사용하고 있지 않기 때문에 프록시 객체를 넣어두게 됨.
        System.out.println("=======================================");
        orderItem.getOrder().getOrderDate(); // 실제 Order를 사용하는 시점에 조회 쿼리가 실행된다.
        System.out.println("=======================================");
    }
}