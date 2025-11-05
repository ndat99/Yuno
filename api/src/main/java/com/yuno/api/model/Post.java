package com.yuno.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "posts")
@Data
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(columnDefinition = "NVARCHAR(1000)")
    private String content;

    @JoinColumn(name="user_id")
    private int user_id;

    @ManyToOne //nhiều post thuộc về một user
    @JoinColumn(name="user_id", insertable = false, updatable = false)
    private User user;
}
