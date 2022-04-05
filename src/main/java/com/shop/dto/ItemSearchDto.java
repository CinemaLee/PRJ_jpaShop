package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType; // 상품 등록일 기준

    private ItemSellStatus searchSellStatus; // 판매상태

    private String searchBy; // 어떤 유형으로 조회할지 (itemNm, createBy)

    private String searchQuery = ""; // searchBy를 저장할 변수
}
