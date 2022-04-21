package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 상품이 장바구니에 들어있는지 조회
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // CartDetailDto 목록 조회
    // jpql 로 작성. 반환값으로 DTO객체를 바로 반환
    @Query(
            "select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
                    "from CartItem ci, ItemImg im " +
                    "join ci.item i " +
                    "where ci.cart.id = :cartId " +
                    "and im.item.id = ci.item.id " +
                    "and im.repimgYn = 'Y' " +
                    "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
