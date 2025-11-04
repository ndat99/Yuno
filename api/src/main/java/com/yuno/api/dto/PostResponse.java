package com.yuno.api.dto;

import lombok.Data;

@Data
public class PostResponse {
    private int id;
    private String content;
    private UserResponse user;
}
