package com.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //사용자의 요청 처리후(모델 객체를 만들어 데이터를담고) View 를 반환
public class MainController {

    @GetMapping(value = "/")
    public String main(){  //회원가입후 메인페이지로 이동
        return "main";
    }

}
