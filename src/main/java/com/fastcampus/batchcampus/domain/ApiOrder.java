package com.fastcampus.batchcampus.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiOrder {

    public String id;

    public Long customerId;

    private String url;

    private State state;

    private String createdAt;

    public enum State{
        SUCCESS, FAIL
    }
}
