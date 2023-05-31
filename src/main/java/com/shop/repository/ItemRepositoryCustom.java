package com.shop.repository;

import com.shop.dto.ItemSearchDto;
import com.shop.dto.MainItemDto;
import com.shop.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable); //상품 조회 조건을 담고있는 itemSearchDto와 페이징정보를 담은 Pageable을 파라미터로 받음
                                                                                 //Page<item> 을 리턴

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto,Pageable pageable);

}
