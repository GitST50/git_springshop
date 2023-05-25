package com.shop.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.Security;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor(){   //Authentication(인증): 보호된 리소스에 접근한 대상에 대하여 이 유저가 누구인지, 앱의 작업응ㄹ 수행해도 되는 주체인지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = "";
        if(authentication != null)
        {
            userId = authentication.getName();  //현재 로그인 한 사용자의 정보를 조회후 이름을 등록자와 수정자로 지정
        }
        return Optional.of(userId);  //인자로 null값을 받지않음
    }
}
