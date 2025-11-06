package com.yuno.api.dto;

import lombok.Data;

@Data
public class CreateCommentRequest {
    private String content;
    private int postId;
}
