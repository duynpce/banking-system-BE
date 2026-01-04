package com.example.banking_system.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// common dto used to return response
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    private String message;
    private T data;
}
