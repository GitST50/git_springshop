package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    public String itemForm(Model model){

        model.addAttribute("itemFormDto", new ItemFormDto());

        return "/item/itemForm";
    }

    @PostMapping(value = "/admin/item/new")
    public String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model,
                          @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList)
    {
        if(bindingResult.hasErrors()) //상품등록시 필수값없으면 다시 상품등록페이지로
        {
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) //상품 등록시 첫번째 이미지가 empty라면 에러메시지 출력후 상품 등록페이지로 이동
        {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력 값 입니다.");
            return "item/itemForm";
        }

        try{
            itemService.saveItem(itemFormDto, itemImgFileList); //파라미터로 상품정보와 상품이미지 정보를 담고있는 itemImgFileList 넘겨줌
        } catch (Exception e){
            model.addAttribute("errorMessage", "상품등록중 에러가 발생하였습니다.");
            return "item/itemForm";
        }

        return "redirect:/";  //상품이 정상등록되었다면 메인페이지로 리다이렉트
    }
}
