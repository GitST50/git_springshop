package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;



@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);

        return memberRepository.save(member); //memberRepositroy(JpaRepository)에 member를 받아와서 인서트
    }

    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    //UserDetailService 인터페이스는 DB에서 회원정보를 가져옴
    //loadUserByUserName 메소드가 존재:회원정보를 조회후 사용자 정보,권한을 갖는 UserDetails 인터페이스 반환

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException { //로그인할 유저의 이메일을 파라미터로 전달받음
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }
        return User.builder() //UserDetail을 구현하고있는 User객체 반환: 생성자로 회원의 이메일 비번, role을 파라미터로 넘겨줌
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
