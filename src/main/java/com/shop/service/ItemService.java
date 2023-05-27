package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Item item = itemFormDto.createItem(); //dto로 입력받은 데이터로 item생성
        itemRepository.save(item); //item 저장

        //이미지 등록
        for(int i=0; i < itemImgFileList.size(); i++)
        {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i==0) //첫번째 이미지면 대표상품 이미지여부를 Y로 저장 나머지는 N
            {
                itemImg.setRepimgYn("Y");
            }
            else
            {
                itemImg.setRepimgYn("N");
            }
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); //상품의 이미지정보를 저장
        }
        return item.getId();
    }
}
