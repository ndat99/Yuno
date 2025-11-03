package com.yuno.api.config;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.yuno.api.security.JwtAuthFilter;
import com.yuno.api.security.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration //báo cho spring biết đây là file chứa các cài đặt (beans)
//file này sẽ đc đọc đầu tiên
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsServiceImpl userDetailsService){
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }
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
                .requestMatchers(HttpMethod.GET, "/api/posts").permitAll() //rule 2: cho phép gọi GET đến /api/posts
                .requestMatchers(HttpMethod.POST, "/api/posts").authenticated() //rule 3: phải có vé mới cho POST posts
                .anyRequest().authenticated() //rule 4: mọi yêu cầu khác đều phải xác thực (đăng nhập)
        )
        .authenticationProvider(authenticationProvider()) //cài hệ thống xác thực vào
        //cài người soát vé Jwt Filter vào dây chuyền vào trước cái Filter mặc định (UsernamePasswordAuthenticationFilter)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
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
