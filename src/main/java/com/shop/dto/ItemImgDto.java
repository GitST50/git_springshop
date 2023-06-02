package com.shop.dto;

import com.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper(); //ModelMapper객체를 멤버변수로 추가

    public static ItemImgDto of(ItemImg itemImg){
        return modelMapper.map(itemImg, ItemImgDto.class); //ItemImg 엔티티를 받아서 해당 객체의 자료형과 멤버변수의 이름이 같을때 ItemImgDto로 값을 복사후 반환
    }

}
