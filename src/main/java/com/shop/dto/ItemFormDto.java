package com.shop.dto;

import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.ui.Model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ItemFormDto {

    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String itemNm;

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private Integer price;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String itemDetail;

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private Integer stockNumber;

    private ItemSellStatus itemSellStatus;

    private List<ItemImgDto> itemImgDtoList = new ArrayList<>(); //상품 저장 후 수정시에 상품 이미지정보를 저장

    private List<Long> itemImgIds = new ArrayList<>(); //상품의 이미지 id를 저장

    private static ModelMapper modelMapper = new ModelMapper();

    public Item createItem(){
        return modelMapper.map(this, Item.class); //modelMapper 를 이용하여 엔티티와 DTO간의 데이터를 복사후 복사한 객체를 반환
    }                                                   //modelMapper(라이브러리): 서로 다른 클래스의 값을 필드의 이름과 자료형이 같으면 getter setter를 통해 값을 복사후 객체반환

    public static ItemFormDto of(Item item){
        return modelMapper.map(item, ItemFormDto.class);  // 위와 동일
    }

}
