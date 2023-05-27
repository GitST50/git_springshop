package com.shop.service;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemImgDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
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

    @Transactional(readOnly = true) //상품데이터를 읽어오는 트랜잭션을 읽기전용으로 설정: JPA가 변경감지를 수행하지않아 성능향상
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId); //해당상품이미지 조회, 오름차순
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList){  //조회한 ItemImg 를 ItemImgDto 로 만들어서 List에 추가
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId).orElseThrow(EntityNotFoundException::new); //상품의 아이디를 통해 상품 엔티티를 조회 존재하지않으면 Exception
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);

        return itemFormDto;
    }
}
