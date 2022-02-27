package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 기본메소드 제공 : save, delete, count, findAll

    // 쿼리 메소드
    // 조건이 많아지면 이름이 복잡해짐
    List<Item> findByItemNm(String itemNm);
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);
    List<Item> findByPriceLessThan(Integer price);
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);


    // JPQL
    @Query("SELECT i FROM Item i " +
            "WHERE i.itemDetail like %:itemDetail% " +
            "order by i.price desc")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

}
