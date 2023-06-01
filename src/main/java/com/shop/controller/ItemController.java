package com.shop.controller;

import com.shop.dto.ItemFormDto;
import com.shop.dto.ItemSearchDto;
import com.shop.entity.Item;
import com.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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

    @GetMapping(value = "/admin/item/{itemId}")
    public String itemDtl(@PathVariable("itemId") Long itemId, Model model)
    {
        try{
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);  //조회한 상품 데이터를 모델에 담아서 뷰로 전달
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (EntityNotFoundException e){
            model.addAttribute("error Message", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemForm";
        }

        return "item/itemForm";
    }

    @PostMapping(value = "/admin/item/{itemId}")
    public String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile") List<MultipartFile>
                             itemImgFileList, Model model){
        if(bindingResult.hasErrors()){
            return "item/itemForm";
        }

        if(itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null)
        {
            model.addAttribute("errorMessage", "첫번째 상품 이미지는 필수 입력값입니다.");
            return "item/itemForm";
        }

        try
        {
            itemService.updateItem(itemFormDto, itemImgFileList); //상품 수정
        }
        catch (Exception e)
        {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생하였습니다.");
            return "item/itemForm";
        }
        return "redirect:/";
    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"}) //상품관리화면에서 url에 페이지번호가 없는경우/있는경우 두가지 매핑
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page")Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3); //페이징을 위해 PageRequest.of메소드로 Pageable객체 생성
                                                                                      //param1:조회할 페이지번호 param2:한번에 가지고 올 데이터 수
                                                                                      //url에 페이지번호가 없으면 0페이지 조회
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable); //조회조건과 페이징정보를 넘겨 Page<Item>을 반환
        model.addAttribute("items", items);  //조회한 상품데이터와 페이징정보를 뷰에전달
        model.addAttribute("itemSearchDto", itemSearchDto);  //페이지 전환시 기존 검색조건을 유지하고 이동하도록 뷰에 전달
        model.addAttribute("maxPage", 5); //상품관리 메뉴 하단에 보여줄 페이지의 최대 개수
        return "item/itemMng";
    }

    @GetMapping(value = "/item/{itemId}")
    public String itemDtl(Model model, @PathVariable("itemId") Long itemId){
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }
}
