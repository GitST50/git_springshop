package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {

    private String searchDateType; //현재시간과 상품등록일을 비교해서 상품데이터 조회

    private ItemSellStatus searchSellStatus; //상품판매상태기준 상품데이터 조회

    private String searchBy; //어떤유형으로 조회할지 선택 itemNm:상품명 createdBy:상품 등록자 id

    private String searchQuery= ""; //조회할 검색어 저장할 변수, searchBy가 itemNM이면 상품명기준검색, createdBy면 상품등록자기준 검색



}
