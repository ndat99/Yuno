package com.yuno.api.dto;

import lombok.Data;

//khai báo đây là DTO
@Data //auto tạo getter setter cho các thuộc tính
public class RegisterRequest {
    private String username;
    private String password; //pass lúc chưa băm
    private String name;
    private String email;
}
