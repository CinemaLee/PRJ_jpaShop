package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AndRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception { // 여기에 명시된 url만 검증을 하겠다.
        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        http.authorizeRequests()// 시큐리티 처리에 HttpServletRequest를 이용하겠다.
                .mvcMatchers("/","/members/**","/item/**","/images/**").permitAll() // 모든 사용자가 인증없이 해당 경로에 접근할 수 있도록
                .mvcMatchers("/admin/**").hasRole("ADMIN") // ADMIN만 접근할 수 있도록
                .anyRequest().authenticated(); // 위 경로를 제외한 나머지 경로들은 모두 인증을 요구하도록 설정

        http.exceptionHandling() // 인증되지 않은 사용자가 접근했을 때 수행되는 핸들러.
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception { // Security에서 인증은 AuthenticationManager로 이루어진다.
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/js/**","/img/**"); // static 하위 파일은 인증 무시하도록 설정.
    }
}
