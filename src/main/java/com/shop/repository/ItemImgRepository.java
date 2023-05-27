package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    List<ItemImg> findByIdOrderByIdAsc(Long itemId); //이미지가 잘 저장되었는지 확인

}
