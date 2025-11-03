package com.yuno.api.security;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component //đây là 1 công cụ
public class JwtAuthFilter extends OncePerRequestFilter{
    private final JwtService jwtService; //cái tạo vé
    private final UserDetailsServiceImpl userDetailsServiceImpl; //cái chỉ đường

    public JwtAuthFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsServiceImpl){
        this.jwtService = jwtService;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{
        //Lấy header tên là Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userName;

        //kiểm tra xem token có tồn tại và có đúng kiểu (Bearer) ko
        if (authHeader == null || !authHeader.startsWith("Bearer")){
            filterChain.doFilter(request, response);
            return;
        }
        
        //tách token ra khỏi Bearer
        jwt = authHeader.substring(7); //Bearer có 7 ký tự
        //đưa token cho JwtService để lấy username
        userName = jwtService.getUsernameFromToken(jwt);
        //nếu có username và người này chưa đc xác thực trong hệ thống
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null){
            //dùng cái chỉ đường để tìm user trong CSDL
            UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(userName);
            //dùng cái tạo token để kiểm tra token có hợp lệ ko
            if (jwtService.validateToken(jwt)){
                //nếu token hợp lệ, tạo một phiên xác thực
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, //ko cần password
                    userDetails.getAuthorities()
                );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                //Báo cho SecurityContext biết tên người yêu cầu
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        //Cho yêu cầu đi tiếp (dù token có hợp lệ hay ko)
        filterChain.doFilter(request, response);
    }
}
