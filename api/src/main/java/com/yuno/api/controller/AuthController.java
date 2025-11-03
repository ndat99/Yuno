package com.yuno.api.controller;

import com.yuno.api.dto.AuthResponse;
import com.yuno.api.dto.LoginRequest;
import com.yuno.api.dto.RegisterRequest;
import com.yuno.api.model.User;
import com.yuno.api.service.AuthService;

import org.springframework.security.core.AuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController //khai báo đây là API controller
@RequestMapping("/api/auth") //báo cho Spring: mọi yêu cầu đều bắt đầu bằng /api/auth
public class AuthController {
    private final AuthService authService;
    //cần một authservice để làm việc
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register") //báo cho Spring: "ai gọi đến /api/auth/register bằng POST thì vào đây"
    public ResponseEntity<User> register(@RequestBody RegisterRequest registerRequest) {
        //nhận form registerRequest từ bên ngoài
        //đưa form cho authService quản lý
        User registeredUser = authService.registerUser(
            registerRequest.getUsername(),
            registerRequest.getPassword(),
            registerRequest.getName(),
            registerRequest.getEmail()
        );     
        //trả lời cho bên ngoài "đký thành công" và gửi ttin user vừa tạo
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
        try{
            //đưa form cho AuthService
            AuthResponse authResponse = authService.loginUser(loginRequest);
            //nếu thành công, trả về túi đựng token
            return ResponseEntity.ok(authResponse);

        } catch (AuthenticationException e){
            //nếu báo lỗi (username/password), trả về lỗi 401 Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();            
        }
    }
    
}
