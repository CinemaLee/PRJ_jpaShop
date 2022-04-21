package com.shop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartOrderDto {

    private Long cartItemId;
    private List<CartOrderDto> cartOrderDtoList; // 장바구니에서 여러 개의 상품을 주문하기 떄문. 자기 자신을 List로 갖는다.
}
