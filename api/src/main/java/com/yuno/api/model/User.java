package com.yuno.api.model; //đại loại là namespace

// import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity //khai báo đây là Entity (bản sao của một table)
@Table(name = "users") //ứng với table users
@Data //auto tạo getter setter

public class User {
    @Id //đánh dấu khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) //báo cho JPA biết ID tự tăng    
    private int id;
    
    private String username;
    private String password;
    private String name;
    private String email;
}
