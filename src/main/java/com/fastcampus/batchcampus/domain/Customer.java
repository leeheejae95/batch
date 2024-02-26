package com.fastcampus.batchcampus.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Customer {

    private Long id;

    private String name;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

    public Customer(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.updateAt = LocalDateTime.now();
    }
}
