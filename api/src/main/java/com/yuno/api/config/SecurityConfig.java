package com.yuno.api.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//thư viện để encode password
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import com.yuno.api.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration //báo cho spring biết đây là file chứa các cài đặt (beans)
//file này sẽ đc đọc đầu tiên
public class SecurityConfig {
    @Bean //báo rằng đây là beans, hãy lưu nó vào spring
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean //luật mới
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
            .csrf(csrf -> csrf.disable()) //tắt CFRS (vì dùng API chứ ko dùng form web)
            .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/auth/**").permitAll() //rule 1: cho phép TẤT CẢ yêu cầu đến /api/auth/** (bao gồm register)
            .anyRequest().authenticated() //rule 2: mọi yêu cầu khác đều phải xác thực (đăng nhập)
        );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsServiceImpl userDetailsService){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //khi ai đó gõ username, dùng UserDetailsServiceImpl để tìm
        authProvider.setUserDetailsService(userDetailsService);
        //dùng passwordEncoder để so sánh mật khẩu
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    //được gọi trong AuthController, nó sẽ dùng AuthenticationProvider ta vừa tạo để làm việc
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }
    
}
