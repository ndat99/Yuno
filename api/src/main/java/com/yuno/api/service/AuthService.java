package com.yuno.api.service;

import com.yuno.api.dto.AuthResponse;
import com.yuno.api.dto.LoginRequest;
import com.yuno.api.model.User;
import com.yuno.api.repository.UserRepository;
import com.yuno.api.security.JwtService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service //khai báo đây là service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    //hàm dựng
    public AuthService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        AuthenticationManager authenticationManager,
                        JwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    //đăng ký
    public User registerUser(String username, String password, String name, String email){
        String hashedPassword = passwordEncoder.encode(password);
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(hashedPassword);
        newUser.setName(name);
        newUser.setEmail(email);
        
        //cho repository lưu user này (hàm save() do JpaRepository tự cung cấp)
        return userRepository.save(newUser);
    }

    //đăng nhập
    public AuthResponse loginUser(LoginRequest loginRequest){
        //đưa form cho AuthenticationManager xác thực
        //nó sẽ gọi UserDetailsServiceImpl
        //và dùng PasswordEncoder để so sánh mk
        //nếu sai username/pass thì sẽ nó ra lỗi
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginRequest.getUsername(),
                    loginRequest.getPassword()
                )
        );
        //nếu ko lỗi thì tạo token
        String token = jwtService.generateToken((loginRequest.getUsername()));
        return new AuthResponse(token);
    }

}
