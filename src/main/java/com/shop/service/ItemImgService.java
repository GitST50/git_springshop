package com.shop.service;

import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}") //application.properties의 itemImgLocation값을 불러와서 itemImgLocation에 넣어줌
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        if(!StringUtils.isEmpty(oriImgName)) //상품의 이미지가 등록되었다면 저장할경로, 파일의 이름, 파일의 바이트배열을 파일 업로드 parameter로 uploadFile을 호출
        {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes()); //호출 결과 로컬에 저장된 파일의 이름을 imgName변수에 저장
            imgUrl = "/images/item/" + imgName; //저장한 상품이미지를 불러올 경로를 설정, 외부 리소스를 불러오는 urlPatterns로 WebMvcConfig에서 /images/**설정
        }                                       //프로퍼티 파일에서 설정한 uploadPath 프로퍼티 경로인 C:/shop/ 아래의 item에 이미지를 저장하므로 상품이미지 불러오는 경로로
                                                //images/item/를 붙여주기

        //입력받은 상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgName, imgUrl); //imgName:실제 로커에 저장된 상품 이미지파일의 이름
        itemImgRepository.save(itemImg);                    //oriImgName:원본 이미지 파일의 이름
    }                                                       //imgUrl:업로드 결과 로컬에 저장된 상품 이미지 파일을 불러오는 경로

}
