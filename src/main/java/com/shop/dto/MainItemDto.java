package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    @QueryProjection //생성자에 어노테이션 선언하여 Querydsl로 조회시 MainItemDto객체로 바로 받아옴
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price)
    {

        this.id=id;
        this.itemNm=itemNm;
        this.itemDetail=itemDetail;
        this.imgUrl=imgUrl;
        this.price=price;
    }

}
