package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login") //로그인 페이지 url 설정
                .defaultSuccessUrl("/")  // 로그인 성공시 이동할 url설정
                .usernameParameter("email")  //로그인시 사용할 파라미터 이름으로 email 지정
                .failureUrl("/members/login/error")  //로그인 실패시 이동할 url
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) //로그아웃 url
                .logoutSuccessUrl("/") //로그아웃 성공시 이동할 url
                ;

        http.authorizeRequests()  //HttpServletRequest를 시큐리티 처리에 이용
                .mvcMatchers("/css/**","/js/**","img/**").permitAll() //permitAll로 모든 사용자가 로그인없이 접근할수있게 함(메인페이지등)
                .mvcMatchers("/","/members/**","item/**","/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN") //계정이 ADMIN일 경우만 접근 가능
                .anyRequest().authenticated() //의외의 나머지 경로들은 모두 인증 요구
                ;
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                ;


        return http.build();

    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }





}
